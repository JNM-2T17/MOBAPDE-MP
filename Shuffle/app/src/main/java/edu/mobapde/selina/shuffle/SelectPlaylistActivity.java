package edu.mobapde.selina.shuffle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class SelectPlaylistActivity extends AppCompatActivity
                                    implements SelectPlaylistFragment.OnFragmentInteractionListener,
                                            SelectAlbumFragment.OnFragmentInteractionListener,
                                            SelectArtistFragment.OnFragmentInteractionListener {
    public static final String LIST = "list";
    public static final String PLAYLIST = "playlist";
    public static final String VAL = "val";

    private LinearLayout choicePanel;
    private Button playlistButton;
    private Button albumButton;
    private Button artistButton;

    private Fragment playlistFragment;
    private Fragment albumFragment;
    private Fragment artistFragment;

    private int listMode;
    private long playlist;
    private String selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_playlist);

        choicePanel = (LinearLayout)findViewById(R.id.choicePanel);
        playlistButton = (Button)findViewById(R.id.playlistButton);
        albumButton = (Button)findViewById(R.id.albumButton);
        artistButton = (Button)findViewById(R.id.artistButton);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int level = sp.getInt(MainActivity.LEVEL,SettingsActivity.TITLE);
        switch(level) {
            case SettingsActivity.ARTIST:
                choicePanel.setVisibility(View.GONE);
                break;
            case SettingsActivity.ALBUM:
                albumButton.setVisibility(View.GONE);
                break;
            default:
        }

        playlistFragment = SelectPlaylistFragment.newInstance();
        albumFragment = SelectAlbumFragment.newInstance();
        artistFragment = SelectArtistFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.selectFragment,playlistFragment)
                .commit();

        playlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.selectFragment, playlistFragment)
                        .commit();
                swapColors(playlistFragment);
            }
        });

        artistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.selectFragment,artistFragment)
                        .commit();
                swapColors(artistFragment);
            }
        });

        albumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.selectFragment,albumFragment)
                        .commit();
                swapColors(albumFragment);
            }
        });
    }

    public void startGame(int mode) {
        if( mode == 0 ) {
            Intent timeAttack = new Intent(getBaseContext(),TimeAttack.class);
            timeAttack.putExtra(LIST,listMode);
            timeAttack.putExtra(VAL,selected);
            timeAttack.putExtra(PLAYLIST,playlist);
            startActivity(timeAttack);
        } else {
            Intent songRush = new Intent(getBaseContext(),SongRush.class);
            songRush.putExtra(LIST,listMode);
            songRush.putExtra(VAL,selected);
            songRush.putExtra(PLAYLIST,playlist);
            startActivity(songRush);
        }
    }

    @Override
    public void onFragmentInteraction(Playlist p) {
        playlist = p.id();
        onFragmentInteraction(BuildPlaylistActivity.PLAYLIST,null);
    }

    @Override
    public void onFragmentInteraction(int fragmentType, String value) {
        listMode = fragmentType;
        GameModeDialog gmd = new GameModeDialog();
        switch(fragmentType) {
            case BuildPlaylistActivity.ALBUM:
            case BuildPlaylistActivity.ARTIST:
                selected = value;
                //fall-through
            case BuildPlaylistActivity.PLAYLIST:
                gmd.show(getFragmentManager(),"");
                break;
            default:
        }
    }

    public void swapColors(Fragment fragment) {
        if( fragment.equals(artistFragment)/*fragment instanceof ArtistFragment*/ ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                playlistButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1, null));
                albumButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1, null));
                artistButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2, null));
            } else {
                playlistButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
                albumButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
                artistButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2));
            }
        } else if( fragment.equals(albumFragment)/*fragment instanceof AlbumFragment*/ ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                playlistButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1, null));
                albumButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2,null));
                artistButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1,null));
            } else {
                playlistButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
                albumButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2));
                artistButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
            }
        } else if ( fragment.equals(playlistFragment)/*fragment instanceof SongFragment*/ ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                playlistButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2, null));
                albumButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1,null));
                artistButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1,null));
            } else {
                playlistButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2));
                albumButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
                artistButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
            }
        }
    }

}
