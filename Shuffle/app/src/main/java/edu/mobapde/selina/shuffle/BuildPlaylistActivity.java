package edu.mobapde.selina.shuffle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Stack;

public class BuildPlaylistActivity extends AppCompatActivity
                                implements SongFragment.OnFragmentInteractionListener,
                                            ArtistFragment.OnFragmentInteractionListener,
                                            AlbumFragment.OnFragmentInteractionListener {
    public static final int ALBUM = 0;
    public static final int ARTIST = 1;
    public static final int SONG = 2;
    public static final int PLAYLIST = 3;

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

    private long playlistId;

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
        fragmentStack = new Stack<Fragment>();
        active = new Stack<Integer>();

        songs = new ArrayList<Long>();

        playlistId = 0;
        Intent playlist = getIntent();
        Bundle b = playlist.getExtras();
        if( b != null ) {
            playlistId = playlist.getExtras().getLong(CreatePlaylistActivity.ID_KEY);
            String pName = b.getString(CreatePlaylistActivity.NAME_KEY);
            playlistField.setText(pName);
            DBManager dbm = new DBManager(getBaseContext());
            p = dbm.getPlayList(playlistId);
            for(int i = 0; i < p.size(); i++) {
                songs.add(p.song(i));
            }
        }

        long[] sel = new long[songs.size()];
        for(int i = 0; i < sel.length; i++) {
            sel[i] = songs.get(i).longValue();
        }
        mainSongFragment = SongFragment.newInstance(null,sel);
        mainAlbumFragment = AlbumFragment.newInstance(null);
        mainArtistFragment = ArtistFragment.newInstance();
        fragmentStack.push(mainSongFragment);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.buildFragment, mainSongFragment);
        ft.commit();

        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                active.push(SONG);
                Log.i("BuildPlaylistActivity","Setting Songs allbutton");
                for(Long l: songs) {
                    Log.i("BuildPlaylistActivity","with " + l);
                }
                mainSongFragment.setSongs(songs);
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
                        albumSongFragment.setSongs(songs);
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
                        artistSongFragment.setSongs(songs);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.buildFragment, artistSongFragment).commit();
                        fragmentStack.push(artistSongFragment);
                        break;
                    default:
                }

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = playlistField.getText().toString();
                if( name.length() > 0 ){
                    DBManager db = new DBManager(getBaseContext());
                    long[] songList = new long[songs.size()];
                    for(int i = 0; i < songList.length; i++) {
                        songList[i] = songs.get(i).longValue();
                    }

                    //if new
                    if( playlistId == 0 ) {
                        db.addPlaylist(name,songList);
                    } else {
                        db.updatePlaylist(playlistId,name,songList);
                    }
                    finish();
                } else {
                    Toast.makeText(getBaseContext(),"Please input a playlist name"
                                    ,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainSongFragment.setSongs(songs);
        if( albumSongFragment != null) {
            albumSongFragment.setSongs(songs);
        }
        if( artistSongFragment != null ) {
            artistSongFragment.setSongs(songs);
        }
    }

    @Override
    public void onFragmentInteraction(int source, String value) {
        long[] sel = new long[songs.size()];
        for(int i = 0; i < sel.length; i++) {
            sel[i] = songs.get(i).longValue();
        }
        switch(active.peek()) {
            case ALBUM:
                active.push(ALBUM);
                albumSongFragment = SongFragment.newInstance(value,sel);
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
                        artistAlbumFragment = AlbumFragment.newInstance(value);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.buildFragment,artistAlbumFragment).commit();
                        fragmentStack.push(artistAlbumFragment);
                        break;
                    case ALBUM:
                        artistStat = SONG;
                        artistSongFragment = SongFragment.newInstance(value,sel);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.buildFragment,artistSongFragment).commit();
                        fragmentStack.push(artistSongFragment);
                        break;
                    default:
                }
                break;
            default:
        }
    }

    @Override
    public void onFragmentInteraction(long songId, boolean checked) {
        Log.i("BuildPlaylistActivity","START onFragmentInteraction");
        if( checked ) {
            Log.i("BuildPlaylistActivity","Adding " + songId);
            songs.add(songId);
        } else {
            Log.i("BuildPlaylistActivity","Removing " + songId);
            songs.remove(songs.indexOf(songId));
        }
        for(long l: songs) {
            Log.i("BuildPlaylistActivity","Song: " + l);
        }
        Log.i("BuildPlaylistActivity","mainSongFragment");
        mainSongFragment.setSongs(songs);
        if( albumSongFragment != null) {
            Log.i("BuildPlaylistActivity","albumSongFragment");
            albumSongFragment.setSongs(songs);
        }
        if( artistSongFragment != null ) {
            Log.i("BuildPlaylistActivity","artistSongFragment");
            artistSongFragment.setSongs(songs);
        }
        Log.i("BuildPlaylistActivity","END onFragmentInteraction");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            if( back() ) return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean back() {
        if( active.size() > 1 ) {
            active.pop();
            fragmentStack.pop();
            if( fragmentStack.peek() instanceof SongFragment ) {
                ((SongFragment)fragmentStack.peek()).setSongs(songs);
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.buildFragment, fragmentStack.peek()).commit();
            return true;
        } else {
            return false;
        }
    }
}
