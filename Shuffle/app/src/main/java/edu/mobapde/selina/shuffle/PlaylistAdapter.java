package edu.mobapde.selina.shuffle;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ryana on 3/8/2016.
 */
public class PlaylistAdapter extends CursorRecyclerViewAdapter<PlaylistAdapter.PlaylistHolder> {
    private CreatePlaylistActivity.OnClickListener listener;

    public PlaylistAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    public void setListener(CreatePlaylistActivity.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(PlaylistHolder viewHolder, Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Playlist.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(Playlist.COLUMN_NAME));
        viewHolder.setPlaylist(new Playlist(id,name));
    }

    @Override
    public PlaylistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_view,null);

        return new PlaylistHolder(v);
    }

    public class PlaylistHolder extends RecyclerView.ViewHolder {
        private View playlistPanel;
        private TextView playlistLabel;
        private Playlist p;

        public PlaylistHolder(View itemView) {
            super(itemView);
            playlistPanel = (View)itemView.findViewById(R.id.playlistPanel);
            playlistLabel = (TextView)itemView.findViewById(R.id.playlistLabel);
            playlistPanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(p);
                }
            });
        }

        public void setPlaylist(Playlist p) {
            this.p = p;
            playlistLabel.setText(p.name());
        }
    }
}
