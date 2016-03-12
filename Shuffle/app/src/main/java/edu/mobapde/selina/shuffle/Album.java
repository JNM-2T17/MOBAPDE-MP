package edu.mobapde.selina.shuffle;


import android.graphics.Bitmap;

/**
 * Created by XPS 13 on 3/11/2016.
 */
public class Album {

    private String name;
    private String artistName;
    private Bitmap albumArt;

    public Album(String name, String artistName) {
        this.name = name;
        this.artistName = artistName;
        this.albumArt = null;
    }

    public Album(String name, String artistName, Bitmap albumArt) {
        this.name = name;
        this.artistName = artistName;
        this.albumArt = albumArt;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Bitmap getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(Bitmap albumArt) {
        this.albumArt = albumArt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
