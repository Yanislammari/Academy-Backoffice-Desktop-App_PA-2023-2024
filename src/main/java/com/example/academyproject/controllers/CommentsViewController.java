package com.example.academyproject.controllers;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.Comment;
import com.example.academyproject.models.Course;
import com.example.academyproject.models.User;
import com.example.academyproject.services.CommentService;
import com.example.academyproject.services.CourseService;
import com.example.academyproject.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;

public class CommentsViewController {
    @FXML
    private TableView<Comment> commentTableView;
    @FXML
    private TableColumn<Comment, String> commentIdColumn;
    @FXML
    private TableColumn<Comment, String> userEmailColumn;
    @FXML
    private TableColumn<Comment, String> courseNameColumn;
    @FXML
    private TableColumn<Comment, String> contentColumn;
    @FXML
    private TableColumn<Comment, String> dateColumn;
    @FXML
    private ComboBox<String> courseFilterComboBox;
    @FXML
    private TextField searchTextField;
    private ObservableList<Comment> commentsList;
    private ObservableList<Course> coursesList;
    private ObservableList<User> usersList;

    @FXML
    public void initialize() {
        this.commentsList = CommentService.getComments();
        this.coursesList = CourseService.getCourses();
        this.usersList = UserService.getUsers();

        this.commentIdColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));
        this.userEmailColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(this.getUserEmail(cellData.getValue().getUserId())));
        this.courseNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(this.getCourseName(cellData.getValue().getCourseId())));
        this.contentColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getContent()));
        this.dateColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCreatedAt().split("T")[0]));

        this.commentTableView.setItems(this.commentsList);
        List<String> courseNames = this.coursesList.stream().map(Course::getTitle).collect(Collectors.toList());
        courseNames.add(0, "ALL");
        this.courseFilterComboBox.setItems(FXCollections.observableArrayList(courseNames));
        this.courseFilterComboBox.setValue("ALL");
        this.courseFilterComboBox.setOnAction(event -> this.filterComments());
        this.searchTextField.setOnKeyReleased(this::onSearchKeyReleased);
    }

    private String getUserEmail(String userId) {
        for(User user : this.usersList) {
            if(user.getId().equals(userId)) {
                return user.getEmail();
            }
        }
        return "Unknown user";
    }

    private String getCourseName(String courseId) {
        for(Course course : this.coursesList) {
            if(course.getId().equals(courseId)) {
                return course.getTitle();
            }
        }
        return "Unknown course";
    }

    private void filterComments() {
        String selectedCourse = this.courseFilterComboBox.getValue();
        String searchText = this.searchTextField.getText().toLowerCase();

        ObservableList<Comment> filteredList = this.commentsList.stream()
                .filter(comment -> (selectedCourse.equals("TOUT") || this.getCourseName(comment.getCourseId()).equals(selectedCourse)) && (this.getUserEmail(comment.getUserId()).toLowerCase().contains(searchText)))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        this.commentTableView.setItems(filteredList);
    }

    private void onSearchKeyReleased(KeyEvent event) {
        this.filterComments();
    }

    @FXML
    private void deleteSelectedComment() {
        Comment selectedComment = this.commentTableView.getSelectionModel().getSelectedItem();

        if(selectedComment != null) {
            CommentService.deleteComment(selectedComment.getId());
            this.commentsList.remove(selectedComment);
            this.commentTableView.refresh();
        }
        else {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Please select a comment to delete.");
        }
    }
}
