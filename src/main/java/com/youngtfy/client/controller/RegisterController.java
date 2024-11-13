package com.youngtfy.client.controller;

import com.youngtfy.common.DataObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.youngtfy.client.common.ClientBase;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private Button registerSubmitBtn;
    @FXML
    private Button backBtn;
    private Stage stage;

    ClientBase cb = null;
    BufferedReader in = null;
    // 서버 데이터 전송
    ObjectOutputStream out = null;

    LoginController loginController;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCb(ClientBase cb){
        this.cb = cb;
        this.in = cb.getIn();
        this.out = cb.getOut();
    }

    public void setLoginController(LoginController loginController){ this.loginController = loginController; }

    @FXML
    public void initialize() {
        registerSubmitBtn.setOnAction(event -> handleRegister());
        backBtn.setOnAction(event -> handleBack());
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        int chk = chkRegister(email, username, password, confirmPassword);

        System.out.println(chk);
        if (chk != 0) handleNoRegAlert(chk); // 실패 시 알럿
        else {
            String[] user = {email, password, username};
            DataObject data = new DataObject(3, user);
            try {
                out.writeObject(data);
                out.flush();
            } catch (IOException e) {
                System.out.println("IO 예외 발생: " + e.getMessage());
                e.printStackTrace();
            }
            handleYesRegAlert();
        }
    }

    private void handleYesRegAlert(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("회원가입 성공");
        alert.setHeaderText("회원가입에 성공했습니다.");
        alert.setContentText("로그인 화면으로 돌아갑니다.");
        alert.showAndWait();
        handleBack();
    }

    // 회원가입 안되는 경우
    private void handleNoRegAlert(int chk) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("회원가입 실패");
        if (chk == 5){
            alert.setHeaderText("입력칸 공백");
            alert.setContentText("모두 작성해주세요.");
        } else if (chk == 1) {
            alert.setHeaderText("이메일 형태가 아님");
            alert.setContentText("이메일 형태로 작성해주세요.");
        } else if (chk == 2){
            alert.setHeaderText("비밀번호가 다름");
            alert.setContentText("비밀번호가 맞는지 확인해주세요.");
        } else if (chk == 3){
            alert.setHeaderText("사용중인 이메일");
            alert.setContentText("사용중인 이메일입니다.");
        } else if (chk == 4){
            alert.setHeaderText("사용중인 닉네임");
            alert.setContentText("사용중인 닉네임입니다.");
        }
        alert.showAndWait();
    }

    /**
     * 0 = 정상, 1 = 이메일 형태가 아님, 2 = 비밀번호 다름, 3 = 이메일 중복, 4 = 닉네임 중복
     * 5 = 입력 칸 엠티
     * @param email [ 이메일 ]
     * @param username [ 닉네임 ]
     * @param password [ 비번 ]
     * @param confirmPassword [ 비번확인 ]
     * @return 스테이터스 코드 형식
     */
    private int chkRegister(String email, String username, String password, String confirmPassword){
        int chk = 0;

        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()
                || confirmPasswordField.getText().isEmpty() || usernameField.getText().isEmpty()
        ) return 5; // 입력칸 엠티
        else if (!isValidEmail(email)) return 1; // 이메일 형태 확인
        else if (!password.equals(confirmPassword)) return 2; // 비밀번호 확인


        String[] strings = {email, username};
        DataObject data = new DataObject(2, strings);
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

            int receive = Integer.parseInt(received); // 문자열을 정수로 변환
            if (receive != 0) return receive; // 이메일, 닉네임 중복 확인
        } catch (IOException e){
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean isValidEmail(String email) { // 이메일 확인 정규표현식 [regex]
        final String email_regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(email_regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches(); // 이메일이 정규 표현식과 일치하는지 확인
    }

    private void handleBack() {
        try {
            // FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent root = loader.load();

            // RegisterController의 인스턴스를 가져와 stage 설정
            LoginController loginController = loader.getController();
            loginController.setStage((Stage) backBtn.getScene().getWindow());
            loginController.setCb(this.loginController.cb);

            // 기존 씬을 새로운 뷰로 교체
            Scene scene = new Scene(root);
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace(); // 오류 발생 시 스택 트레이스를 출력
        }
    }
}
