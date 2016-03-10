package edu.mobapde.selina.shuffle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class SelectPlaylistActivity extends AppCompatActivity {
    private LinearLayout choicePanel;
    private Button playlistButton;
    private Button albumButton;
    private Button artistButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_playlist);

        choicePanel = (LinearLayout)findViewById(R.id.choicePanel);
        playlistButton = (Button)findViewById(R.id.allButton);
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

    }

    public void startGame(int mode) {
        if( mode == 0 ) {
            Intent timeAttack = new Intent(getBaseContext(),TimeAttack.class);
            startActivity(timeAttack);
        } else {
            Intent songRush = new Intent(getBaseContext(),SongRush.class);
            startActivity(songRush);
        }
    }
}
