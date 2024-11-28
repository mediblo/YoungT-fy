package com.youngtfy.client.controller;

import com.youngtfy.client.common.ClientBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.BufferedReader;
import java.io.ObjectOutputStream;

public class SearchController {

    ClientBase cb = null;
    // 서버 데이터 수신
    BufferedReader in = null;
    // 서버 데이터 전송
    ObjectOutputStream out = null;
    MainViewController mainViewController;

    private Stage stage;

    @FXML
    private Button homeBtn;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setCb(ClientBase cb){
        this.cb = cb;
        System.out.println(3);
        System.out.println(this.cb);
        this.in = cb.getIn();
        this.out = cb.getOut();
    }

    public void setMainViewController(MainViewController mainViewController) { this.mainViewController = mainViewController; }

    @FXML
    public void initialize() {
        homeBtn.setOnAction(event -> handleBack());
    }

    private void handleBack() {
        try {
            // FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
            Parent root = loader.load();

            // MainviewController의 인스턴스를 가져와 stage 설정
            MainViewController mainViewController = this.mainViewController;
            mainViewController.setStage((Stage) homeBtn.getScene().getWindow());
            mainViewController.setCb(this.cb);
            mainViewController.setGUI();
            mainViewController.setCb(this.cb);

            // 기존 씬을 새로운 뷰로 교체

            // 기존 Scene을 유지
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


        } catch (Exception e) {
            e.printStackTrace(); // 오류 발생 시 스택 트레이스를 출력
        }
    }
}
