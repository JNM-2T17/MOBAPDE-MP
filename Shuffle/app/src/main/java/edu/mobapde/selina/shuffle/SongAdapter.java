package edu.mobapde.selina.shuffle;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryana on 3/10/2016.
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {
    private List<Song> songs;
    private OnClickListener listener;
    private ArrayList<Long> selected;

    public SongAdapter(List<Song> songs) {
        this.songs = songs;
        selected = new ArrayList<Long>();
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setSelected(ArrayList<Long> selected) {
        this.selected = selected;
        Log.i("SongAdapter","Updating");
        for(Long l : selected) {
            Log.i("SongAdapter","Update with " + l);
        }
        update();
    }

    @Override
    public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_song,null);
        return new SongHolder(v);
    }

    @Override
    public void onBindViewHolder(SongHolder holder, int position) {
        holder.setSong(songs.get(position));
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void update() {
        for(int i = 0; i < getItemCount(); i++) {
            notifyItemChanged(i);
        }
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        private Song s;
        private CheckBox songCheckBox;
        public SongHolder(View itemView) {
            super(itemView);
            songCheckBox = (CheckBox)itemView.findViewById(R.id.songCheckBox);
            songCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSongClick(s.getId(),songCheckBox.isChecked());
                }
            });
        }

        public void setSong(Song s) {
            this.s = s;
            songCheckBox.setText(s.getTitle());
            Log.i("SongAdapter","Finding " + s.getId() + " " + s.getTitle());
            boolean found = false;
            for( Long l: selected) {
                if( l.longValue() == s.getId() ) {
                    found = true;
                    break;
                }
            }
            songCheckBox.setChecked(found);
        }
    }

    public interface OnClickListener {
        public void onSongClick(long id, boolean checked);
    }
}
