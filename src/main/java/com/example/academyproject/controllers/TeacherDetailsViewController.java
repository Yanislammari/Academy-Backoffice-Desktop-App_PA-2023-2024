package com.example.academyproject.controllers;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.Course;
import com.example.academyproject.models.Enrollment;
import com.example.academyproject.models.User;
import com.example.academyproject.services.CourseService;
import com.example.academyproject.services.EnrollmentService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TeacherDetailsViewController implements Initializable {
    @FXML
    private ImageView profileImageView;
    @FXML
    private Label idLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private TableView<Course> coursesTableView;
    @FXML
    private TableColumn<Course, String> courseNameColumn;
    @FXML
    private TableColumn<Course, String> courseDescriptionColumn;
    @FXML
    private TableColumn<Course, String> courseLevelColumn;
    @FXML
    private TableColumn<Course, String> enrollmentNumber;
    private static User selectedUser;
    private ObservableList<Course> userCourses = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(selectedUser != null) {
            this.loadUserData();
            this.loadUserCourses();
        }
    }

    public static void setSelectedUser(User user) {
        selectedUser = user;
    }

    private void loadUserData() {
        if(selectedUser != null) {
            this.idLabel.setText(selectedUser.getId());
            this.firstNameLabel.setText(selectedUser.getFirstName());
            this.lastNameLabel.setText(selectedUser.getLastName());
            this.emailLabel.setText(selectedUser.getEmail());
            this.roleLabel.setText(selectedUser.getRole().toString());
            Image image = new Image(selectedUser.getProfilePicture());
            this.profileImageView.setImage(image);
            Circle clip = new Circle(75, 75, 75);
            this.profileImageView.setClip(clip);
        }
    }

    private void loadUserCourses() {
        ObservableList<Course> allCourses = CourseService.getCourses();

        this.userCourses.setAll(allCourses.stream()
                .filter(course -> course.getTeacherId().equals(selectedUser.getId()))
                .collect(Collectors.toList()));

        this.courseNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        this.courseDescriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        this.courseLevelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLevel().toString()));

        this.enrollmentNumber.setCellValueFactory(cellData -> {
            Course course = cellData.getValue();
            int enrollmentCount = this.getEnrollmentCountByCourseId(course.getId());
            return new SimpleStringProperty(String.valueOf(enrollmentCount));
        });

        this.coursesTableView.setItems(this.userCourses);
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
    private void handleBack(ActionEvent event) throws IOException{
        SceneController sceneController = new SceneController();
        sceneController.switchView("views/principal-view.fxml", event);
    }
}
