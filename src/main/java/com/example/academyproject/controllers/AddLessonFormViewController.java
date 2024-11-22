package com.example.academyproject.controllers;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.Course;
import com.example.academyproject.services.CourseService;
import com.example.academyproject.services.LessonService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class AddLessonFormViewController {
    @FXML
    private ComboBox<String> courseComboBox;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Button selectVideoButton;
    @FXML
    private Button addLessonButton;
    @FXML
    private Label videoFilePathLabel;
    private File selectedVideoFile;
    private ObservableList<Course> allCourses = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        this.allCourses = CourseService.getCourses();
        List<String> courseTitles = this.allCourses.stream().map(Course::getTitle).toList();
        this.courseComboBox.setItems(FXCollections.observableArrayList(courseTitles));
        this.courseComboBox.setValue("Select a course");
        this.selectVideoButton.setOnAction(event -> selectVideoFile());
        this.addLessonButton.setOnAction(event -> addLesson());
    }

    @FXML
    private void selectVideoFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.mkv"));
        this.selectedVideoFile = fileChooser.showOpenDialog((Window) this.selectVideoButton.getScene().getWindow());

        if(this.selectedVideoFile != null) {
            this.videoFilePathLabel.setText("Selected file: " + this.selectedVideoFile.getAbsolutePath());
        }
        else {
            this.videoFilePathLabel.setText("No file selected");
        }
    }

    @FXML
    private void addLesson() {
        String selectedCourseTitle = this.courseComboBox.getValue();
        Course selectedCourse = this.allCourses.stream()
                .filter(course -> course.getTitle().equals(selectedCourseTitle))
                .findFirst()
                .orElse(null);

        if(selectedCourse == null) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Please select a valid course.");
            return;
        }

        String lessonTitle = this.titleTextField.getText();
        String lessonDescription = this.descriptionTextArea.getText();

        if(this.selectedVideoFile == null || lessonTitle.isEmpty() || lessonDescription.isEmpty()) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all the fields and select a video file.");
            return;
        }

        LessonService.addLesson(selectedCourse.getId(), this.selectedVideoFile, lessonTitle, lessonDescription);
        this.clearForm();
    }

    private void clearForm() {
        this.titleTextField.clear();
        this.descriptionTextArea.clear();
        this.videoFilePathLabel.setText("No file selected");
        this.courseComboBox.setValue("Select a course");
        this.selectedVideoFile = null;
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchView("views/principal-view.fxml", event);
    }
}
