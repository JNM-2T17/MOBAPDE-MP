package edu.mobapde.selina.shuffle;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

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

    public List<Song> getAllSongs() {
        Cursor c = cr.query(uri,new String[] {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
        },null,null, MediaStore.Audio.Media.TITLE);
        ArrayList<Song> songs = new ArrayList<Song>();
        if( c.moveToFirst()) {
            do {
                songs.add(new Song(c.getLong(c.getColumnIndex(MediaStore.Audio.Media._ID)),
                        c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                        c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                        c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                        c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DURATION))));
                Song s = songs.get(songs.size() - 1);
            } while(c.moveToNext());
        }
        c.close();
        return songs;
    }

    public List<String> getAllAlbums() {
        Cursor c = cr.query(uri,new String[] {
                MediaStore.Audio.Media.ALBUM
        },null,null,MediaStore.Audio.Media.ALBUM);
        ArrayList<String> albums = new ArrayList<String>();
        if( c.moveToFirst() ) {
            do {
                String album = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                if( albums.size() == 0 || !album.equals(albums.get(albums.size() - 1))) {
                    albums.add(album);
                }
            } while( c.moveToNext());
        }
        return albums;
    }

    public List<String> getAllArtists() {
        Cursor c = cr.query(uri,new String[] {
                MediaStore.Audio.Media.ARTIST
        },null,null,MediaStore.Audio.Media.ARTIST);
        ArrayList<String> artists = new ArrayList<String>();
        if( c.moveToFirst() ) {
            do {
                String artist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                if( artists.size() == 0 || !artist.equals(artists.get(artists.size() - 1))) {
                    artists.add(artist);
                }
            } while( c.moveToNext());
        }
        return artists;
    }

    public List<String> getAlbumsOf(String artist) {
        Cursor c = cr.query(uri,new String[] {
                MediaStore.Audio.Media.ALBUM
        },MediaStore.Audio.Media.ARTIST + " = ?",new String[] {
                artist
        },MediaStore.Audio.Media.ALBUM);
        ArrayList<String> albums = new ArrayList<String>();
        if( c.moveToFirst() ) {
            do {
                String album = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                if( albums.size() == 0 || !album.equals(albums.get(albums.size() - 1))) {
                    albums.add(album);
                }
            } while( c.moveToNext());
        }
        return albums;
    }

    public List<Song> getSongsIn(String album) {
        Cursor c = cr.query(uri,new String[] {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
        },MediaStore.Audio.Media.ALBUM + " = ?",new String[] {
                album
        }, MediaStore.Audio.Media.TITLE);
        ArrayList<Song> songs = new ArrayList<Song>();
        if( c.moveToFirst()) {
            do {
                songs.add(new Song(c.getLong(c.getColumnIndex(MediaStore.Audio.Media._ID)),
                        c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                        c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                        c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                        c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DURATION))));
            } while(c.moveToNext());
        }
        return songs;
    }

}
