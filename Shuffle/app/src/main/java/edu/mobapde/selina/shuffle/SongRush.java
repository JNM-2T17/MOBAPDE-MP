package edu.mobapde.selina.shuffle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class SongRush extends AppCompatActivity {
    private ArrayList<Song> playlist;
    private DBManager dbm;
    private MusicProvider mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_rush);

        Intent list = getIntent();
        int type = list.getExtras().getInt(SelectPlaylistActivity.LIST);
        dbm = new DBManager(getBaseContext());
        mp = new MusicProvider(getContentResolver());
        playlist = new ArrayList<Song>();
        switch(type) {
            case BuildPlaylistActivity.PLAYLIST:
                Playlist p = dbm.getPlayList(list.getExtras()
                                                .getInt(SelectPlaylistActivity.PLAYLIST));
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

        shuffle();

        //start


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

    }

    public void nextSong() {

    }

    public void finishGame() {
        
    }

    public void skip() {

    }

    public void guess(String guess) {

    }
}
