package com.example.academyproject.controllers;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.Lesson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LessonDetailsViewController implements Initializable {
    @FXML
    private MediaView videoView;
    @FXML
    private Slider volumeSlider;
    private MediaPlayer mediaPlayer;
    private static Lesson selectedLesson;

    public static void setSelectedLesson(Lesson lesson) {
        selectedLesson = lesson;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(selectedLesson.getVideo() != null && !selectedLesson.getVideo().isEmpty()) {
            Media media = new Media(selectedLesson.getVideo());
            this.mediaPlayer = new MediaPlayer(media);
            this.videoView.setMediaPlayer(this.mediaPlayer);
            this.mediaPlayer.play();

            this.volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                this.mediaPlayer.setVolume(newValue.doubleValue());
            });
        }
    }

    @FXML
    private void playVideo(ActionEvent event) {
        if(this.mediaPlayer != null) {
            this.mediaPlayer.play();
        }
    }

    @FXML
    private void pauseVideo(ActionEvent event) {
        if(this.mediaPlayer != null) {
            this.mediaPlayer.pause();
        }
    }

    @FXML
    private void rewindVideo(ActionEvent event) {
        if(this.mediaPlayer != null) {
            this.mediaPlayer.seek(this.mediaPlayer.getCurrentTime().subtract(javafx.util.Duration.seconds(10)));
        }
    }

    @FXML
    private void fastForwardVideo(ActionEvent event) {
        if(this.mediaPlayer != null) {
            this.mediaPlayer.seek(this.mediaPlayer.getCurrentTime().add(javafx.util.Duration.seconds(10)));
        }
    }

    @FXML
    private void handleBackAction(ActionEvent event) throws IOException {
        if(this.mediaPlayer != null) {
            this.mediaPlayer.stop();
        }

        SceneController sceneController = new SceneController();
        sceneController.switchView("views/principal-view.fxml", event);
    }
}
