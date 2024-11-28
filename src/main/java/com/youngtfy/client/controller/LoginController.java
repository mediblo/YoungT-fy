package com.youngtfy.client.controller;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.Interpolator;

import com.youngtfy.common.DataObject;
import com.youngtfy.client.common.ClientBase;

import java.io.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Circle rotatingCircle;
    @FXML
    private TextField idField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button submitBtn;
    @FXML
    private Button registerBtn;
    private Stage stage;

    ClientBase cb = null;
    // 서버 데이터 수신
    BufferedReader in = null;
    // 서버 데이터 전송
    ObjectOutputStream out = null;

    Parent root = null;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        registerBtn.setOnAction(event -> handleRegister());
        submitBtn.setOnAction(event -> handleLogin());



        initializeAnimation();
    }

    public void setCb(ClientBase cb){
        this.cb = cb;
        this.in = cb.getIn();
        this.out = cb.getOut();
    }

    public LoginController() {}

    public void show() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        root = loader.load();

        LoginController controller = loader.getController();
        controller.setCb(cb);
        controller.setStage(stage);

        Scene scene = new Scene(root);
        stage.setTitle("YoungT-fy");
        stage.setScene(scene);
        stage.show();
    }

    private void handleLogin() {
        String id = idField.getText();
        String password = passwordField.getText();
        String[] s_data = {id, password};
        DataObject data = new DataObject(1, s_data);
        int chk = -1;

        // 로그인 서비스 호출
        boolean success = true;
        try {
            out.writeObject(data);
            out.flush();
        }
        catch (IOException e){
            System.out.println("IO 예외 발생: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            String received = in.readLine(); // 서버로부터 문자열을 수신

            chk = Integer.parseInt(received); // 문자열을 정수로 변환
        } catch (IOException e){
            e.printStackTrace();
        }

        if (chk == 0) {
            System.out.println("로그인 성공!");
            try {
                // FXML 파일 로드
                FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
                Parent root = loader.load();

                // MainViewController의 인스턴스를 가져와 stage 설정
                MainViewController mainViewController = loader.getController();
                mainViewController.setStage((Stage) submitBtn.getScene().getWindow());
                mainViewController.setCb(cb);
                mainViewController.setGUI(id);
                mainViewController.setLoginController(this);

                // 기존 씬을 새로운 뷰로 교체
                Scene scene = new Scene(root);
                Stage stage = (Stage) submitBtn.getScene().getWindow();
                stage.setScene(scene);
                stage.show();

            } catch (Exception e) {
                e.printStackTrace(); // 오류 발생 시 스택 트레이스를 출력
            }
        } else {
            System.out.println("로그인 실패! 올바른 정보를 입력하세요.");
        }
    }

    @FXML
    private void handleRegister() {
        try {
            // FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register-view.fxml"));
            Parent root = loader.load();

            // RegisterController의 인스턴스를 가져와 stage 설정
            RegisterController registerController = loader.getController();
            registerController.setStage((Stage) registerBtn.getScene().getWindow());
            registerController.setCb(cb);
            registerController.setLoginController(this);

            // 기존 씬을 새로운 뷰로 교체
            Scene scene = new Scene(root);
            Stage stage = (Stage) registerBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace(); // 오류 발생 시 스택 트레이스를 출력
        }
    }

    private void initializeAnimation() { // 빙글빙글 큰 원
        if (rotatingCircle != null) {
            RotateTransition rotate = new RotateTransition(Duration.seconds(20), rotatingCircle);
            rotate.setByAngle(360);
            rotate.setCycleCount(RotateTransition.INDEFINITE);
            rotate.setInterpolator(Interpolator.LINEAR);
            rotate.play();
        } else {
            System.out.println("rotatingCircle is null. Please check FXML loading.");
        }
    }
}
