package edu.mobapde.selina.shuffle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CreatePlaylistActivity extends AppCompatActivity {
    public static final String ID_KEY = "id";
    public static final String NAME_KEY = "name";
    private DBManager dbm;

    private Button addButton;
    private RecyclerView playlistView;
    private PlaylistAdapter pa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);
        addButton = (Button)findViewById(R.id.addButton);
        playlistView = (RecyclerView)findViewById(R.id.playlistView);

        dbm = new DBManager(getBaseContext());
        pa = new PlaylistAdapter(getBaseContext(),null);
        pa.setListener(new OnClickListener() {
            @Override
            public void onClick(Playlist p) {
                Intent editPlaylist = new Intent(getBaseContext(),BuildPlaylistActivity.class);
                editPlaylist.putExtra(ID_KEY,p.id());
                editPlaylist.putExtra(NAME_KEY,p.name());
                Log.i("CreatePlaylistActivity","Putting " + p.id() + " and " + p.name());
                startActivity(editPlaylist);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent buildPlaylist = new Intent(getBaseContext(),BuildPlaylistActivity.class);
                startActivity(buildPlaylist);
            }
        });

        playlistView.setAdapter(pa);
        playlistView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        pa.swapCursor(dbm.getPlaylists());
    }

    public interface OnClickListener {
        public void onClick(Playlist p);
    }
}
