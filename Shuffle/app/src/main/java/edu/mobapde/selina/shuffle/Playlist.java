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
    private String name;
    private ArrayList<String> songs;

    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<String>();
    }

    public Playlist(String name, String[] songs) {
        this.name = name;
        Log.i("Playlist",name);
        this.songs = new ArrayList<String>();
        for(String song: songs) {
            Log.i("Playlist",song);
            this.songs.add(song);
        }
    }

    public String name() {
        return name;
    }

    public String song(int index) {
        return songs.get(index);
    }
}