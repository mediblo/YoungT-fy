package com.youngtfy.server.DAO;

import java.sql.*;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:mysql://mediblo.hopto.org:3306/youngtfy";  // 데이터베이스 주소
    private static final String DB_USER = "youngtfy_client";  // MySQL 사용자 이름
    private static final String DB_PASSWORD = "0000"; // MySQL 비밀번호

    private Connection connection = null;
    private Statement stmt = null;

    public void getConnection() {
        try {
            // JDBC를 사용하여 MySQL에 연결
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            stmt = connection.createStatement();
            System.out.println("데이터베이스 연결 성공!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("데이터베이스 연결 실패: " + e.getMessage());
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("데이터베이스 연결이 종료되었습니다.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("데이터베이스 연결 종료 실패: " + e.getMessage());
            } finally {
                connection = null;  // 자원을 해제하고 null로 초기화
            }
        }
    }

    public Connection getter_Connection(){
        return this.connection;
    }
    public Statement getter_statement(){
        return this.stmt;
    }
}
