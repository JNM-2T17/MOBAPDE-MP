package edu.mobapde.selina.shuffle;

/**
 * Created by ryana on 3/1/2016.
 */
public class Score {
    public static final String TABLE = "sh_score";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ARTIST = "artist";
    public static final String COLUMN_PLAYLIST = "playlistId";
    public static final String COLUMN_SCORE = "score";
    public static final int TYPE_WHOLE = 0;
    public static final int TYPE_ARTIST = 1;
    public static final int TYPE_PLAYLIST = 2;
    private int type;
    private String artist;
    private int playlist;
    private int score;

    public Score(int score) {
        type = TYPE_WHOLE;
        this.score = score;
    }

    public Score(int score, String artist) {
        type = TYPE_ARTIST;
        this.artist = artist;
    }

    public Score(int score, int playlist) {
        type = TYPE_PLAYLIST;
        this.score = score;
    }

    public int type() {
        return type;
    }

    public String artist() {
        return artist;
    }

    public int playlist() {
        return playlist;
    }

    public int score() {
        return score;
    }
}
