package edu.mobapde.selina.shuffle;

/**
 * Created by ryana on 3/1/2016.
 */
public class Score {
    public static final String TABLE = "sh_score";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ARTIST = "artist";
    public static final String COLUMN_PLAYLIST = "playlistId";
    public static final String COLUMN_ALBUM = "album";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_SCORE = "score";
    public static final int TYPE_WHOLE = 0;
    public static final int TYPE_ARTIST = 1;
    public static final int TYPE_PLAYLIST = 2;
    public static final int TYPE_ALBUM = 3;
    public static final int SONG_RUSH = 4;
    public static final int TIME_ATTACK = 5;
    private int gameType;
    private String artist;
    private int playlist;
    private String album;
    private int score;

    public Score(int score,int gameType) {
        this.score = score;
        this.gameType = gameType;
    }

    public Score(int score, String artist,boolean isArtist,int gameType) {
        if( isArtist ) {
            this.artist = artist;
        } else {
            this.album = artist;
        }
        this.gameType = gameType;
    }

    public Score(int score, int playlist,int gameType) {
        this.score = score;
        this.gameType = gameType;
    }

    public int gameType() { return gameType; }

    public String artist() {
        return artist;
    }

    public int playlist() {
        return playlist;
    }

    public String album() { return album; }

    public int score() {
        return score;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setPlaylist(int playlist) {
        this.playlist = playlist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getListType() {
        if( artist != null ) {
            return BuildPlaylistActivity.ARTIST;
        } else if( album != null ) {
            return BuildPlaylistActivity.ALBUM;
        } else if( playlist != 0 ){
            return BuildPlaylistActivity.PLAYLIST;
        } else {
            return BuildPlaylistActivity.SONG;
        }
    }

    public String value() {
        if( artist != null ) {
            return artist;
        } else if( album != null ) {
            return album;
        } else if(playlist != 0){
            return "" + playlist;
        } else return "";
    }

    public String toString() {
        return gameType + " " + getListType() + " " + value() + " " + score;
    }
}
