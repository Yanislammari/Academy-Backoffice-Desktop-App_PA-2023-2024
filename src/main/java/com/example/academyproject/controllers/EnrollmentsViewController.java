package com.example.academyproject.controllers;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.Course;
import com.example.academyproject.models.Enrollment;
import com.example.academyproject.models.User;
import com.example.academyproject.services.CourseService;
import com.example.academyproject.services.EnrollmentService;
import com.example.academyproject.services.UserService;
import com.example.academyproject.utils.tablecells.ProgressBarTableCell;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EnrollmentsViewController implements Initializable {
    @FXML
    private TableView<Enrollment> enrollmentTableView;
    @FXML
    private TableColumn<Enrollment, String> enrollmentIdColumn;
    @FXML
    private TableColumn<Enrollment, String> userEmailColumn;
    @FXML
    private TableColumn<Enrollment, String> courseNameColumn;
    @FXML
    private TableColumn<Enrollment, Double> lessonProgressionColumn;
    @FXML
    private TableColumn<Enrollment, String> noteColumn;
    @FXML
    private TableColumn<Enrollment, Integer> attemptsColumn;
    @FXML
    private TextField searchTextField;
    @FXML
    private ComboBox<String> courseFilterComboBox;
    private ObservableList<Enrollment> enrollments = FXCollections.observableArrayList();
    private FilteredList<Enrollment> filteredEnrollments;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.enrollments = EnrollmentService.getEnrollments();
        this.filteredEnrollments = new FilteredList<>(this.enrollments, p -> true);
        this.initializeTableColumns();
        this.initializeCourseFilterComboBox();
        this.setupListeners();
        this.enrollmentTableView.setItems(this.filteredEnrollments);
    }

    private void initializeTableColumns() {
        this.enrollmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        this.userEmailColumn.setCellValueFactory(cellData -> {
            Enrollment enrollment = cellData.getValue();
            User user = UserService.getUserById(enrollment.getStudentId());
            return new SimpleStringProperty((user != null) ? user.getEmail() : "");
        });

        this.courseNameColumn.setCellValueFactory(cellData -> {
            Enrollment enrollment = cellData.getValue();
            Course course = CourseService.getCourseById(enrollment.getCourseId());
            return new SimpleStringProperty((course != null) ? course.getTitle() : "");
        });

        this.lessonProgressionColumn.setCellValueFactory(cellData -> {
            Enrollment enrollment = cellData.getValue();
            Course course = CourseService.getCourseById(enrollment.getCourseId());

            if(course != null && enrollment.getProgression() != null) {
                int totalLessons = course.getLessons().size();
                int finishedLessons = enrollment.getProgression().getLessonsFinish().size();
                return new SimpleDoubleProperty((double) finishedLessons / totalLessons).asObject();
            }
            return new SimpleDoubleProperty(0).asObject();
        });

        this.lessonProgressionColumn.setCellFactory(column -> new ProgressBarTableCell<>());

        this.noteColumn.setCellValueFactory(cellData -> {
            Enrollment enrollment = cellData.getValue();
            Course course = CourseService.getCourseById(enrollment.getCourseId());

            if(course != null && enrollment.getProgression() != null) {
                int attempts = enrollment.getProgression().getAttempts();
                if(attempts == 0) {
                    return new SimpleStringProperty("NOT DONE");
                }
                else {
                    int exercisesSuccess = enrollment.getProgression().getExercisesSuccess().size();
                    int totalExercises = course.getExecises().size();
                    return new SimpleStringProperty(exercisesSuccess + "/" + totalExercises);
                }
            }
            return new SimpleStringProperty("0/0");
        });

        this.attemptsColumn.setCellValueFactory(cellData -> {
            Enrollment enrollment = cellData.getValue();
            if(enrollment.getProgression() != null) {
                return new SimpleIntegerProperty(enrollment.getProgression().getAttempts()).asObject();
            }
            return new SimpleIntegerProperty(0).asObject();
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
        this.searchTextField.textProperty().addListener((observable, oldValue, newValue) -> this.filterEnrollments());
        this.courseFilterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> this.filterEnrollments());
    }

    @FXML
    private void filterEnrollments() {
        String searchText = this.searchTextField.getText().toLowerCase();
        String selectedCourse = this.courseFilterComboBox.getValue();

        this.filteredEnrollments.setPredicate(enrollment -> {
            boolean matchesSearchText = true;
            boolean matchesCourseFilter = true;
            User user = UserService.getUserById(enrollment.getStudentId());
            if(user != null) {
                matchesSearchText = user.getEmail().toLowerCase().contains(searchText);
            }

            if(!"ALL".equals(selectedCourse)) {
                Course course = CourseService.getCourseById(enrollment.getCourseId());
                if(course != null) {
                    matchesCourseFilter = course.getTitle().equals(selectedCourse);
                }
                else {
                    matchesCourseFilter = false;
                }
            }
            return matchesSearchText && matchesCourseFilter;
        });
    }

    @FXML
    public void buttonDeleteActionEnrollment() {
        Enrollment enrollmentToDelete = this.enrollmentTableView.getSelectionModel().getSelectedItem();
        if(enrollmentToDelete == null) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "You must select a course");
            return;
        }
        this.enrollments.remove(enrollmentToDelete);
        EnrollmentService.deleteEnrollment(enrollmentToDelete);
        this.enrollmentTableView.setItems(this.filteredEnrollments);
    }
}
