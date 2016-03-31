package edu.mobapde.selina.shuffle;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jonah on 3/11/2016.
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistHolder> {
    private List<Artist> artists;
    private OnClickListener listener;

    public ArtistAdapter(List<Artist> artists) {
        this.artists = artists;
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ArtistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_view,parent,false);
        return new ArtistHolder(v);
    }

    @Override
    public void onBindViewHolder(ArtistHolder holder, int position) {
        holder.setArtist(artists.get(position));
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public class ArtistHolder extends RecyclerView.ViewHolder {
        private Artist s;
        private TextView artistLabel;
        public ArtistHolder(View itemView) {
            super(itemView);
            artistLabel = (TextView)itemView.findViewById(R.id.artistLabel);
            artistLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onArtistClick(s.getName());
                }
            });
        }

        public void setArtist(Artist s) {
            this.s = s;
            artistLabel.setText(s.getName());
            if (s.getName().equalsIgnoreCase("<unknown>")){
                artistLabel.setTypeface(artistLabel.getTypeface(), Typeface.ITALIC);
            }
        }
    }

    public interface OnClickListener {
        public void onArtistClick(String name);
    }
}
