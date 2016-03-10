package edu.mobapde.selina.shuffle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class BuildPlaylistActivity extends AppCompatActivity
                                implements SongFragment.OnFragmentInteractionListener,
                                            ArtistFragment.OnFragmentInteractionListener,
                                            AlbumFragment.OnFragmentInteractionListener {
    public static final int ALBUM = 0;
    public static final int ARTIST = 1;
    public static final int SONG = 2;

    private Playlist p;
    private DBManager dbm;

    private EditText playlistField;
    private Button saveButton;
    private Button allButton;
    private Button albumButton;
    private Button artistButton;

    private SongFragment mainSongFragment;

    private int active;

    private int albumStat;
    private AlbumFragment mainAlbumFragment;
    private SongFragment albumSongFragment;

    private int artistStat;
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

        albumStat = ALBUM;
        artistStat = ARTIST;

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

        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                active = SONG;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.buildFragment, mainSongFragment).commit();
            }
        });

        albumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                active = ALBUM;
            }
        });

        artistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                active = ARTIST;

            }
        });
    }

    @Override
    public void onFragmentInteraction(int source, String value) {
        switch(active) {
            case ALBUM:
                albumSongFragment = SongFragment.newInstance(value);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.buildFragment,albumSongFragment).addToBackStack(null)
                        .commit();
                albumStat = SONG;
                break;
            case ARTIST:
                switch(source) {
                    case ARTIST:
                        artistStat = ALBUM;
                        break;
                    case ALBUM:
                        break;
                    default:
                }
                break;
            default:
        }
    }

    @Override
    public void onFragmentInteraction(long songId, boolean checked) {
        if( checked ) {
            songs.add(songId);
        } else {
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

    public void back(int source) {
        switch(active) {
            case ALBUM:
                albumSongFragment = SongFragment.newInstance(value);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.buildFragment,albumSongFragment).commit();
                albumStat = SONG;
                break;
            case ARTIST:
                switch(source) {

                }
                break;
            default:
        }
    }
}
