package com.example.academyproject.controllers;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.Course;
import com.example.academyproject.models.Lesson;
import com.example.academyproject.services.CourseService;
import com.example.academyproject.services.LessonService;
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

public class LessonsViewController implements Initializable {
    @FXML
    private TableView<Lesson> lessonsTableView;
    @FXML
    private TableColumn<Lesson, String> lessonIdColumn;
    @FXML
    private TableColumn<Lesson, String> lessonTitleColumn;
    @FXML
    private TableColumn<Lesson, String> lessonDescriptionColumn;
    @FXML
    private TableColumn<Lesson, String> lessonCourseName;
    @FXML
    private TextField searchTextField;
    @FXML
    private ComboBox<String> courseFilterComboBox;
    private Lesson lessonDetail;
    private ObservableList<Lesson> lessons = FXCollections.observableArrayList();
    private FilteredList<Lesson> filteredLessons;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.lessons = LessonService.getAllLessons(CourseService.getCourses());
        this.filteredLessons = new FilteredList<>(this.lessons, p -> true);
        this.initializeTableColumns();
        this.initializeCourseFilterComboBox();
        this.setupListeners();
        this.lessonsTableView.setItems(this.filteredLessons);
    }

    private void initializeTableColumns() {
        this.lessonIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.lessonTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        this.lessonDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        this.lessonCourseName.setCellValueFactory(cellData -> {
            Lesson lesson = cellData.getValue();
            Course course = CourseService.getCourseById(lesson.getIdCourse());
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
        this.searchTextField.textProperty().addListener((observable, oldValue, newValue) -> this.filterLessons());
        this.courseFilterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> this.filterLessons());
    }

    @FXML
    private void filterLessons() {
        String searchText = this.searchTextField.getText().toLowerCase();
        String selectedCourse = this.courseFilterComboBox.getValue();

        this.filteredLessons.setPredicate(lesson -> {
            boolean matchesSearchText = lesson.getTitle().toLowerCase().contains(searchText);
            boolean matchesCourseFilter = "ALL".equals(selectedCourse) || CourseService.getCourseById(lesson.getIdCourse()).getTitle().equals(selectedCourse);
            return matchesSearchText && matchesCourseFilter;
        });
    }

    @FXML
    public void buttonDeleteActionLesson() {
        Lesson lessonToDelete = this.lessonsTableView.getSelectionModel().getSelectedItem();
        if(lessonToDelete == null){
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "You must select a lesson");
            return;
        }
        this.lessons.remove(lessonToDelete);
        LessonService.deleteLesson(lessonToDelete);
        this.lessonsTableView.setItems(this.filteredLessons);
    }

    @FXML
    public void addAction(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchView("add-lesson-form.fxml", event);
    }

    @FXML
    public void showDetailsAction(ActionEvent event) throws IOException {
        if(this.lessonsTableView.getSelectionModel().getSelectedItems().isEmpty()) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "You must select a lesson");
        }
        else {
            this.lessonDetail = this.lessonsTableView.getSelectionModel().getSelectedItem();
            LessonDetailsViewController.setSelectedLesson(this.lessonDetail);
            SceneController sceneController = new SceneController();
            sceneController.switchView("views/lesson-details-view.fxml", event);
        }
    }

    @FXML
    public void editLessonAction(ActionEvent event) throws IOException {
        if(this.lessonsTableView.getSelectionModel().getSelectedItems().isEmpty()) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "You must select a lesson");
        }
        else {
            Lesson lessonToEdit = this.lessonsTableView.getSelectionModel().getSelectedItem();
            SetLessonFormViewController.setSelectedLesson(lessonToEdit);
            SceneController sceneController = new SceneController();
            sceneController.switchView("views/set-lesson-form.fxml", event);
        }
    }
}
