package edu.mobapde.selina.shuffle;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by XPS 13 on 3/30/2016.
 */
public class LeaderboardAdapter extends CursorRecyclerViewAdapter<LeaderboardAdapter.LeaderboardHolder> {

    private int placeNo = 1;

    public LeaderboardAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void onBindViewHolder(LeaderboardHolder viewHolder, Cursor cursor) {
        int gameType = cursor.getInt(cursor.getColumnIndex(Score.COLUMN_TYPE));
        int score = cursor.getInt(cursor.getColumnIndex(Score.COLUMN_SCORE));
        Score s = new Score(score,gameType);
        switch (gameType){
            case BuildPlaylistActivity.ALBUM:
                s.setAlbum(cursor.getString(cursor.getColumnIndex(Score.COLUMN_ALBUM)));
                break;
            case BuildPlaylistActivity.ARTIST:
                s.setArtist(cursor.getString(cursor.getColumnIndex(Score.COLUMN_ARTIST)));
                break;
            case BuildPlaylistActivity.PLAYLIST:
                s.setPlaylist(cursor.getInt(cursor.getColumnIndex(Score.COLUMN_PLAYLIST)));
                break;
            case BuildPlaylistActivity.SONG:
                break;
        }

        viewHolder.setScore(s);


    }

    @Override
    public LeaderboardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_view,parent, false);
        return new LeaderboardHolder(v);
    }

    public class LeaderboardHolder extends RecyclerView.ViewHolder {
        private View leaderboardPanel;
        private TextView leaderboardPlace;
        private TextView leaderboardLabel;
        private TextView leaderboardScore;
        private Score score;

        public LeaderboardHolder(View itemView) {
            super(itemView);
            leaderboardPanel = (View) itemView.findViewById(R.id.leaderboardPanel);
            leaderboardPlace = (TextView) itemView.findViewById(R.id.leaderboardPlace);
            leaderboardLabel = (TextView) itemView.findViewById(R.id.leaderboardLabel);
            leaderboardScore = (TextView) itemView.findViewById(R.id.leaderboardScore);
        }

        public void setScore(Score score){
            this.score = score;
            leaderboardPlace.setText(placeNo + "");
            placeNo++;
            switch(score.gameType()){
                case Score.TYPE_WHOLE:
                    leaderboardLabel.setText("All songs");
                    leaderboardLabel.setTypeface(leaderboardLabel.getTypeface(), Typeface.ITALIC);
                    break;
                case Score.TYPE_ARTIST:
                    leaderboardLabel.setText(score.artist());
                    break;
                case Score.TYPE_PLAYLIST:
                    if (score.playlist() == 0){
                        leaderboardLabel.setText("All songs");
                        leaderboardLabel.setTypeface(leaderboardLabel.getTypeface(), Typeface.ITALIC);
                    } else {
                        leaderboardLabel.setText("Playlist #" + score.playlist());
                    }
                    break;
                case Score.TYPE_ALBUM:
                    leaderboardLabel.setText(score.album());
                    break;
            }
            leaderboardScore.setText(score.score() + "");
        }
    }

}
