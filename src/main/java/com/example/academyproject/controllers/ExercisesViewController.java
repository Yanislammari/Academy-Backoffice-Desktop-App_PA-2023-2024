package com.example.academyproject.controllers;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.Course;
import com.example.academyproject.models.Exercise;
import com.example.academyproject.services.CourseService;
import com.example.academyproject.services.ExerciseService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ExercisesViewController implements Initializable {
    @FXML
    private TableView<Exercise> exercisesTableView;
    @FXML
    private TableColumn<Exercise, String> exerciseIdColumn;
    @FXML
    private TableColumn<Exercise, String> exerciseQuestionColumn;
    @FXML
    private TableColumn<Exercise, String> exerciseCourseName;
    @FXML
    private TextField searchTextField;
    @FXML
    private ComboBox<String> courseFilterComboBox;
    private ObservableList<Exercise> exercises = FXCollections.observableArrayList();
    private FilteredList<Exercise> filteredExercises;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.exercises = ExerciseService.getAllExercises(CourseService.getCourses());
        this.filteredExercises = new FilteredList<>(this.exercises, p -> true);
        this.initializeTableColumns();
        this.initializeCourseFilterComboBox();
        this.setupListeners();
        this.exercisesTableView.setItems(this.filteredExercises);
    }

    private void initializeTableColumns() {
        this.exerciseIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.exerciseQuestionColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        this.exerciseCourseName.setCellValueFactory(cellData -> {
            Exercise exercise = cellData.getValue();
            Course course = CourseService.getCourseById(exercise.getIdCourse());
            return new SimpleStringProperty((course != null) ? course.getTitle() : "");
        });
    }

    private void initializeCourseFilterComboBox() {
        ObservableList<String> courseTitles = FXCollections.observableArrayList();
        courseTitles.add("ALL");

        courseTitles.addAll(CourseService.getCourses().stream()
                .map(Course::getTitle)
                .collect(Collectors.toList()));

        this.courseFilterComboBox.setItems(courseTitles);
        this.courseFilterComboBox.setValue("ALL");
    }

    private void setupListeners() {
        this.searchTextField.textProperty().addListener((observable, oldValue, newValue) -> this.filterExercises());
        this.courseFilterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> this.filterExercises());
    }

    @FXML
    private void filterExercises() {
        String searchText = this.searchTextField.getText().toLowerCase();
        String selectedCourse = this.courseFilterComboBox.getValue();

        this.filteredExercises.setPredicate(exercise -> {
            boolean matchesSearchText = exercise.getTitle().toLowerCase().contains(searchText);
            boolean matchesCourseFilter = "ALL".equals(selectedCourse) || CourseService.getCourseById(exercise.getIdCourse()).getTitle().equals(selectedCourse);
            return matchesSearchText && matchesCourseFilter;
        });
    }

    public void addQcmAction(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchView("views/add-exercise-qcm-form.fxml", event);
    }

    public void addBlankAction(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchView("views/add-exercise-blank-form.fxml", event);
    }

    @FXML
    public void buttonDeleteActionExercise() {
        Exercise exerciseToDelete = this.exercisesTableView.getSelectionModel().getSelectedItem();
        if(exerciseToDelete == null) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "You must select a course");
            return;
        }
        this.exercises.remove(exerciseToDelete);
        ExerciseService.deleteExercise(exerciseToDelete);
        this.exercisesTableView.setItems(this.filteredExercises);
    }
}
