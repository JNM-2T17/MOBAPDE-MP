package edu.mobapde.selina.shuffle;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by ryana on 3/8/2016.
 */
public class MusicProvider {
    private ContentResolver cr;
    private Uri uri;

    public MusicProvider(ContentResolver cr) {
        this.cr = cr;
        uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    public Cursor getAllSongs() {
//        long id;
//        String title;
//        String artist;
//        String album;
//        long duration;
        Cursor c = cr.query(uri,new String[] {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
        },null,null, MediaStore.Audio.Media.TITLE);
        return c;
    }
}
