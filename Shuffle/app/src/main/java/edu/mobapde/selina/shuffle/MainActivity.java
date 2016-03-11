package edu.mobapde.selina.shuffle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView levelLabelMain;
    private Button playButton;
    private Button createButton;
    private Button settingsButton;
    private Button leadButton;
    private DBManager dbm;
    public static final String LEVEL = "LEVEL";
    private int level;
    private SharedPreferences sp;

    public static final String[] LEVELS = new String[] {
            "Guess Artist",
            "Guess Album",
            "Guess Song Title"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        levelLabelMain = (TextView)findViewById(R.id.levelLabelMain);
        playButton = (Button)findViewById(R.id.playButton);
        createButton = (Button)findViewById(R.id.createButton);
        settingsButton = (Button)findViewById(R.id.settingsButton);
        leadButton = (Button)findViewById(R.id.leadButton);
        dbm = new DBManager(getBaseContext());

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent play = new Intent();
                play.setClass(getBaseContext(),SelectPlaylistActivity.class);
                play.putExtra(LEVEL, level);
                startActivity(play);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent create = new Intent();
                create.setClass(getBaseContext(), CreatePlaylistActivity.class);
                startActivity(create);
            }
        });

        /*settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settings = new Intent();
                settings.setClass(getBaseContext(),SettingsActivity.class);
                settings.putExtra(LEVEL,level);
                startActivity(settings);
            }
        });*/
        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        levelLabelMain.setText("Difficulty: " + LEVELS[sp.getInt(LEVEL, SettingsActivity.TITLE)]);
        //settingsButton.setText(LEVELS[getIntent().getExtras().getInt(MainActivity.LEVEL)]);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetLevelDialog sld = new SetLevelDialog();
                sld.show(getFragmentManager(), "");
            }
        });

        leadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lead = new Intent();
                lead.setClass(getBaseContext(),LeaderboardActivity.class);
                startActivity(lead);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //load level
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        level = sp.getInt(LEVEL,SettingsActivity.TITLE);
    }

    public void setLevel(int level) {
        String strLevel = LEVELS[level];
        levelLabelMain.setText("Difficulty: " + strLevel);
        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt(MainActivity.LEVEL, level);
        spe.commit();
    }
}
