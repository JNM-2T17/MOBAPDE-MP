package edu.mobapde.selina.shuffle;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ryana on 3/1/2016.
 */
public class Playlist {
    public static final String TABLE = "sh_playlist";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String SUB_TABLE = "sh_playlist_songs";
    public static final String SUB_COLUMN_ID = "_playlistId";
    public static final String SUB_COLUMN_SONG = "_song";
    private int id;
    private String name;
    private ArrayList<Long> songs;

    public Playlist(int id, String name) {
        this.id = id;
        this.name = name;
        this.songs = new ArrayList<Long>();
    }

    public Playlist(int id, String name, Long[] songs) {
        this.id = id;
        this.name = name;
        Log.i("Playlist",name);
        this.songs = new ArrayList<Long>();
        for(Long song: songs) {
            Log.i("Playlist",song + "");
            this.songs.add(song);
        }
    }

    public long id() { return id; }

    public String name() {
        return name;
    }

    public Long song(int index) {
        return songs.get(index);
    }

    public int size() { return songs.size(); }
}
