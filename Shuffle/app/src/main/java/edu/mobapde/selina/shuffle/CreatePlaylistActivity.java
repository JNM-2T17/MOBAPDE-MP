package edu.mobapde.selina.shuffle;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class CreatePlaylistActivity extends AppCompatActivity {
    private DBManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);
        dbm = new DBManager(getBaseContext());
//        long id = dbm.addPlaylist("Custom1",new String[] {
//                "Victorious",
//                "Don't Threaten Me With A Good Time",
//                "Hallelujah",
//                "Emperor's New Clothes"
//        });
        dbm.getPlayList(1);
    }
}
