package edu.mobapde.selina.shuffle;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

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
        List<Score> scores = dbm.getScores();
        for(Score s : scores) {
            Log.i("LeaderboardActivity",s.toString() );
        }

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
