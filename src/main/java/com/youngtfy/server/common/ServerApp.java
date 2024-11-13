package com.youngtfy.server.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.youngtfy.server.DAO.*;

public class ServerApp {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final int serverPort;

    public ServerApp(int port){
        this.serverPort = port;
    }

    public void start(){

        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            DatabaseConnection dbConnection = new DatabaseConnection();
            dbConnection.getConnection();

            System.out.println("서버가 " + serverSocket.getLocalPort() + " 포트에서 대기중...");

            // 서버 종료 감지를 위한 스레드 생성
            Thread exitThread = new Thread(() -> {
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
                try {
                    String command;
                    while ((command = consoleReader.readLine()) != null) {
                        if ("exit".equalsIgnoreCase(command)) {
                            System.out.println("서버 종료 명령어가 입력되었습니다.");
                            dbConnection.closeConnection();
                            System.exit(0);  // 서버 종료
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            exitThread.start();

            // 클라이언트 연결을 계속 대기
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("클라이언트 연결됨: " + clientSocket.getInetAddress());

                ServerHandler serverHandler = new ServerHandler(clientSocket, dbConnection);
                executor.submit(serverHandler); // 클라이언트를 비동기적으로 처리

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        ServerApp server = new ServerApp(65500);
        server.start();
    }
}