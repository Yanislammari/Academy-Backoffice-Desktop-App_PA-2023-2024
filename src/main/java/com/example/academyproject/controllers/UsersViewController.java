package com.example.academyproject.controllers;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.Role;
import com.example.academyproject.models.User;
import com.example.academyproject.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class UsersViewController implements Initializable {
    public static User userToSet;
    @FXML
    private TableView<User> usersTableView;
    @FXML
    private TableColumn<User, String> userProfilePictureColumn;
    @FXML
    private TableColumn<User, String> userIdColumn;
    @FXML
    private TableColumn<User, String> userFirstNameColumn;
    @FXML
    private TableColumn<User, String> userLastNameColumn;
    @FXML
    private TableColumn<User, String> userEmailColumn;
    @FXML
    private TableColumn<User, String> userRoleColumn;
    @FXML
    private TextField searchTextField;
    private ObservableList<User> users = FXCollections.observableArrayList();
    private FilteredList<User> filteredUsers;
    private final Map<String, Image> imageCache = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.users = UserService.getUsers();
        this.filteredUsers = new FilteredList<>(this.users, p -> true);
        this.userProfilePictureColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProfilePicture()));

        this.userProfilePictureColumn.setCellFactory(column -> {
            TableCell<User, String> cell = new TableCell<>() {
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
                        if(imageCache.containsKey(imageUrl)) {
                            imageView.setImage(imageCache.get(imageUrl));
                        }
                        else {
                            Image image = new Image(imageUrl);
                            imageCache.put(imageUrl, image);
                            imageView.setImage(image);
                        }
                        setGraphic(imageView);
                    }
                }
            };
            return cell;
        });

        this.userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.userFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        this.userLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        this.userEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.userRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        this.usersTableView.setItems(this.filteredUsers);
        this.searchTextField.textProperty().addListener((observable, oldValue, newValue) -> this.filterUsersByEmail());
    }

    @FXML
    private void filterUsersByEmail() {
        String searchText = this.searchTextField.getText().toLowerCase();
        this.filteredUsers.setPredicate(user -> {
            if(searchText == null || searchText.isEmpty()) {
                return true;
            }
            return user.getEmail().toLowerCase().contains(searchText);
        });
    }

    @FXML
    public void buttonDeleteAction() {
        if(this.usersTableView.getSelectionModel().getSelectedItems().isEmpty()){
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "You must select a user.");
        }
        else {
            User userToDelete = this.usersTableView.getSelectionModel().getSelectedItem();
            if(userToDelete.getId().equals(UserService.userConnected.getId())) {
                SceneController.showAlert(Alert.AlertType.ERROR, "Error", "You cannot delete your own account !");
            }
            else {
                UserService.deleteUser(userToDelete);
                this.users.remove(userToDelete);
            }
        }
    }

    @FXML
    public void addAction(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchView("views/add-user-form.fxml", event);
    }

    @FXML
    public void setAction(ActionEvent event) throws IOException {
        if(this.usersTableView.getSelectionModel().getSelectedItems().isEmpty()) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "You must select a user.");
        }
        else {
            userToSet = this.usersTableView.getSelectionModel().getSelectedItem();
            SceneController sceneController = new SceneController();
            sceneController.switchView("views/set-user-form.fxml", event);
        }
    }

    @FXML
    public void showDetailsAction(ActionEvent event) throws IOException {
        if(this.usersTableView.getSelectionModel().getSelectedItems().isEmpty()) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "You must select a user.");
        }
        else {
            userToSet = this.usersTableView.getSelectionModel().getSelectedItem();
            if(userToSet.getRole() == Role.STUDENT) {
                StudentDetailsViewController.setSelectedUser(userToSet);
                SceneController sceneController = new SceneController();
                sceneController.switchView("views/student-details-view.fxml", event);
            }
            else {
                TeacherDetailsViewController.setSelectedUser(userToSet);
                SceneController sceneController = new SceneController();
                sceneController.switchView("views/teacher-details-view.fxml", event);
            }
        }
    }
}
