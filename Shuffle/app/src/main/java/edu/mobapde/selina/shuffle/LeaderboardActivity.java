package edu.mobapde.selina.shuffle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        DBManager dbm = new DBManager(getBaseContext());
        List<Score> scores = dbm.getScores();
        for(Score s : scores) {
            Log.i("LeaderboardActivity",s.toString() );
        }
    }
}
