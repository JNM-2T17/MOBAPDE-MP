package edu.mobapde.selina.shuffle;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by XPS 13 on 3/11/2016.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumHolder> {
    private List<Album> albums;
    private OnClickListener listener;

    public AlbumAdapter(List<Album> albums) {
        this.albums = albums;
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public AlbumHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_view,null);
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
        private TextView albumLabel;
        public AlbumHolder(View itemView) {
            super(itemView);
            albumLabel = (TextView)itemView.findViewById(R.id.albumLabel);
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
        }
    }

    public interface OnClickListener {
        public void onAlbumClick(String album);
    }
}
