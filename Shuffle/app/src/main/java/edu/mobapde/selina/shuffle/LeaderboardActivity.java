package edu.mobapde.selina.shuffle;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LeaderboardActivity extends AppCompatActivity implements LeaderboardFragment.OnFragmentInteractionListener {

    private LinearLayout choicePanel;
    private Button songRushButton;
    private Button timeAttackButton;
    private Button globalRushButton;
    private Button globalAttackButton;

    private LeaderboardFragment songRushFragment;
    private LeaderboardFragment timeAttackFragment;
    private LeaderboardArrayFragment globalRushFragment;
    private LeaderboardArrayFragment globalAttackFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        DBManager dbm = new DBManager(getBaseContext());

        choicePanel = (LinearLayout) findViewById(R.id.choicePanel);
        songRushButton = (Button) findViewById(R.id.songRushButton);
        timeAttackButton = (Button) findViewById(R.id.timeAttackButton);
        globalRushButton = (Button) findViewById(R.id.globalRushButton);
        globalAttackButton = (Button) findViewById(R.id.globalAttackButton);

        songRushFragment = LeaderboardFragment.newInstance(Score.SONG_RUSH);
        timeAttackFragment = LeaderboardFragment.newInstance(Score.TIME_ATTACK);
        globalRushFragment = LeaderboardArrayFragment.newInstance(Score.SONG_RUSH);
        globalAttackFragment = LeaderboardArrayFragment.newInstance(Score.TIME_ATTACK);
        getSupportFragmentManager().beginTransaction().add(R.id.leaderboardFragment,songRushFragment)
                .commit();

        songRushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.leaderboardFragment, songRushFragment)
                        .commit();
                swapColors(songRushFragment);
            }
        });

        timeAttackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.leaderboardFragment, timeAttackFragment)
                        .commit();
                swapColors(timeAttackFragment);
            }
        });

        globalRushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.leaderboardFragment, globalRushFragment)
                        .commit();
                swapColors(globalRushFragment);
            }
        });

        globalAttackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.leaderboardFragment, globalAttackFragment)
                        .commit();
                swapColors(globalAttackFragment);
            }
        });

        (new ScoreUploader()).execute();
    }

    class ScoreUploader extends AsyncTask<Void,Void,Boolean>{
        private DBManager dbm;

        public ScoreUploader() {
            dbm = new DBManager(getBaseContext());
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            ArrayList<Score> scores = dbm.getScoresForUpload();
            if( scores.size() == 0 ) {
                return false;
            }
            OkHttpClient ohc = new OkHttpClient();
            RequestBody rb = new FormBody.Builder()
                    .add("scores", (new Gson()).toJson(scores))
                    .build();

            Request request = new Request.Builder()
                    .url("http://192.168.1.35:8080/ShuffleServer/Put")
                    .post(rb)
                    .build();
            try {
                Response response = ohc.newCall(request).execute();
                return new Boolean(response.body().string());
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if( aBoolean ) {
                dbm.setAllUploaded();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void swapColors(Fragment fragment) {
        if( fragment.equals(songRushFragment)/*fragment instanceof ArtistFragment*/ ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                songRushButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2, null));
                timeAttackButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1, null));
                globalRushButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1, null));
                globalAttackButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1, null));
            } else {
                songRushButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2));
                timeAttackButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
                globalRushButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
                globalAttackButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
            }
        } else if( fragment.equals(timeAttackFragment)/*fragment instanceof AlbumFragment*/ ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                songRushButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1, null));
                timeAttackButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2,null));
                globalRushButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1, null));
                globalAttackButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1, null));
            } else {
                songRushButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
                timeAttackButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2));
                globalRushButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
                globalAttackButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
            }
        } else if( fragment.equals(globalRushFragment)/*fragment instanceof AlbumFragment*/ ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                songRushButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1, null));
                timeAttackButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1,null));
                globalRushButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2, null));
                globalAttackButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1, null));
            } else {
                songRushButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
                timeAttackButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
                globalRushButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2));
                globalAttackButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
            }
        } else if( fragment.equals(globalAttackFragment)/*fragment instanceof AlbumFragment*/ ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                songRushButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1, null));
                timeAttackButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1,null));
                globalRushButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1, null));
                globalAttackButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2, null));
            } else {
                songRushButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
                timeAttackButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
                globalRushButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_1));
                globalAttackButton.setBackground(getResources().getDrawable(R.drawable.background_gradient_2));
            }
        }
    }
}
