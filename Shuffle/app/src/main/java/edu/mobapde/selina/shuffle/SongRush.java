package edu.mobapde.selina.shuffle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentUris;
import android.content.Context;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongRush extends AppCompatActivity {
    public static final int SPEECH = 1;
    private List<Song> playlist;
    private DBManager dbm;
    private MusicProvider mp;

    private int currSong;
    private Handler fifteen;
    private Runnable songTimer;
    private boolean gameStart;
    private int score;
    private int listType;
    private String list;
    private int type;
    private String value;
    private long pId;
    private String compare;

    private TextView gameLabel;
    private TextView scoreLabel;
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
        gameLabel = (TextView)findViewById(R.id.gameLabel);
        listType = list.getExtras().getInt(SelectPlaylistActivity.LIST);
        dbm = new DBManager(getBaseContext());
        mp = new MusicProvider(getContentResolver());
        switch(listType) {
            case BuildPlaylistActivity.SONG:
                playlist = mp.getAllSongs();
                gameLabel.setText("All Songs");
                this.list = "All Songs";
                break;
            case BuildPlaylistActivity.PLAYLIST:
                pId = list.getExtras().getLong(SelectPlaylistActivity.PLAYLIST);
                this.list = pId + "";
                Playlist p = dbm.getPlayList(pId);
                gameLabel.setText(p.name());
                playlist = new ArrayList<Song>();
                for(int i = 0; i < p.size(); i++) {
                    playlist.add(mp.getSong(p.song(i)));
                }
                break;
            case BuildPlaylistActivity.ALBUM:
                value = list.getExtras().getString(SelectPlaylistActivity.VAL);
                this.list = value;
                gameLabel.setText(value);
                playlist = mp.getSongsIn(value);
                break;
            case BuildPlaylistActivity.ARTIST:
                value = list.getExtras().getString(SelectPlaylistActivity.VAL);
                this.list = value;
                gameLabel.setText(value);
                playlist = mp.getSongsOf(value);
                break;
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        type = sp.getInt(MainActivity.LEVEL,SettingsActivity.TITLE);

        startButton = (Button)findViewById(R.id.startButton);
        skipButton = (Button)findViewById(R.id.skipButton);
        guessField = (EditText)findViewById(R.id.guessField);
        guessButton = (Button)findViewById(R.id.guessButton);
        scoreLabel = (TextView)findViewById(R.id.score);
        scoreLabel.setVisibility(View.GONE);
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
                    guessField.requestFocus();
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
                guess();
                guessField.requestFocus();
            }
        });

        guessField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    //if the enter key was pressed, then hide the keyboard and do whatever needs doing.
                    guessField.requestFocus();
                    guess();
                    InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(guessField.getApplicationWindowToken(), 0);
                }
                return false;
            }
        });

        guessField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (guessField.getText().toString().isEmpty()){
                    guessButton.setText("Skip");
                } else {
                    guessButton.setText("Guess");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        scoreLabel.setVisibility(View.VISIBLE);
        scoreLabel.setText("0");
        nextSong();
    }

//    public void startListen() {
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Try speaking");
//        try {
//            startActivityForResult(intent, SPEECH );
//        } catch(ActivityNotFoundException anfe) {
//            Toast.makeText(getApplicationContext(),
//                    "Speech not supported",
//                    Toast.LENGTH_SHORT).show();
//        }
//
//        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
//        sr.startListening(intent);
//        Log.i("111111", "11111111");
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch(requestCode) {
//            case SPEECH:
//                if( resultCode == RESULT_OK && data != null ) {
//                    ArrayList<String> result = data
//                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    guessField.setText(result.get(0));
//                }
//                break;
//            default:
//        }
//    }

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
                        guess();
                        nextSong();
                    }
                },15000);
                mMediaPlayer.seekTo(start);
                mMediaPlayer.start();
//                startListen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else finishGame();
    }

    public void stop() {
        if( mMediaPlayer != null ) {
            try {
                mMediaPlayer.stop();
            } catch( IllegalStateException ise) {}
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
    }

    public void finishGame() {
        fifteen.removeCallbacks(songTimer);
        stop();
        mMediaPlayer.release();
        gameStart = false;
        startButton.setText("Start");
        skipButton.setVisibility(View.GONE);
        guessField.setVisibility(View.GONE);
        guessButton.setVisibility(View.GONE);
        scoreLabel.setVisibility(View.GONE);
        (new DialogFragment(){
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder;
                if (score > 0){
                    builder = new AlertDialog.Builder(getActivity())
                            .setTitle("Game Finished")
                            .setMessage("You got " + score + " / " + playlist.size() + "!")
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
                } else {
                    builder = new AlertDialog.Builder(getActivity())
                            .setTitle("Game Finished")
                            .setMessage("Better luck next time.")
                            .setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dismiss();
                                }
                            });
                    return builder.create();
                }
            }
        }).show(getFragmentManager(), "");
    }

    public void saveGame() {
        dbm.addScore(GameModeDialog.SONG_RUSH,listType,list,score);
        Toast.makeText(getBaseContext(),"Score Saved!",Toast.LENGTH_LONG).show();
    }

    public boolean lcsWord(String str1,String str2) {
        int[][] lcs = new int[str1.length() + 1][str2.length() + 1];

        int i = 0;
        int j = 0;
        for( i = 0; i < lcs.length - 1; i++) {
            for( j = 0; j < lcs[0].length - 1; j++ ) {
                if( str1.charAt(i) == str2.charAt(j) ) {
                    lcs[i + 1][j + 1] = lcs[i][j] + 1;
                } else {
                    lcs[i + 1][j + 1] = Math.max(lcs[i][j + 1],lcs[i + 1][j]);
                }
            }
        }

        String longest;
        int k = i;
        int l = j;

        return lcs[i][j] * 2 >= Math.max(str1.length(), str2.length());
    }

    public boolean lcsGuess(String compare, String guess) {
        String[] parts1 = compare.split(" ");
        String[] parts2 = guess.split(" ");
        int[][] lcs = new int[parts1.length + 1][parts2.length + 1];

        int i = 0;
        int j = 0;
        for( i = 0; i < lcs.length - 1; i++) {
            for( j = 0; j < lcs[0].length - 1; j++ ) {
                if( lcsWord(parts1[i],parts2[j]) ) {
                    lcs[i + 1][j + 1] = lcs[i][j] + 1;
                } else {
                    lcs[i + 1][j + 1] = Math.max(lcs[i][j + 1],lcs[i + 1][j]);
                }
            }
        }

        String longest;
        int k = i;
        int l = j;

//        String str = "";
//        while( k != 0 && l != 0) {
//            if( lcs[k][l] == lcs[k][l - 1]) {
//                l--;
//            } else if( lcs[k][l] == lcs[k - 1][l]) {
//                k--;
//            } else {
//                str = parts1[k - 1] + " " + str;
//                l--;
//                k--;
//            }
//        }

        return lcs[i][j] >= Math.min(3,parts1.length);
    }

    public void guess() {
        String guess = guessField.getText().toString();
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
        if( lcsGuess(compare.toLowerCase(), guess.toLowerCase())) {
            score++;
            scoreLabel.setText(score + "");
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
