package edu.mobapde.selina.shuffle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ScoreManager {
	public static Score[] getScores(String mode) {
		DBManager dbm = DBManager.getInstance();
		
		Connection c = dbm.getConnection();
		
		String query = "SELECT artist, album, type, score, mode"
						+ " FROM shf_score"
					    + " WHERE status = 1 AND mode = ?"
						+ " ORDER BY score DESC"
					    + " LIMIT 100";
		PreparedStatement ps;
		try {
			ps = c.prepareStatement(query);
			ps.setString(1, mode);
			
			ResultSet rs = ps.executeQuery();
			
			ArrayList<Score> scores = new ArrayList<Score>();
			
			while(rs.next()) {
				int type = rs.getInt("type");
				Score s = new Score(rs.getInt("score"),type);
				if( type == Score.TYPE_ARTIST ) {
					s.setArtist(rs.getString("artist"));
				} else if( type == Score.TYPE_ALBUM ) {
					s.setAlbum(rs.getString("album"));
				}
				scores.add(s);
			}
			
			return scores.toArray(new Score[0]);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return new Score[0];
	}
	
	public static boolean addScores(Score[] s) {
		DBManager dbm = DBManager.getInstance();
		
		Connection c = dbm.getConnection();
		
		String query = "INSERT INTO shf_score(artist, album, type, score, mode)"
						+ " VALUES ";
		for(int i = 0; i < s.length; i++ ) {
			if( i > 0 ) {
				query += ",";
			}
			query += "(?,?,?,?,?)";
		}
		PreparedStatement ps;
		try {
			ps = c.prepareStatement(query);
			for(int i = 0; i < s.length; i++ ) {
				ps.setString(i * 5 + 1, s[i].artist());
				ps.setString(i * 5 + 2, s[i].album());
				ps.setInt(i * 5 + 3, s[i].gameType());
				ps.setInt(i * 5 + 4, s[i].score());
				ps.setInt(i * 5 + 5, s[i].mode());
			}
			ps.execute();
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
