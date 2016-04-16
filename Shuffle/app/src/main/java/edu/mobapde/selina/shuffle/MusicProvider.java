package edu.mobapde.selina.shuffle;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
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
    private ArrayList<Bitmap> albumArts = new ArrayList<>();
    final public static Uri sArtworkUri = Uri
            .parse("content://media/external/audio/albumart");

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

    public Song getSong(long id) {
        Cursor c = cr.query(uri, new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
        }, MediaStore.Audio.Media._ID + " = ?", new String[] {
                id + ""
        }, MediaStore.Audio.Media.TITLE);
        if (c.moveToFirst()) {
            return new Song(c.getLong(c.getColumnIndex(MediaStore.Audio.Media._ID)),
                    c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                    c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                    c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                    c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DURATION)));
        }
        return null;
    }

    public ArrayList<Album> getAllAlbums() {
        Cursor c = cr.query(uri,new String[] {
                "DISTINCT " + MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID
            },
            MediaStore.Audio.Media.ALBUM + " is not null ) GROUP BY (" + MediaStore.Audio.Media.ALBUM,
            null,MediaStore.Audio.Media.ALBUM);
        ArrayList<Album> albums = new ArrayList<>();
        if( c.moveToFirst() ) {
            do {
                String album = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                Long albumId = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
                Bitmap albumArt = null;


//                try {
//                    albumArt = MediaStore.Images.Media.getBitmap(cr, albumArtUri);
//                    if (!albumArts.contains(albumArt)){
//
//                        albumArt = Bitmap.createScaledBitmap(albumArt, 128, 128, true);
//                        albumArts.add(albumArt);
//                    }
//                } catch (FileNotFoundException exception) {
//                    //exception.printStackTrace();
//                } catch (IOException e) {
//                    //e.printStackTrace();
//                }

                if( albums.size() == 0 || !album.equals(albums.get(albums.size() - 1))) {
                    albums.add(new Album(album,artist,albumArt));
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

    public ArrayList<Album> getAlbumsOf(String artist) {
        Cursor c = cr.query(uri,new String[] {
                "DISTINCT " + MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID
        },MediaStore.Audio.Media.ARTIST + " = ?",new String[] {
                artist
        },MediaStore.Audio.Media.ALBUM);
        ArrayList<Album> albums = new ArrayList<>();
        if( c.moveToFirst() ) {
            do {
                String album = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                Long albumId = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
                Bitmap albumArt = null;

//                try {
//                    albumArt = MediaStore.Images.Media.getBitmap(cr, albumArtUri);
//                    if (!albumArts.contains(albumArt)){
//                        albumArt = Bitmap.createScaledBitmap(albumArt, 128, 128, true);
//                        albumArts.add(albumArt);
//                    }
//                } catch (FileNotFoundException exception) {
//                    //exception.printStackTrace();
//                } catch (IOException e) {
//                    //e.printStackTrace();
//                }

                if( albums.size() == 0 || !album.equals(albums.get(albums.size() - 1))) {
                    albums.add(new Album(album,artist,albumArt));
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

    public List<Song> getSongsOf(String artist) {
        Cursor c = cr.query(uri,new String[] {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
        },MediaStore.Audio.Media.ARTIST + " = ?",new String[] {
                artist
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
