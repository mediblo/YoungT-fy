package com.youngtfy.client.common;

import com.youngtfy.client.controller.LoginController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class ClientApp extends Application {

    public ClientBase cb = new ClientBase();
    Socket socket;

    public static void main(String[] args) {
        launch(args); // JavaFX 애플리케이션 시작
    }

    @Override
    public void start(Stage primaryStage) {
        // 서버와 연결을 위한 별도 스레드
        new Thread(() -> {
            try {
                cb.startSocket();
                socket = cb.getSocket();

                Platform.runLater(() -> {
                    try {
                        LoginController loginController = new LoginController();
                        loginController.setCb(cb);
                        loginController.setStage(primaryStage);
                        loginController.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }  catch (Exception e) {
                // 모든 다른 예외 처리
                System.out.println("예기치 않은 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();

        // 메인 창 닫기 요청 시 애플리케이션 종료
        primaryStage.setOnCloseRequest(event -> {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.exit();  // JavaFX 애플리케이션 종료
            System.exit(0);    // 모든 스레드 종료
        });
    }
}
