package edu.mobapde.selina.shuffle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
        mainSongFragment.hideBack();
        mainAlbumFragment.hideBack();

        fragmentStack.push(mainSongFragment);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.buildFragment, mainSongFragment);
        ft.commit();
        swapColors(fragmentStack.peek());

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

                /*while (fragmentStack.size() > 2){
                    fragmentStack.pop();
                }*/

                fragmentStack.push(mainSongFragment);
                swapColors(fragmentStack.peek());
            }
        });

        albumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                active.push(ALBUM);

                /*while (fragmentStack.size() > 2){
                    fragmentStack.pop();
                }*/

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
                swapColors(fragmentStack.peek());
            }
        });

        artistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                active.push(ARTIST);

                /*while (fragmentStack.size() > 2){
                    fragmentStack.pop();
                }*/

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
                swapColors(fragmentStack.peek());

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
    public void up() {
        switch(active.peek()) {
            case ARTIST:
                switch(artistStat) {
                    case ALBUM:
                        artistStat = ARTIST;
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.buildFragment,mainArtistFragment).commit();
                        break;
                    case SONG:
                        artistStat = ALBUM;
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.buildFragment,artistAlbumFragment).commit();
                        break;
                }
                break;
            case ALBUM:
                if( albumStat == SONG ) {
                    albumStat = ALBUM;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.buildFragment,mainAlbumFragment).commit();
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
            (new DialogFragment() {
                @Override
                public Dialog onCreateDialog(Bundle savedInstanceState) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                            .setTitle("Exiting")
                            .setMessage("Are you sure you want to exit without saving?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().finish();
                                    dismiss();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dismiss();
                                }
                            });
                    return builder.create();
                }
            }).show(getFragmentManager(),"");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    public boolean back() {
//        if( active.size() > 1 ) {
//            active.pop();
//            fragmentStack.pop();
//            swapColors(fragmentStack.peek());
//            if( fragmentStack.peek() instanceof SongFragment ) {
//                ((SongFragment) fragmentStack.peek()).setSongs(songs);
//            }
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.buildFragment, fragmentStack.peek()).commit();
//            return true;
//        } else {
//            return false;
//        }
//    }

    public void swapColors(Fragment fragment) {
        if( fragment.equals(mainArtistFragment)/*fragment instanceof ArtistFragment*/ ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                allButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1, null));
                albumButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1, null));
                artistButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2, null));
            } else {
                allButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
                albumButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
                artistButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2));
            }
        } else if( fragment.equals(mainAlbumFragment)/*fragment instanceof AlbumFragment*/ ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                allButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1,null));
                albumButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2,null));
                artistButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1,null));
            } else {
                allButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
                albumButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2));
                artistButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
            }
        } else if ( fragment.equals(mainSongFragment)/*fragment instanceof SongFragment*/ ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                allButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2,null));
                albumButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1,null));
                artistButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1,null));
            } else {
                allButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2));
                albumButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
                artistButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
            }
        }
    }

}
