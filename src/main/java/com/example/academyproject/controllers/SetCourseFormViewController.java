package com.example.academyproject.controllers;

import com.example.academyproject.SceneController;
import com.example.academyproject.services.CourseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SetCourseFormViewController implements Initializable {
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
        if(Objects.equals(this.level.valueProperty().getValue(), "BEGINNER")) {
            this.choiceLevel = "beginner";
        }
        else if(Objects.equals(this.level.valueProperty().getValue(), "INTERMEDIATE")) {
            this.choiceLevel = "intermediate";
        }
        else if(Objects.equals(this.level.valueProperty().getValue(), "ADVANCED")) {
            this.choiceLevel = "advanced";
        }

        CourseService.editCourse(CoursesViewController.courseToDelete.getId(), this.title.getText(), this.description.getText(), this.choiceLevel, this.courseImageView.getImage());
        SceneController sceneController = new SceneController();
        sceneController.switchView("views/principal-view.fxml", event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.title.setText(CoursesViewController.courseToDelete.getTitle());
        this.description.setText(CoursesViewController.courseToDelete.getDescription());
    }
}
