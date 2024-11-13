package com.youngtfy.client.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientBase {
    private final String serverAddress = "mediblo.hopto.org"; // Server Host
    private int port = 65500; // 포트 번호
    Socket socket = null;

    // 서버 데이터 수신
    BufferedReader in = null;
    // 서버 데이터 전송
    ObjectOutputStream out = null;

    public String getServerAddress(){ return this.serverAddress; }
    public int getPort() { return this.port; }
    public void startSocket() {
        try {
            this.socket = new Socket(serverAddress, port);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("서버에 연결되었습니다.");
        } catch (IOException e){
            // IO 예외 처리
            System.out.println("IO 예외 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public Socket getSocket() { return this.socket; }

    public BufferedReader getIn() { return this.in; }
    public ObjectOutputStream getOut() { return this.out; }

}
