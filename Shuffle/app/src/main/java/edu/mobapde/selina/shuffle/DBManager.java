package edu.mobapde.selina.shuffle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by ryana on 3/1/2016.
 */
public class DBManager extends SQLiteOpenHelper {
    public static final String SCHEMA = "db_shuffle";

    public DBManager(Context context) {
        super(context, SCHEMA, null, 4);
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
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Score.TABLE + " (" +
                "    " + Score.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    " + Score.COLUMN_ARTIST + " TEXT," +
                "    " + Score.COLUMN_PLAYLIST + " INTEGER," +
                "    " + Score.COLUMN_ALBUM + " TEXT NOT NULL," +
                "    " + Score.COLUMN_TYPE + " INTEGER NOT NULL," +
                "    " + Score.COLUMN_SCORE + " INTEGER NOT NULL," +
                "    status INTEGER DEFAULT 1," +
                "    dateAdded DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "    FOREIGN KEY(" + Score.COLUMN_PLAYLIST + ") " +
                "       REFERENCES sh_playlist(" + Playlist.COLUMN_ID + ")" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + Playlist.TABLE + ";";
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + Playlist.SUB_TABLE + ";";
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + Score.TABLE;
        db.execSQL(sql);
        onCreate(db);
    }

    public long addPlaylist(String playlistName,String[] songs) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", playlistName);
        long id = db.insert(Playlist.TABLE,null,cv);
        cv = new ContentValues();
        cv.put(Playlist.SUB_COLUMN_ID, id);
        for(String s: songs) {
            cv.put(Playlist.SUB_COLUMN_SONG,s);
            db.insert(Playlist.SUB_TABLE, null, cv);
        }
        return id;
    }

    public Playlist getPlayList(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + Playlist.COLUMN_ID + ", " + Playlist.COLUMN_NAME + ", "
                                + Playlist.SUB_COLUMN_SONG + " FROM " + Playlist.TABLE
                                + " P INNER JOIN " + Playlist.SUB_TABLE + " PS " + "ON P._id = PS."
                                + Playlist.SUB_COLUMN_ID + " AND P.status = 1 AND PS.status = 1 " +
                                "WHERE " + Playlist.COLUMN_ID + " = ?", new String[]{id + ""});
        String name;
        if( c.moveToFirst() ) {
            id = c.getInt(c.getColumnIndex(Playlist.COLUMN_ID));
            name = c.getString(c.getColumnIndex(Playlist.COLUMN_NAME));
            ArrayList<String> songs = new ArrayList<String>();
            do {
                songs.add(c.getString(c.getColumnIndex(Playlist.SUB_COLUMN_SONG)));
            }while(c.moveToNext());
            return new Playlist((int)id,name,songs.toArray(new String[0]));
        } else {
            return null;
        }
    }

    public Cursor getPlaylists() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(Playlist.TABLE,new String[]{
                Playlist.COLUMN_ID,
                Playlist.COLUMN_NAME
        },"status = ?",new String[]{"1"},null,null,Playlist.COLUMN_NAME);
        return c;
    }
}
