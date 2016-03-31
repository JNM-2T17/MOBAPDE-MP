package edu.mobapde.selina.shuffle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ryana on 3/1/2016.
 */
public class DBManager extends SQLiteOpenHelper {
    public static final String SCHEMA = "db_shuffle";

    public DBManager(Context context) {
        super(context, SCHEMA, null, 7);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Playlist.TABLE + " (" +
                "    " + Playlist.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    " + Playlist.COLUMN_NAME + " TEXT NOT NULL," +
                "    status INTEGER DEFAULT 1," +
                "    dateAdded DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Playlist.SUB_TABLE + " (" +
                "    " + Playlist.SUB_COLUMN_ID + " INTEGER," +
                "    " + Playlist.SUB_COLUMN_SONG + " INTEGER," +
                "    status INTEGER DEFAULT 1," +
                "    dateAdded DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "    PRIMARY KEY(" + Playlist.SUB_COLUMN_ID + "," + Playlist.SUB_COLUMN_SONG
                + ",dateAdded)," +
                "    FOREIGN KEY (" + Playlist.SUB_COLUMN_ID + ") REFERENCES sh_playlist("
                + Playlist.COLUMN_ID + ")" +
                ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Score.TABLE + " (" +
                "    " + Score.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    " + Score.COLUMN_ARTIST + " TEXT," +
                "    " + Score.COLUMN_PLAYLIST + " INTEGER," +
                "    " + Score.COLUMN_ALBUM + " TEXT," +
                "    " + Score.COLUMN_TYPE + " INTEGER NOT NULL," +
                "    " + Score.COLUMN_SCORE + " INTEGER NOT NULL," +
                "    " + Score.COLUMN_MODE + " INTEGER NOT NULL," +
                "    status INTEGER DEFAULT 1," +
                "    dateAdded DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "    FOREIGN KEY(" + Score.COLUMN_PLAYLIST + ") " +
                "       REFERENCES sh_playlist(" + Playlist.COLUMN_ID + ")" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ArrayList<Playlist> playlists = new ArrayList<Playlist>();
        Cursor c = getPlaylistsSub(db);
        if( c.moveToFirst()) {
            do {
                playlists.add(getPlaylistSub(db,c.getLong(c.getColumnIndex(Playlist.COLUMN_ID))));
            } while(c.moveToNext());
        }

        String sql = "DROP TABLE IF EXISTS " + Playlist.TABLE + ";";
        db.execSQL(sql);

        sql = "DROP TABLE IF EXISTS " + Playlist.SUB_TABLE + ";";
        db.execSQL(sql);

        ArrayList<Score> scores = getScores(db);
        sql = "DROP TABLE IF EXISTS " + Score.TABLE;
        db.execSQL(sql);

        onCreate(db);
        for(Playlist p: playlists) {
            long[] songs = new long[p.size()];
            for( int i = 0; i < p.size(); i++) {
                songs[i] = p.song(i);
            }
            addPlaylistSub(db,p.name(),songs);
        }
        for( Score s : scores ) {
            addScore(db,s.mode(),s.getListType(),s.value(),s.score());
        }
    }

    public void addScore(int mode,int type,String value, int score) {
        SQLiteDatabase db = getWritableDatabase();

        addScore(db, mode, type, value, score);
        Log.i("Database","Yay");
    }

    public void addScore(SQLiteDatabase db,int mode, int type, String value, int score) {
        ContentValues cv = new ContentValues();
        cv.put(Score.COLUMN_SCORE, score);
        cv.put(Score.COLUMN_TYPE, type);
        cv.put(Score.COLUMN_MODE, mode);
        switch(type) {
            case BuildPlaylistActivity.PLAYLIST:
                cv.put(Score.COLUMN_PLAYLIST,Integer.parseInt(value));
                break;
            case BuildPlaylistActivity.ALBUM:
                cv.put(Score.COLUMN_ALBUM,value);
                break;
            case BuildPlaylistActivity.ARTIST:
                cv.put(Score.COLUMN_ARTIST,value);
                break;
            default:
        }

        db.insert(Score.TABLE, null, cv);
        Log.i("Database", "Yay");
    }

    public ArrayList<Score> getScores() {
        SQLiteDatabase db = getReadableDatabase();

        return getScores(db);
    }

    public ArrayList<Score> getScores(SQLiteDatabase db) {
        Cursor c = db.query(Score.TABLE, null, null, null, null, null, Score.COLUMN_SCORE + " DESC");
        ArrayList<Score> scores = new ArrayList<Score>();
        if( c.moveToFirst()) {
            do {
                Score s = new Score(c.getInt(c.getColumnIndex(Score.COLUMN_SCORE))
                        ,c.getInt(c.getColumnIndex(Score.COLUMN_TYPE)));
                boolean isArtist =  !c.isNull(c.getColumnIndex(Score.COLUMN_ARTIST));
                boolean isAlbum =  !c.isNull(c.getColumnIndex(Score.COLUMN_ALBUM));
                boolean isPlaylist =  !c.isNull(c.getColumnIndex(Score.COLUMN_PLAYLIST));
                if( isArtist ) {
                    s.setArtist(c.getString(c.getColumnIndex(Score.COLUMN_ARTIST)));
                } else if( isAlbum ) {
                    s.setAlbum(c.getString(c.getColumnIndex(Score.COLUMN_ALBUM)));
                } else if( isPlaylist ){
                    s.setPlaylist(c.getInt(c.getColumnIndex(Score.COLUMN_PLAYLIST)));
                }
                scores.add(s);
            }while(c.moveToNext());
        }
        return scores;
    }

    public ArrayList<Score> getScores(int gameType) {
        SQLiteDatabase db = getReadableDatabase();

        return getScores(db,gameType);
    }

    public ArrayList<Score> getScores(SQLiteDatabase db,int gameType) {
        Cursor c = db.query(Score.TABLE, null, Score.COLUMN_TYPE + " = ?", new String[] {
                "" + gameType
        }, null, null, Score.COLUMN_SCORE + " DESC");
        ArrayList<Score> scores = new ArrayList<Score>();
        if( c.moveToFirst()) {
            do {
                Score s = new Score(c.getInt(c.getColumnIndex(Score.COLUMN_SCORE))
                        ,c.getInt(c.getColumnIndex(Score.COLUMN_TYPE)));
                boolean isArtist =  !c.isNull(c.getColumnIndex(Score.COLUMN_ARTIST));
                boolean isAlbum =  !c.isNull(c.getColumnIndex(Score.COLUMN_ALBUM));
                boolean isPlaylist =  !c.isNull(c.getColumnIndex(Score.COLUMN_PLAYLIST));
                if( isArtist ) {
                    s.setArtist(c.getString(c.getColumnIndex(Score.COLUMN_ARTIST)));
                } else if( isAlbum ) {
                    s.setAlbum(c.getString(c.getColumnIndex(Score.COLUMN_ALBUM)));
                } else if( isPlaylist ){
                    s.setPlaylist(c.getInt(c.getColumnIndex(Score.COLUMN_PLAYLIST)));
                }
                scores.add(s);
            }while(c.moveToNext());
        }
        return scores;
    }

    public Cursor getScoresAsCursor(int gameMode) {
        SQLiteDatabase db = getReadableDatabase();

        Log.i("You suck","Data retrieved");
        return getScoresAsCursor(db,gameMode);
    }

    public Cursor getScoresAsCursor(SQLiteDatabase db,int gameMode) {
        Cursor c = db.query(Score.TABLE, null, Score.COLUMN_MODE + " = ?", new String[] {
                "" + gameMode
        }, null, null, Score.COLUMN_SCORE + " DESC");
        return c;
//        ArrayList<Score> scores = new ArrayList<Score>();
//        if( c.moveToFirst()) {
//            do {
//                Score s = new Score(c.getInt(c.getColumnIndex(Score.COLUMN_SCORE))
//                        ,c.getInt(c.getColumnIndex(Score.COLUMN_TYPE)));
//                boolean isArtist =  !c.isNull(c.getColumnIndex(Score.COLUMN_ARTIST));
//                boolean isAlbum =  !c.isNull(c.getColumnIndex(Score.COLUMN_ALBUM));
//                boolean isPlaylist =  !c.isNull(c.getColumnIndex(Score.COLUMN_PLAYLIST));
//                if( isArtist ) {
//                    s.setArtist(c.getString(c.getColumnIndex(Score.COLUMN_ARTIST)));
//                } else if( isAlbum ) {
//                    s.setAlbum(c.getString(c.getColumnIndex(Score.COLUMN_ALBUM)));
//                } else if( isPlaylist ){
//                    s.setPlaylist(c.getInt(c.getColumnIndex(Score.COLUMN_PLAYLIST)));
//                }
//                s.setMode(c.getInt(c.getColumnIndex(Score.COLUMN_MODE)));
//                scores.add(s);
//            }while(c.moveToNext());
//        }
//        return scores;

    }

    public long addPlaylist(String playlistName,long[] songs) {
        SQLiteDatabase db = getWritableDatabase();
        return addPlaylistSub(db,playlistName,songs);
    }

    private long addPlaylistSub(SQLiteDatabase db, String playlistName, long[] songs) {
        ContentValues cv = new ContentValues();
        cv.put("name", playlistName);
        long id = db.insert(Playlist.TABLE,null,cv);
        cv = new ContentValues();
        cv.put(Playlist.SUB_COLUMN_ID, id);
        for(long s: songs) {
            cv.put(Playlist.SUB_COLUMN_SONG,s);
            db.insert(Playlist.SUB_TABLE, null, cv);
        }
        return id;
    }

    public Playlist getPlayList(long id) {
        SQLiteDatabase db = getReadableDatabase();
        return getPlaylistSub(db,id);
    }

    public Playlist getPlaylistSub(SQLiteDatabase db, long id) {
        Log.i("DBManager","SELECT " + Playlist.COLUMN_ID + ", " + Playlist.COLUMN_NAME + ", "
                + Playlist.SUB_COLUMN_SONG + " FROM " + Playlist.TABLE
                + " P INNER JOIN " + Playlist.SUB_TABLE + " PS ON P." + Playlist.COLUMN_ID
                + " = PS." + Playlist.SUB_COLUMN_ID + " AND P.status = 1 AND PS.status = 1 WHERE "
                + Playlist.COLUMN_ID + " = " + id);
        Cursor c = db.rawQuery("SELECT " + Playlist.COLUMN_ID + ", " + Playlist.COLUMN_NAME + ", "
                + Playlist.SUB_COLUMN_SONG + " FROM " + Playlist.TABLE
                + " P INNER JOIN " + Playlist.SUB_TABLE + " PS ON P." + Playlist.COLUMN_ID
                + " = PS." + Playlist.SUB_COLUMN_ID + " AND P.status = 1 AND PS.status = 1 WHERE "
                + Playlist.COLUMN_ID + " = ?", new String[]{id + ""});
        String name;
        if( c.moveToFirst() ) {
            id = c.getInt(c.getColumnIndex(Playlist.COLUMN_ID));
            name = c.getString(c.getColumnIndex(Playlist.COLUMN_NAME));
            ArrayList<Long> songs = new ArrayList<Long>();
            do {
                songs.add(c.getLong(c.getColumnIndex(Playlist.SUB_COLUMN_SONG)));
            }while(c.moveToNext());
            return new Playlist((int)id,name,songs.toArray(new Long[0]));
        } else {
            return null;
        }
    }

    public void updatePlaylist(long id, String name, long[] songs) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Playlist.COLUMN_NAME,name);
        db.update(Playlist.TABLE, cv, Playlist.COLUMN_ID + " = ?", new String[]{
                id + ""
        });

        cv = new ContentValues();
        cv.put("status",0);
        db.update(Playlist.SUB_TABLE,cv,Playlist.SUB_COLUMN_ID + " = ?",new String[] {
                id + ""
        });

        cv = new ContentValues();
        cv.put(Playlist.SUB_COLUMN_ID, id);
        for(long s: songs) {
            cv.put(Playlist.SUB_COLUMN_SONG,s);
            db.insert(Playlist.SUB_TABLE, null, cv);
        }
    }

    public Cursor getPlaylists() {
        SQLiteDatabase db = getReadableDatabase();

        return getPlaylistsSub(db);
    }

    public Cursor getPlaylistsSub(SQLiteDatabase db) {
        Cursor c = db.query(Playlist.TABLE,new String[]{
                Playlist.COLUMN_ID,
                Playlist.COLUMN_NAME
        },"status = ?",new String[]{"1"},null,null,Playlist.COLUMN_NAME);
        return c;
    }
}
