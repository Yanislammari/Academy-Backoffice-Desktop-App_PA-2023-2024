package com.example.academyproject.controllers;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.Course;
import com.example.academyproject.models.Enrollment;
import com.example.academyproject.models.Exercise;
import com.example.academyproject.models.Lesson;
import com.example.academyproject.models.User;
import com.example.academyproject.services.EnrollmentService;
import com.example.academyproject.services.UserService;
import com.example.academyproject.utils.tablecells.ProgressBarTableCell;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.util.Callback;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Circle;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.util.stream.Collectors;

public class CourseDetailsViewController implements Initializable {
    @FXML
    private ImageView courseImageView;
    @FXML
    private Label courseDescriptionLabel;
    @FXML
    private TableView<Lesson> lessonsTableView;
    @FXML
    private TableColumn<Lesson, String> lessonNameColumn;
    @FXML
    private TableColumn<Lesson, String> lessonDescriptionColumn;
    @FXML
    private TableView<Exercise> questionsTableView;
    @FXML
    private TableColumn<Exercise, String> questionColumn;
    @FXML
    private TableView<User> usersTableView;
    @FXML
    private TableColumn<User, String> userProfilePictureColumn;
    @FXML
    private TableColumn<User, String> userLastNameColumn;
    @FXML
    private TableColumn<User, String> userFirstNameColumn;
    @FXML
    private TableColumn<User, String> userEmailColumn;
    @FXML
    private TableColumn<User, Double> userProgressionColumn;
    @FXML
    private TableColumn<User, String> userGradeColumn;
    @FXML
    private TableColumn<User, Integer> userAttemptsColumn;
    private static Course selectedCourse;

    public static void setSelectedCourse(Course course) {
        selectedCourse = course;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCourseDetails(selectedCourse);
        this.lessonNameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        this.lessonDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        this.questionColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        this.configureUserTableColumns();
        this.loadCourseLessons(selectedCourse);
        this.loadCourseExercises(selectedCourse);
        this.loadCourseUsers(selectedCourse);
    }

    private void configureUserTableColumns() {
        this.userProfilePictureColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProfilePicture()));
        
        this.userProfilePictureColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<User, String> call(TableColumn<User, String> param) {
                return new TableCell<>() {
                    private final ImageView imageView = new ImageView();
                    private final Circle clip = new Circle(); {
                        imageView.setFitWidth(30);
                        imageView.setFitHeight(30);
                        clip.setRadius(15);
                        clip.setCenterX(15);
                        clip.setCenterY(15);
                        imageView.setClip(clip);
                        setGraphic(imageView);
                    }

                    @Override
                    protected void updateItem(String imageUrl, boolean empty) {
                        super.updateItem(imageUrl, empty);
                        if(empty || imageUrl == null) {
                            setGraphic(null);
                        }
                        else {
                            Image image = new Image(imageUrl);
                            imageView.setImage(image);
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });

        this.userLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        this.userFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        this.userEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        this.userProgressionColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            Enrollment enrollment = findEnrollmentForUserAndCourse(user.getId(), selectedCourse.getId());

            if(enrollment != null && enrollment.getProgression() != null) {
                Course course = selectedCourse;
                int totalLessons = course.getLessons().size();
                int finishedLessons = enrollment.getProgression().getLessonsFinish().size();
                double progress = (double) finishedLessons / totalLessons;
                return new SimpleDoubleProperty(progress).asObject();
            }
            return new SimpleDoubleProperty(0).asObject();
        });

        this.userProgressionColumn.setCellFactory(column -> new ProgressBarTableCell<>());

        this.userGradeColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            Enrollment enrollment = findEnrollmentForUserAndCourse(user.getId(), selectedCourse.getId());

            if(enrollment != null && enrollment.getProgression() != null) {
                Course course = selectedCourse;
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

        this.userAttemptsColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            Enrollment enrollment = findEnrollmentForUserAndCourse(user.getId(), selectedCourse.getId());

            if(enrollment != null && enrollment.getProgression() != null) {
                return new SimpleIntegerProperty(enrollment.getProgression().getAttempts()).asObject();
            }
            return new SimpleIntegerProperty(0).asObject();
        });
    }

    private Enrollment findEnrollmentForUserAndCourse(String userId, String courseId) {
        List<Enrollment> allEnrollments = EnrollmentService.getEnrollments();

        return allEnrollments.stream()
                .filter(enrollment -> enrollment.getCourseId().equals(courseId) && enrollment.getStudentId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public void loadCourseDetails(Course course) {
        if(course == null) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "No course selected");
            return;
        }
        if(course.getImageUrl() != null && !course.getImageUrl().isEmpty()) {
            Image courseImage = new Image(course.getImageUrl(), true);
            this.courseImageView.setImage(courseImage);
        }
        else {
            this.courseImageView.setImage(new Image("/images/default-course-image.png"));
        }
        this.courseDescriptionLabel.setText(course.getDescription());
    }

    public void loadCourseLessons(Course course) {
        if(course == null || course.getLessons().isEmpty()) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "No lesson available for this course");
            return;
        }
        ObservableList<Lesson> lessons = FXCollections.observableArrayList(course.getLessons());
        this.lessonsTableView.setItems(lessons);
    }

    public void loadCourseExercises(Course course) {
        if(course == null || course.getExecises().isEmpty()) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "No exercise available for this course");
            return;
        }
        ObservableList<Exercise> exercises = FXCollections.observableArrayList(course.getExecises());
        this.questionsTableView.setItems(exercises);
    }

    public void loadCourseUsers(Course course) {
        if(course == null) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "No course selected");
            return;
        }

        List<Enrollment> allEnrollments = EnrollmentService.getEnrollments();
        List<Enrollment> courseEnrollments = allEnrollments.stream()
                .filter(enrollment -> enrollment.getCourseId().equals(course.getId()))
                .collect(Collectors.toList());

        ObservableList<User> users = FXCollections.observableArrayList();
        for(Enrollment enrollment : courseEnrollments) {
            User user = UserService.getUserById(enrollment.getStudentId());
            if(user != null) {
                users.add(user);
            }
        }

        this.usersTableView.setItems(users);
    }

    @FXML
    public void handleBack(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchView("views/principal-view.fxml", event);
    }
}
