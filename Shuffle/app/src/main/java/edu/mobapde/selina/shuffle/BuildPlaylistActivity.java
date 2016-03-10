package edu.mobapde.selina.shuffle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Stack;

public class BuildPlaylistActivity extends AppCompatActivity
                                implements SongFragment.OnFragmentInteractionListener,
                                            ArtistFragment.OnFragmentInteractionListener {
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
    private Stack<Integer> active;
    private Stack<Fragment> fragmentStack;

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
        fragmentStack.push(mainSongFragment);

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
                active.push(SONG);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.buildFragment, mainSongFragment).commit();
                fragmentStack.push(mainSongFragment);
            }
        });

        albumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                active.push(ALBUM);
                switch(albumStat) {
                    case ALBUM:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.buildFragment, mainAlbumFragment).commit();
                        fragmentStack.push(mainAlbumFragment);
                        break;
                    case SONG:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.buildFragment, albumSongFragment).commit();
                        fragmentStack.push(albumSongFragment);
                        break;
                    default:
                }
            }
        });

        artistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                active.push(ARTIST);
                switch(artistStat) {
                    case ARTIST:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.buildFragment, mainArtistFragment).commit();
                        fragmentStack.push(mainArtistFragment);
                        break;
                    case ALBUM:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.buildFragment, artistAlbumFragment).commit();
                        fragmentStack.push(artistAlbumFragment);
                        break;
                    case SONG:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.buildFragment, artistSongFragment).commit();
                        fragmentStack.push(artistSongFragment);
                        break;
                    default:
                }

            }
        });
    }

    @Override
    public void onFragmentInteraction(int source, String value) {
        switch(active.peek()) {
            case ALBUM:
                active.push(ALBUM);
                albumSongFragment = SongFragment.newInstance(value);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.buildFragment, albumSongFragment).commit();
                fragmentStack.push(albumSongFragment);
                albumStat = SONG;
                break;
            case ARTIST:
                active.push(ARTIST);
                switch(source) {
                    case ARTIST:
                        artistStat = ALBUM;
                        break;
                    case ALBUM:
                        artistStat = SONG;
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

    public void back() {

    }

    public void back(int source) {
        switch(active.peek()) {
            case ALBUM:
                switch(source) {
                    case ALBUM:
                        artistStat = ALBUM;
                        break;
                    case SONG:
                        artistStat = SONG;
                        break;

                    default:
                }
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
