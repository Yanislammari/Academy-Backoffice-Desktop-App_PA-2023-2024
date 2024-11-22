package com.example.academyproject.controllers;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.Lesson;
import com.example.academyproject.services.LessonService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SetLessonFormViewController implements Initializable {
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Button selectVideoButton;
    @FXML
    private Label videoFilePathLabel;
    private File selectedVideoFile;
    private static Lesson selectedLesson;

    public static void setSelectedLesson(Lesson lesson) {
        selectedLesson = lesson;
    }

    @FXML
    private void handleSelectVideoFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        this.selectedVideoFile = fileChooser.showOpenDialog(this.selectVideoButton.getScene().getWindow());
        if(this.selectedVideoFile != null) {
            this.videoFilePathLabel.setText("Selected file: " + this.selectedVideoFile.getAbsolutePath());
        }
        else {
            this.videoFilePathLabel.setText("No file selected");
        }
    }

    @FXML
    private void handleSaveButton(ActionEvent event) throws IOException {
        if(selectedLesson == null) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "No lesson selected!");
            return;
        }

        String title = this.titleTextField.getText();
        String description = this.descriptionTextArea.getText();
        if(title.isEmpty() || description.isEmpty()) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled in!");
            return;
        }

        LessonService.updateLesson(selectedLesson.getIdCourse(), selectedLesson.getId(), title, description, this.selectedVideoFile);
        SceneController sceneController = new SceneController();
        sceneController.switchView("views/principal-view.fxml", event);
    }

    @FXML
    private void handleCancelButton(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchView("views/principal-view.fxml", event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(selectedLesson != null) {
            this.titleTextField.setText(selectedLesson.getTitle());
            this.descriptionTextArea.setText(selectedLesson.getDescription());
        }
    }
}
