package com.youngtfy.client.controller;

import com.youngtfy.client.common.ClientBase;

import com.youngtfy.common.DataObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MainViewController {
    ClientBase cb = null;
    // 서버 데이터 수신
    BufferedReader in = null;
    // 서버 데이터 전송
    ObjectOutputStream out = null;
    private Stage stage;
    Parent root = null;
    String musicUrl = "X";

    @FXML
    private Button playBtn;
    @FXML
    private Button nextBtn;
    @FXML
    private Button stopBtn;
    @FXML
    private Slider musicSlider;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Button searchBtn;
    @FXML
    private TextField searchInput;
    @FXML
    private ImageView album_now;
    @FXML
    private Label trackNameLabel;

    LoginController loginController;
    boolean chkLast = true;

    String email;

    Media media = null;
    MediaPlayer mediaPlayer;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public MainViewController() {}

    public void setCb(ClientBase cb){
        this.cb = cb;
        System.out.println(1);
        System.out.println(this.cb);
        this.in = cb.getIn();
        this.out = cb.getOut();
    }

    public void setGUI(String email){
        this.email = email;
        boolean chkLast = test();
        initializePlayer(chkLast);
    }

    public void setGUI(){
        chkLast = test();
        initializePlayer(chkLast);
    }

    @FXML
    public void initialize() {
        searchBtn.setOnAction(e -> handleSearch());
        stopBtn.setVisible(false);
    }

    public void initializePlayer(boolean chkLast) {
        playBtn.setOnAction(event -> {
            System.out.println(2);
            if (mediaPlayer == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("노래가 엄서요!");
                alert.showAndWait();
            }
            else{
                mediaPlayer.play();
                playBtn.setVisible(false);
                stopBtn.setVisible(true);
            }});
        stopBtn.setOnAction(event -> {
            mediaPlayer.pause();
            playBtn.setVisible(true);
            stopBtn.setVisible(false);
        });

        if(chkLast){
            volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(newValue.doubleValue());
                }
            });

            return;
        }


        mediaPlayer.setVolume(volumeSlider.getValue());

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mediaPlayer.setVolume(newValue.doubleValue());
        });

        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!musicSlider.isValueChanging()) { // 사용자가 드래그 중이 아닐 때만 업데이트
                double progress = newTime.toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
                musicSlider.setValue(progress);
            }
        });

        // 진행 바를 이동하면 재생 시간을 조정
        musicSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (musicSlider.isValueChanging()) { // 사용자가 드래그 중일 때만 동작
                double seekTime = newVal.doubleValue() * mediaPlayer.getTotalDuration().toSeconds();
                mediaPlayer.seek(Duration.seconds(seekTime));
            }
        });
    }

    @FXML
    private void handleSearch() {
        try {
            // FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("search-view.fxml"));
            Parent root = loader.load();

            // SearchController의 인스턴스를 가져와 stage 설정
            SearchController searchController = loader.getController();
            searchController.setStage((Stage) searchBtn.getScene().getWindow());

            searchController.setCb(cb);
            System.out.println(2);
            System.out.println(this.cb);
            searchController.setMainViewController(this);

            // 기존 씬을 새로운 뷰로 교체
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            initializePlayer(chkLast);

        } catch (Exception e) {
            e.printStackTrace(); // 오류 발생 시 스택 트레이스를 출력
        }
    }

    /*
    private void handleSearch(){
        URL resourceUrl = getClass().getResource("image/album_now.jpg");
        double currentValue = volumeSlider.getValue();

        String[] input_data = {searchInput.getText(), email};
        DataObject data = new DataObject(5, input_data);

        try {
            out.writeObject(data);
            out.flush();
        }
        catch (IOException e){
            System.out.println("IO 예외 발생1: " + e.getMessage());
            e.printStackTrace();
        }
        String imageUrl = null;
        URL url = null;

        try {
            imageUrl = in.readLine();
            musicUrl = in.readLine(); // 서버로부터 문자열을 수신
            trackNameLabel.setText(in.readLine());

            System.out.println("change musicUrl : " + musicUrl);
        } catch (IOException e){
            e.printStackTrace();
        }
        try {
            URI uri = new URI(imageUrl);
            url = uri.toURL();
        } catch (URISyntaxException | MalformedURLException e){
            e.printStackTrace();
        }

        // 저장할 경로 지정 시발 왜 최상위폴더에서 시작되냐 개새끼야

        Path destinationPath = null;
        try{
            destinationPath = Paths.get(resourceUrl.toURI());
        } catch (URISyntaxException e){
            e.printStackTrace();
        }

        try {
            Files.createDirectories(destinationPath.getParent());
        } catch (IOException e) {
            e.printStackTrace();
            return; // 디렉토리 생성 실패 시 종료
        }

        // 파일이 존재하는 경우 삭제
        try {
            if (Files.exists(destinationPath)) {
                Files.delete(destinationPath);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        // InputStream을 통해 URL에서 데이터 읽기
        try (InputStream in = url.openStream()) {
            Files.copy(in, destinationPath);
        } catch(IOException e){
            e.printStackTrace();
        }

        if(stopBtn.isVisible()) {
            mediaPlayer.pause();
            playBtn.setVisible(true);
            stopBtn.setVisible(false);
        }

        media = new Media(musicUrl);
        mediaPlayer = new MediaPlayer(media);

        Image newImage = new Image(resourceUrl.toExternalForm());
        album_now.setImage(newImage);
        musicSlider.setValue(0);

        mediaPlayer.setVolume(currentValue);

        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!musicSlider.isValueChanging()) { // 사용자가 드래그 중이 아닐 때만 업데이트
                double progress = newTime.toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
                musicSlider.setValue(progress);
            }
        });
    }
     */

    private void updateSliderTrackColor(int volume) {
        double percentage = volume; // 0.0 ~ 1.0으로 변환
        String trackColor = String.format("-fx-background-color: linear-gradient(to right, #3D3D3D %s%%, #2A2A2A %s%%);",
                percentage * 100, percentage * 100);
        musicSlider.lookup(".track").setStyle(trackColor);
    }

    private boolean test(){
        String[] input_data = {email};
        DataObject data = new DataObject(4, input_data);
        try {
            out.writeObject(data);
            out.flush();
        }
        catch (IOException e){
            System.out.println("IO 예외 발생: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            trackNameLabel.setText(in.readLine());
            musicUrl = in.readLine(); // 서버로부터 문자열을 수신
            if (musicUrl.equals("-1")){
                return true;
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        media = new Media(musicUrl);
        mediaPlayer = new MediaPlayer(media);
        return false;
    }

    public void setLoginController(LoginController loginController){ this.loginController = loginController; }

}
