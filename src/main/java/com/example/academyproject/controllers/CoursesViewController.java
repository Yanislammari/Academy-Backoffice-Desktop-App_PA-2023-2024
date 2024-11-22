package com.example.academyproject.controllers;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.Course;
import com.example.academyproject.models.Enrollment;
import com.example.academyproject.models.User;
import com.example.academyproject.services.CourseService;
import com.example.academyproject.services.EnrollmentService;
import com.example.academyproject.services.UserService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CoursesViewController implements Initializable {
    @FXML
    private TableView<Course> coursesTableView;
    @FXML
    private TableColumn<Course, String> courseIdColumn;
    @FXML
    private TableColumn<Course, String> courseTitleColumn;
    @FXML
    private TableColumn<Course, String> courseDescriptionColumn;
    @FXML
    private TableColumn<Course, String> courseLevelColumn;
    @FXML
    private TableColumn<Course, String> courseTeacherFirstName;
    @FXML
    private TableColumn<Course, String> courseTeacherLastName;
    @FXML
    private TableColumn<Course, String> courseEnrollmentsColumn;
    @FXML
    private static Course selectedCourse;
    public static Course courseToDelete;
    @FXML
    private TextField searchTextField;
    private ObservableList<Course> courses = FXCollections.observableArrayList();
    private FilteredList<Course> filteredCourses;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.courses = CourseService.getCourses();
        this.filteredCourses = new FilteredList<>(this.courses, p -> true);
        this.courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.courseTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        this.courseDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        this.courseLevelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));

        this.courseTeacherFirstName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Course, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Course, String> cellData) {
                Course course = cellData.getValue();
                User teacher = UserService.getUserById(course.getTeacherId());
                return new SimpleStringProperty((teacher != null) ? teacher.getFirstName() : "");
            }
        });

        this.courseTeacherLastName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Course, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Course, String> cellData) {
                Course course = cellData.getValue();
                User teacher = UserService.getUserById(course.getTeacherId());
                return new SimpleStringProperty((teacher != null) ? teacher.getLastName() : "");
            }
        });

        this.courseEnrollmentsColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Course, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Course, String> cellData){
                Course course = cellData.getValue();
                int count = getEnrollmentCountByCourseId(course.getId());
                return new SimpleStringProperty(String.valueOf(count));
            }
        });

        this.coursesTableView.setItems(this.filteredCourses);
        this.searchTextField.textProperty().addListener((observable, oldValue, newValue) -> this.filterCoursesByTitle());
    }

    private int getEnrollmentCountByCourseId(String courseId) {
        int count = 0;
        ObservableList<Enrollment> enrollments = EnrollmentService.getEnrollments();

        for(Enrollment enrollment : enrollments) {
            if(enrollment.getCourseId().equals(courseId)) {
                count++;
            }
        }
        return count;
    }

    @FXML
    private void filterCoursesByTitle() {
        String searchText = this.searchTextField.getText().toLowerCase();
        this.filteredCourses.setPredicate(course -> {
            if(searchText == null || searchText.isEmpty()) {
                return true;
            }
            return course.getTitle().toLowerCase().contains(searchText);
        });
    }

    @FXML
    public void buttonDeleteActionCourse() {
        Course courseToDelete = this.coursesTableView.getSelectionModel().getSelectedItem();
        if(courseToDelete == null) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "You must select a course");
            return;
        }

        this.courses.remove(courseToDelete);
        CourseService.deleteCourse(courseToDelete);
        this.coursesTableView.setItems(this.filteredCourses);
    }

    @FXML
    public void setAction(ActionEvent event) throws IOException {
        if(this.coursesTableView.getSelectionModel().getSelectedItems().isEmpty()) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "You must select a course");
        }
        else {
            courseToDelete = this.coursesTableView.getSelectionModel().getSelectedItem();
            SceneController sceneController = new SceneController();
            sceneController.switchView("views/set-course-form.fxml", event);
        }
    }

    @FXML
    public void addAction(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchView("views/add-course-view.fxml", event);
    }

    @FXML
    public void showDetailsAction(ActionEvent event) throws IOException {
        if(this.coursesTableView.getSelectionModel().getSelectedItems().isEmpty()) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "You must select a course");
        }
        else {
            selectedCourse = this.coursesTableView.getSelectionModel().getSelectedItem();
            CourseDetailsViewController.setSelectedCourse(selectedCourse);
            SceneController sceneController = new SceneController();
            sceneController.switchView("course-details-view.fxml", event);
        }
    }
}
