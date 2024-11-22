package com.example.academyproject.controllers;

import com.example.academyproject.SceneController;
import com.example.academyproject.services.CourseService;
import com.example.academyproject.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class AddCourseFormViewController {
    @FXML
    private TextField title;
    @FXML
    private TextField description;
    @FXML
    private ComboBox<String> level;
    @FXML
    private Button chooseImageButton;
    @FXML
    private ImageView courseImageView;
    private String choiceLevel;

    @FXML
    private void handleChoosePhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(this.chooseImageButton.getScene().getWindow());

        if(file != null) {
            try {
                Image image = new Image(file.toURI().toString());
                this.courseImageView.setImage(image);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleEdit(ActionEvent event) throws IOException {
        if(this.title.getText().isEmpty() || this.description.getText().isEmpty() || this.level.getValue() == null || this.courseImageView.getImage() == null) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Incomplete Fields","Please fill in all the fields");
            return;
        }
        if(Objects.equals(this.level.getValue(), "BEGINNER")) {
            this.choiceLevel = "beginner";
        }
        else if(Objects.equals(this.level.getValue(), "INTERMEDIATE")) {
            this.choiceLevel = "intermediate";
        }
        else if(Objects.equals(this.level.getValue(), "ADVANCED")) {
            this.choiceLevel = "advanced";
        }

        CourseService.addCourse(UserService.userConnected.getId(), this.title.getText(), this.description.getText(), this.choiceLevel, this.courseImageView.getImage());
        SceneController sceneController = new SceneController();
        sceneController.switchView("views/principal-view.fxml", event);
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchView("views/principal-view.fxml", event);
    }
}
