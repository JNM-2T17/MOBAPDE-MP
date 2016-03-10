package edu.mobapde.selina.shuffle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class BuildPlaylistActivity extends AppCompatActivity
                                implements SongFragment.OnFragmentInteractionListener {
    private Playlist p;
    private DBManager dbm;

    private EditText playlistField;
    private Button saveButton;
    private Button allButton;
    private Button albumButton;
    private Button artistButton;

    private SongFragment mainSongFragment;

    private AlbumFragment mainAlbumFragment;
    private SongFragment albumSongFragment;

    private ArtistFragment mainArtistFragment;
    private AlbumFragment artistAlbumFragment;
    private SongFragment artistSongFragment;

    private int playlistId;

    private ArrayList<Long> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_playlist);

        playlistField = (EditText)findViewById(R.id.playlistField);
        saveButton = (Button)findViewById(R.id.saveButton);
        allButton = (Button)findViewById(R.id.allButton);
        albumButton = (Button)findViewById(R.id.albumButton);
        artistButton = (Button)findViewById(R.id.artistButton);

        mainSongFragment = SongFragment.newInstance(null);

        songs = new ArrayList<Long>();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.buildFragment, mainSongFragment);
        ft.commit();

        playlistId = 0;
        Intent playlist = getIntent();
        Bundle b = playlist.getExtras();
        if( b != null ) {
            playlistId = playlist.getExtras().getInt(CreatePlaylistActivity.ID_KEY);
            String pName = b.getString(CreatePlaylistActivity.NAME_KEY);
            playlistField.setText(pName);
        }

    }

    //@Override
    public void onFragmentInteraction(String source, String select) {
        //if from song
        //mark as selected or unselected
        //if from album
        //start new songfragment for that album
        //if from artist
        //start new albumfragment for that artist
    }

    @Override
    public void onFragmentInteraction(long songId, boolean checked) {
        if( checked ) {
            Log.i("BuildPlaylistActivity","Adding " + songId);
            songs.add(songId);
        } else {
            Log.i("BuildPlaylistActivity","Removing " + songId);
            for( int i = 0; i < )
            songs.remove(songs.indexOf(songId));
        }
        mainSongFragment.setSongs(songs);
        if( albumSongFragment != null) {
            albumSongFragment.setSongs(songs);
        }
        if( artistSongFragment != null ) {
            artistSongFragment.setSongs(songs);
        }
    }
}
