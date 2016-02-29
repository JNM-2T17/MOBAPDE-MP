package edu.mobapde.selina.shuffle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ryana on 3/1/2016.
 */
public class DBManager extends SQLiteOpenHelper {
    public static final String SCHEMA = "db_shuffle";

    public DBManager(Context context) {
        super(context, SCHEMA, null, 2);
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
                "    " + Playlist.SUB_COLUMN_SONG + " TEXT," +
                "    status INTEGER DEFAULT 1," +
                "    dateAdded DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "    PRIMARY KEY(_playlistId,_song)," +
                "    FOREIGN KEY (_playlistId) REFERENCES sh_playlist(_id)" +
                ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS sh_score (" +
                "\t_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    artist TEXT," +
                "    playlistId INTEGER," +
                "    score INTEGER NOT NULL," +
                "    status INTEGER DEFAULT 1," +
                "    dateAdded DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "    FOREIGN KEY(playlistId) REFERENCES sh_playlist(_id)" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + Playlist.TABLE + ";" +
                        "DROP TABLE IF EXISTS " + Playlist.SUB_TABLE + ";" +
                        "DROP TABLE IF EXISTS sh_score";
        db.execSQL(sql);
        onCreate(db);
    }

    public long addPlaylist(String playlistName,String[] songs) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name",playlistName);
        long id = db.insert("sh_playlist",null,cv);
        cv = new ContentValues();
        cv.put("_playlistId", id);
        for(String s: songs) {
            cv.put("_song",s);
            db.insert("sh_playlist_songs",null,cv);
        }
        return id;
    }

    public Playlist getPlayList(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + Playlist.COLUMN_NAME + ", " + Playlist.SUB_COLUMN_SONG +
                                " FROM " + Playlist.TABLE + " P INNER JOIN " +
                                Playlist.SUB_TABLE + " PS " +
                                "ON P._id = PS." + Playlist.SUB_COLUMN_ID
                                        + " AND P.status = 1 AND PS.status = 1 " +
                                "WHERE " + Playlist.COLUMN_ID + " = ?", new String[]{id + ""});
        String name;
        if( c.moveToFirst() ) {
            name = c.getString(c.getColumnIndex("name"));
            ArrayList<String> songs = new ArrayList<String>();
            do {
                songs.add(c.getString(c.getColumnIndex("_song")));
            }while(c.moveToNext());
            return new Playlist(name,songs.toArray(new String[1]));
        } else {
            return null;
        }
    }
}
