package com.example.academyproject.controllers;

import com.example.academyproject.SceneController;
import com.example.academyproject.services.UserService;
import com.example.academyproject.utils.SecurityConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SetUserFormViewController implements Initializable {
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private Button choosePhotoButton;
    @FXML
    private ImageView profileImageView;
    private String choiceRole;

    @FXML
    private void handleChoosePhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(this.choosePhotoButton.getScene().getWindow());

        if(file != null) {
            try {
                Image image = new Image(file.toURI().toString());
                this.profileImageView.setImage(image);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleEdit(ActionEvent event) throws IOException {
        if(this.firstName.getText().isEmpty() || this.lastName.getText().isEmpty() || this.email.getText().isEmpty() || this.password.getText().isEmpty() || this.profileImageView.getImage() == null || this.roleComboBox.getValue() == null) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled in!");
            return;
        }
        if(!SecurityConfig.isValidPassword(this.password.getText())) {
            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "The password must contain an uppercase letter, a lowercase letter, a number, and a special character!");
            return;
        }
        if(Objects.equals(this.roleComboBox.getValue(), "ADMIN")) {
            this.choiceRole = "Admin";
        }
        else if(Objects.equals(this.roleComboBox.getValue(), "STUDENT")) {
            this.choiceRole = "Student";
        }
        else if(Objects.equals(this.roleComboBox.getValue(), "TEACHER")) {
            this.choiceRole = "Teacher";
        }

        UserService.editUser(UsersViewController.userToSet.getId(), this.firstName.getText(), this.lastName.getText(), this.email.getText(), this.password.getText(), this.choiceRole, this.profileImageView.getImage());
        SceneController sceneController = new SceneController();
        sceneController.switchView("views/principal-view.fxml", event);
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchView("views/principal-view.fxml", event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.firstName.setText(UsersViewController.userToSet.getFirstName());
        this.lastName.setText(UsersViewController.userToSet.getLastName());
        this.email.setText(UsersViewController.userToSet.getEmail());
        this.roleComboBox.setValue(UsersViewController.userToSet.getRole().toString());
    }
}
