package edu.mobapde.selina.shuffle;

/**
 * Created by ryana on 3/10/2016.
 */
public class Song {
    private long id;
    private String title;
    private String artist;
    private String album;
    private long duration;

    public Song(String title, long id) {
        this.title = title;
        this.id = id;
    }

    public Song(long id, String title, String artist, String album, long duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
