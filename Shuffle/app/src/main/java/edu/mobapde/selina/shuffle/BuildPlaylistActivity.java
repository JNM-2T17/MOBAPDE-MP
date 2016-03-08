package edu.mobapde.selina.shuffle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class BuildPlaylistActivity extends AppCompatActivity
                                    implements SongFragment.OnFragmentInteractionListener,
                                                AlbumFragment.OnFragmentInteractionListener,
                                                ArtistFragment.OnFragmentInteractionListener {
    private Playlist p;
    private DBManager dbm;

    private EditText playlistField;
    private Button saveButton;
    private Button allButton;
    private Button albumButton;
    private Button artistButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_playlist);

        playlistField = (EditText)findViewById(R.id.playlistField);
        saveButton = (Button)findViewById(R.id.saveButton);
        allButton = (Button)findViewById(R.id.allButton);
        albumButton = (Button)findViewById(R.id.albumButton);
        artistButton = (Button)findViewById(R.id.artistButton);

        Intent playlist = getIntent();
        int id = playlist.getExtras().getInt(CreatePlaylistActivity.ID_KEY);
        String pName = playlist.getExtras().getString(CreatePlaylistActivity.NAME_KEY);
        Log.i("BuildPlaylistActivity",pName);
        if( pName != null ) {
            playlistField.setText(pName);
        }

    }

    @Override
    public void onFragmentInteraction(String source, String select) {
        //if from song
        //mark as selected or unselected
        //if from album
        //start new songfragment for that album
        //if from artist
        //start new albumfragment for that artist
    }
}
