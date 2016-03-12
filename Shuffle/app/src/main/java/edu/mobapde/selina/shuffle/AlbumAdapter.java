package edu.mobapde.selina.shuffle;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by XPS 13 on 3/11/2016.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumHolder> {
    private Resources res;
    private List<Album> albums;
    private OnClickListener listener;

    public AlbumAdapter(Resources res, List<Album> albums) {
        this.res = res;
        this.albums = albums;
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public AlbumHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_view,parent,false);

        return new AlbumHolder(v);
    }

    @Override
    public void onBindViewHolder(AlbumHolder holder, int position) {
        holder.setAlbum(albums.get(position));
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class AlbumHolder extends RecyclerView.ViewHolder {
        private Album album;
        private ImageView albumArt;
        private TextView albumLabel;
        private TextView artistLabel;
        public AlbumHolder(View itemView) {
            super(itemView);
            albumArt = (ImageView)itemView.findViewById(R.id.albumArt);
            albumLabel = (TextView)itemView.findViewById(R.id.albumLabel);
            artistLabel = (TextView) itemView.findViewById(R.id.artistLabel);
            albumLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAlbumClick(album.getName());
                }
            });
        }

        public void setAlbum(Album album) {
            this.album = album;
            albumLabel.setText(album.getName());
            artistLabel.setText(album.getArtistName());
            if (album.getAlbumArt() != null){
                albumArt.setImageBitmap(album.getAlbumArt());
            } else {
                Drawable defaultAlbumArt;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    defaultAlbumArt = res.getDrawable(R.drawable.default_album_art, null);
                } else {
                    defaultAlbumArt = res.getDrawable(R.drawable.default_album_art);
                }
                albumArt.setImageBitmap(drawableToBitmap(defaultAlbumArt));
            }
        }
    }

    public interface OnClickListener {
        public void onAlbumClick(String album);
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


}
