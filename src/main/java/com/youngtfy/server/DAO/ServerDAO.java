package com.youngtfy.server.DAO;

import com.youngtfy.server.model.User;

import java.net.Socket;
import java.sql.*;
import java.util.Vector;
import java.util.List;

public class ServerDAO {

    private final Connection conn;

    public ServerDAO( Connection conn) {
        this.conn = conn;
    }

    public void update_lastTrack(String email, String id, String previewUrl){
        try {
            String query = "SELECT COUNT(*) FROM last_track WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                query = "UPDATE last_track SET trackId = ?, trackPreviewUrl = ? WHERE email = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, id);
                pstmt.setString(2, previewUrl);
                pstmt.setString(3, email);
                pstmt.executeUpdate();
            }
            else { insrt_lastTrack(email, id); }

            pstmt.close();
            rs.close();
        } catch (SQLException e){
            e.printStackTrace();
    }

    }

    private void insrt_lastTrack(String  email, String id){
        try {
            String query = "INSERT INTO last_track (email, trackId) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
            pstmt.close();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public String[] get_trackLast(String email){
        String id = null;
        String previewUrl = null;
        String query = "SELECT * FROM last_track WHERE email = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                id = rs.getString(2);
                previewUrl = rs.getString(3);
            }

            pstmt.close();
            rs.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        String[] output = {id, previewUrl};
        return output;
    }
}
