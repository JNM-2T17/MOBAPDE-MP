package edu.mobapde.selina.shuffle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;

public class SongRush extends AppCompatActivity {
    private ArrayList<Song> playlist;
    private DBManager dbm;
    private MusicProvider mp;

    private int currSong;
    private Handler fifteen;
    private Runnable songTimer;
    private boolean gameStart;
    private int score;
    private int type;
    private String compare;

    private Button startButton;
    private Button skipButton;
    private Button guessButton;
    private EditText guessField;

    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_rush);

        Intent list = getIntent();
        type = list.getExtras().getInt(SelectPlaylistActivity.LIST);
        dbm = new DBManager(getBaseContext());
        mp = new MusicProvider(getContentResolver());
        playlist = new ArrayList<Song>();
        switch(type) {
            case BuildPlaylistActivity.PLAYLIST:
                Playlist p = dbm.getPlayList(list.getExtras()
                                                .getLong(SelectPlaylistActivity.PLAYLIST));
                for(int i = 0; i < p.size(); i++) {
                    playlist.add(mp.getSong(p.song(i)));
                }
                break;
            case BuildPlaylistActivity.ALBUM:
                playlist = (ArrayList<Song>)mp.getSongsIn(list.getExtras()
                                                            .getString(SelectPlaylistActivity.VAL));
                break;
            case BuildPlaylistActivity.ARTIST:
                playlist = (ArrayList<Song>)mp.getSongsOf(list.getExtras()
                                                            .getString(SelectPlaylistActivity.VAL));
                break;
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        type = sp.getInt(MainActivity.LEVEL,SettingsActivity.TITLE);

        startButton = (Button)findViewById(R.id.startButton);
        skipButton = (Button)findViewById(R.id.skipButton);
        guessField = (EditText)findViewById(R.id.guessField);
        guessButton = (Button)findViewById(R.id.guessButton);
        skipButton.setVisibility(View.GONE);
        guessField.setVisibility(View.GONE);
        guessButton.setVisibility(View.GONE);

        gameStart = false;

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startButton.setVisibility(View.GONE);
                if( gameStart ) {
                    finishGame();
                } else {
                    startButton.setText("Stop");
                    startGame();
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSong();
            }
        });

        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guess(guessField.getText().toString());
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            if( gameStart ) {
                (new DialogFragment(){
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                .setTitle("Game Ongoing")
                                .setMessage("The game is ongoing.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dismiss();
                                    }
                                });
                        return builder.create();
                    }
                }).show(getFragmentManager(),"");
                return true;
            } else {
                stop();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void shuffle() {
        for(int i = 0; i < playlist.size(); i++) {
            int j = (int)(Math.random() * playlist.size());
            Song temp = playlist.get(i);
            playlist.set(i,playlist.get(j));
            playlist.set(j,temp);
        }
    }

    public void startGame() {
        shuffle();
        currSong = -1;
        score = 0;
        gameStart = true;
        skipButton.setVisibility(View.VISIBLE);
        guessField.setVisibility(View.VISIBLE);
        guessButton.setVisibility(View.VISIBLE);
        nextSong();
    }

    public void nextSong() {
        guessField.setText("");
        if( currSong > -1 ) {
            fifteen.removeCallbacks(songTimer);
            stop();
        } else {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        currSong++;
        if( currSong < playlist.size() ) {
            Song s = playlist.get(currSong);
            Uri contentUri = ContentUris.withAppendedId(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, s.getId());

            try {
                mMediaPlayer.setDataSource(getApplicationContext(), contentUri);
                mMediaPlayer.prepare();
                int start = s.getDuration() > 15000
                                ? (int)(Math.random() * (s.getDuration() - 15000)) : 0;
                fifteen = new Handler();
                fifteen.postDelayed(songTimer = new Runnable() {
                    @Override
                    public void run() {
                        guess(guessField.getText().toString());
                        nextSong();
                    }
                },15000);
                mMediaPlayer.seekTo(start);
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else finishGame();
    }

    public void stop() {
        mMediaPlayer.stop();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void finishGame() {
        fifteen.removeCallbacks(songTimer);
        stop();
        gameStart = false;
        startButton.setText("Start");
        skipButton.setVisibility(View.GONE);
        guessField.setVisibility(View.GONE);
        guessButton.setVisibility(View.GONE);
        (new DialogFragment(){
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle("Game Finished")
                        .setMessage("You got " + score + "/" + playlist.size() + "!")
                        .setPositiveButton("Save Stats", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveGame();
                                dismiss();
                            }
                        }).setNegativeButton("Don't Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dismiss();
                            }
                        });
                return builder.create();
            }
        }).show(getFragmentManager(),"");
    }

    public void saveGame() {

    }

    public void guess(String guess) {
        Song s = playlist.get(currSong);
        switch(type) {
            case SettingsActivity.ARTIST:
                compare = s.getArtist();
                break;
            case SettingsActivity.ALBUM:
                compare = s.getAlbum();
                break;
            case SettingsActivity.TITLE:
                compare = s.getTitle();
                break;
        }
        if( compare.toLowerCase().equals(guess.toLowerCase())) {
            score++;
        } else {
            (new DialogFragment(){
                @Override
                public Dialog onCreateDialog(Bundle savedInstanceState) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                            .setTitle("Wrong")
                            .setMessage("Wrong guess\nThat was " + compare + ".")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dismiss();
                                }
                            });
                    return builder.create();
                }
            }).show(getFragmentManager(),"");
        }
        nextSong();
    }
}
