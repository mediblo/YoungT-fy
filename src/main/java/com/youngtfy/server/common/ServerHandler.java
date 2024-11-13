package com.youngtfy.server.common;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.youngtfy.common.DataObject;
import com.youngtfy.server.DAO.DatabaseConnection;
import com.youngtfy.server.DAO.ServerDAO;


public class ServerHandler implements Runnable {
    private final Socket clientSocket;
    private final Lock lock = new ReentrantLock();
    private Connection conn;
    private final ServerDAO serverDAO;


    public ServerHandler(Socket clientSocket, DatabaseConnection dbc) {
        this.clientSocket = clientSocket;
        this.conn = dbc.getter_Connection();
        this.serverDAO = new ServerDAO(conn);
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            DataObject data;
            while((data = (DataObject) in.readObject()) != null) {
                int command = data.getCommand();

                // switch 구문처럼 입력에 따라 각 함수 호출
                System.out.println("Command Code : " + command);
                switch (command) {
                    case 1 -> handleLogin(data, out); // 로그인
                    case 2 -> handleChkRegister(data, out); // 회원가입 확인
                    case 3 -> handleRegister(data); // 회원가입
                    case 4 -> handleMusicUrl(data, out); // 미리듣기 url 전송
                    case 5 -> handleSearch(data, out); // 음악 검색 및 변경
                    default -> System.out.println(-1);
                }
            }
        } catch( EOFException | SocketException e) {
            System.out.println("클라이언트가 연결을 종료했습니다: " + e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }




    // -1 로그인 X, 0  로그인 O
    private void handleLogin(DataObject data, PrintWriter out) {
        String[] user = data.getStrings();
        String checkQuery = "SELECT COUNT(*) FROM users WHERE email = ? and password = ?";
        int chk = -1;
        try{
            PreparedStatement pstmt = conn.prepareStatement(checkQuery);
            pstmt.setString(1, user[0]);
            pstmt.setString(2, String.valueOf(user[1].hashCode()));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0){
                chk = 0;
            }
            pstmt.close();
            rs.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        out.println(chk);
        out.flush();
    }

    // 회원가입 확인
    private void handleChkRegister(DataObject data, PrintWriter out) {
        int chk = 0;

        lock.lock();
        try {
            String email = data.getStrings()[0];
            String checkQuery = "SELECT COUNT(*) FROM users WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(checkQuery);
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                chk = 3;
            }

            String username = data.getStrings()[1];
            checkQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
            pstmt = conn.prepareStatement(checkQuery);
            pstmt.setString(1, username);

            rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                chk = 4;
            }

            pstmt.close();
            rs.close();
        } catch (SQLException e){
            System.out.println("SQL 예외 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        out.println(chk);
        out.flush();
    }

    // 예제 함수 3
    private void handleRegister(DataObject data) {
        lock.lock();
        try {
            String[] user = data.getStrings();
            String query = "INSERT INTO users (email, password, username) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, user[0]);
            pstmt.setString(2, String.valueOf(user[1].hashCode()));
            pstmt.setString(3, user[2]);
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // 예제 함수 4
    private synchronized void handleMusicUrl(DataObject data, PrintWriter out) {
        String[] track_last = serverDAO.get_trackLast(data.getStrings()[0]); // id, previewUrl
        System.out.println("done0");

        ServerAPI serverAPI = new ServerAPI();
        String track_name = serverAPI.searchTrackId(track_last[0]);
        out.println(track_name == null ? "None" : track_name);
        out.flush();
        out.println(track_last[1] == null ? "-1" : track_last[1]);
        out.flush();
    }

    // 예제 함수 5
    private synchronized void handleSearch(DataObject data, PrintWriter out) {
        ServerAPI serverAPI = new ServerAPI();
        String[] temp = serverAPI.searchTrackName(data.getStrings()[0]);
        System.out.println("Preview URL : " + temp[1]);
        out.println(temp[0]);
        out.flush();
        out.println(temp[1]);
        out.flush();
        out.println(temp[2]);
        out.flush();

        serverDAO.update_lastTrack(data.getStrings()[1], temp[3], temp[1]);
    }

    private void handleGetLastTrack(DataObject data, PrintWriter out){

    }
}
