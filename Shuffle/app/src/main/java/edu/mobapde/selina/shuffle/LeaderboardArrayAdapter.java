package edu.mobapde.selina.shuffle;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by XPS 13 on 4/13/2016.
 */
public class LeaderboardArrayAdapter extends RecyclerView.Adapter<LeaderboardArrayAdapter.LeaderboardArrayHolder> {

    private Resources res;
    private int gameMode;
    private Score[] scoreArr;

    public LeaderboardArrayAdapter (Resources res, int gameMode){
        this.res = res;
        this.gameMode = gameMode;
        this.scoreArr = new Score[0];
    }

    public void setArray(Score[] array) {
        this.scoreArr = array;
        for(int i = 0; i < getItemCount(); i++) {
            notifyItemChanged(i);
        }
    }

    @Override
    public LeaderboardArrayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_view,parent,false);
        return new LeaderboardArrayHolder(v);
    }

    @Override
    public void onBindViewHolder(LeaderboardArrayHolder holder, int position) {
        holder.setScore(scoreArr[position]);
        holder.setPosition(position + 1);
    }

    @Override
    public int getItemCount() {
        return scoreArr.length;
    }

    public class LeaderboardArrayHolder extends RecyclerView.ViewHolder {
        private View leaderboardPanel;
        private TextView leaderboardPlace;
        private TextView leaderboardLabel;
        private TextView leaderboardScore;
        private Score score;

        public LeaderboardArrayHolder(View itemView) {
            super(itemView);
            leaderboardPanel = (View) itemView.findViewById(R.id.leaderboardPanel);
            leaderboardPlace = (TextView) itemView.findViewById(R.id.leaderboardPlace);
            leaderboardLabel = (TextView) itemView.findViewById(R.id.leaderboardLabel);
            leaderboardScore = (TextView) itemView.findViewById(R.id.leaderboardScore);
        }

        public void setScore(Score score){
            this.score = score;
            Log.i("game type", "game type" + score.gameType());
            switch(score.gameType()){
                case Score.TYPE_WHOLE:
                    leaderboardLabel.setText("All songs");
                    leaderboardLabel.setTypeface(leaderboardLabel.getTypeface(), Typeface.ITALIC);
                    break;
                case Score.TYPE_ARTIST:
                    leaderboardLabel.setText("Artist" + score.artist());
                    break;
                case Score.TYPE_PLAYLIST:
                    if (score.playlist() == 0){
                        leaderboardLabel.setText("All songs");
                        leaderboardLabel.setTypeface(leaderboardLabel.getTypeface(), Typeface.ITALIC);
                    } else {
                        //Playlist playlist = dbm.getPlayList(score.playlist());
                        //leaderboardLabel.setText(playlist.name());
                        leaderboardLabel.setText("Custom Playlist, Local #" + score.playlist());
                    }
                    break;
                case Score.TYPE_ALBUM:
                    leaderboardLabel.setText("Album " + score.album());
                    break;
            }
            leaderboardScore.setText(score.score() + "");
        }

        public void setPosition(int position) {
            leaderboardPlace.setText("" + position);
        }
    }
}
