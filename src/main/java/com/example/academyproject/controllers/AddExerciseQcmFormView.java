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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddExerciseQcmFormView implements Initializable {
    @FXML
    private ComboBox<Course> courseComboBox;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField choice1TextField;
    @FXML
    private TextField choice2TextField;
    @FXML
    private TextField choice3TextField;
    @FXML
    private TextField choice4TextField;
    @FXML
    private ComboBox<String> correctChoiceComboBox;
    private ObservableList<Course> courseList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.loadCourses();
        this.setupTextFields();
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

    private void setupTextFields() {
        this.titleTextField.textProperty().addListener((obs, oldText, newText) -> this.updateCorrectChoiceComboBox());
        this.choice1TextField.textProperty().addListener((obs, oldText, newText) -> this.updateCorrectChoiceComboBox());
        this.choice2TextField.textProperty().addListener((obs, oldText, newText) -> this.updateCorrectChoiceComboBox());
        this.choice3TextField.textProperty().addListener((obs, oldText, newText) -> this.updateCorrectChoiceComboBox());
        this.choice4TextField.textProperty().addListener((obs, oldText, newText) -> this.updateCorrectChoiceComboBox());
    }

    private void updateCorrectChoiceComboBox() {
        String choice1 = this.choice1TextField.getText();
        String choice2 = this.choice2TextField.getText();
        String choice3 = this.choice3TextField.getText();
        String choice4 = this.choice4TextField.getText();

        ArrayList<String> choices = new ArrayList<>();

        if(!choice1.isEmpty()) {
            choices.add(choice1);
        }
        if(!choice2.isEmpty()) {
            choices.add(choice2);
        }
        if(!choice3.isEmpty()) {
            choices.add(choice3);
        }
        if(!choice4.isEmpty()) {
            choices.add(choice4);
        }

        this.correctChoiceComboBox.setItems(FXCollections.observableArrayList(choices));
    }

    @FXML
    private void handleAddQuestionButton(ActionEvent event) {
        Course selectedCourse = this.courseComboBox.getSelectionModel().getSelectedItem();
        String title = this.titleTextField.getText();
        String choice1 = this.choice1TextField.getText();
        String choice2 = this.choice2TextField.getText();
        String choice3 = this.choice3TextField.getText();
        String choice4 = this.choice4TextField.getText();
        String correctChoice = this.correctChoiceComboBox.getValue();

        if(selectedCourse != null && !title.isEmpty() && !choice1.isEmpty() && !choice2.isEmpty() && !choice3.isEmpty() && !choice4.isEmpty() && correctChoice != null) {
            ArrayList<String> options = new ArrayList<>();

            options.add(choice1);
            options.add(choice2);
            options.add(choice3);
            options.add(choice4);

            ExerciseService.addMultipleChoiceExercise(selectedCourse.getId(), title, options, correctChoice);
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
        this.choice1TextField.clear();
        this.choice2TextField.clear();
        this.choice3TextField.clear();
        this.choice4TextField.clear();
        this.correctChoiceComboBox.getItems().clear();
    }
}
