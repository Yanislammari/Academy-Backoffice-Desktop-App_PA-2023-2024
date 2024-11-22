package com.example.academyproject.controllers;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.Course;
import com.example.academyproject.models.Enrollment;
import com.example.academyproject.models.User;
import com.example.academyproject.services.CourseService;
import com.example.academyproject.services.EnrollmentService;
import com.example.academyproject.utils.tablecells.ProgressBarTableCell;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentDetailsViewController implements Initializable {
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
    private TableView<Enrollment> coursesTableView;
    @FXML
    private TableColumn<Enrollment, String> courseNameColumn;
    @FXML
    private TableColumn<Enrollment, Double> lessonProgressionColumn;
    @FXML
    private TableColumn<Enrollment, String> noteColumn;
    @FXML
    private TableColumn<Enrollment, Integer> attemptsColumn;
    private static User selectedUser;
    private ObservableList<Enrollment> userEnrollments = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(selectedUser != null) {
            this.loadUserData();
            this.loadUserEnrollments();
        }
    }

    public static void setSelectedUser(User user) {
        selectedUser = user;
    }

    private void loadUserData() {
        if(selectedUser != null) {
            Image image = new Image(selectedUser.getProfilePicture());
            this.profileImageView.setImage(image);
            Circle clip = new Circle(75, 75, 75);
            this.profileImageView.setClip(clip);
            this.idLabel.setText(String.valueOf(selectedUser.getId()));
            this.firstNameLabel.setText(selectedUser.getFirstName());
            this.lastNameLabel.setText(selectedUser.getLastName());
            this.emailLabel.setText(selectedUser.getEmail());
            this.roleLabel.setText(selectedUser.getRole().toString());
        }
    }

    private void loadUserEnrollments() {
        List<Enrollment> enrollments = EnrollmentService.getEnrollmentsByUserId(selectedUser.getId());
        this.userEnrollments.setAll(enrollments);

        this.courseNameColumn.setCellValueFactory(cellData -> {
            Enrollment enrollment = cellData.getValue();
            Course course = CourseService.getCourseById(enrollment.getCourseId());
            return new SimpleStringProperty(course != null ? course.getTitle() : "Unknown course");
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
                if(enrollment.getProgression().getAttempts() == 0) {
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
        this.coursesTableView.setItems(userEnrollments);
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchView("views/principal-view.fxml", event);
    }
}
