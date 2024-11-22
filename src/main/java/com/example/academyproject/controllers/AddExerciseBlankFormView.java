package com.example.academyproject.controllers;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.Course;
import com.example.academyproject.services.CourseService;
import com.example.academyproject.services.ExerciseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddExerciseBlankFormView implements Initializable {
    @FXML
    private ComboBox<Course> courseComboBox;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField correctOptionTextField;
    private ObservableList<Course> courseList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.loadCourses();
    }

    private void loadCourses() {
        List<Course> courses = CourseService.getCourses();
        this.courseList = FXCollections.observableArrayList(courses);
        this.courseComboBox.setItems(this.courseList);

        this.courseComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Course course, boolean empty) {
                super.updateItem(course, empty);
                setText(empty ? "" : course.getTitle());
            }
        });

        this.courseComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Course course, boolean empty) {
                super.updateItem(course, empty);
                setText(empty ? "" : course.getTitle());
            }
        });
    }

    @FXML
    private void handleAddQuestionButton(ActionEvent event) {
        Course selectedCourse = this.courseComboBox.getSelectionModel().getSelectedItem();
        String title = this.titleTextField.getText();
        String correctOption = this.correctOptionTextField.getText();

        if(selectedCourse != null && !title.isEmpty() && !correctOption.isEmpty()) {
            ExerciseService.addFillInTheBlankExercise(selectedCourse.getId(), title, correctOption);
            this.clearForm();
        }
        else {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all the fields.");
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchView("views/principal-view.fxml", event);
    }

    private void clearForm() {
        this.courseComboBox.getSelectionModel().clearSelection();
        this.titleTextField.clear();
        this.correctOptionTextField.clear();
    }
}
