package edu.mobapde.selina.shuffle;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    public static final int ARTIST = 0;
    public static final int ALBUM = 1;
    public static final int TITLE = 2;
    private Button backButton;
    private Button setLevel;
    private TextView levelLabel;

    public static final String[] LEVELS = new String[] {
            "Guess Artist",
            "Guess Album",
            "Guess Song Title"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        backButton = (Button)findViewById(R.id.backButton);
        setLevel = (Button)findViewById(R.id.setLevel);
        levelLabel = (TextView)findViewById(R.id.levelLabel);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetLevelDialog sld = new SetLevelDialog();
                sld.show(getFragmentManager(), "");
            }
        });

        levelLabel.setText(LEVELS[getIntent().getExtras().getInt(MainActivity.LEVEL)]);
    }

    public void setLevel(int level) {
        String strLevel = LEVELS[level];
        Log.i("SettingsActivity", "setting level to " + level + ": " + strLevel);
        levelLabel.setText(strLevel);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt(MainActivity.LEVEL, level);
        spe.commit();
    }
}
