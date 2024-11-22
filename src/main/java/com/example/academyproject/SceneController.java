package com.example.academyproject;

import com.example.academyproject.controllers.TeacherDetailsViewController;
import com.example.academyproject.services.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class SceneController implements Initializable {
    @FXML
    private Pane mainContent;
    @FXML
    private Button userConnectedButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadHomeView();
            userConnectedButton.setText(UserService.userConnected.getFirstName() + " " + UserService.userConnected.getLastName());
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadStages(String fxmlPath) {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void switchView(String fxmlPath, ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void loadSceneView(String fxmlPath) throws IOException {
        Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
        this.mainContent.getChildren().setAll(view);
    }

    public void loadProfileView(ActionEvent event) throws IOException {
        TeacherDetailsViewController.setSelectedUser(UserService.userConnected);
        switchView("views/teacher-details-view.fxml", event);
    }

    public void loadHomeView() throws IOException {
        loadSceneView("views/home-view.fxml");
    }

    public void loadUserView() throws IOException {
        loadSceneView("views/users-view.fxml");
    }

    public void loadCourseView() throws IOException {
        loadSceneView("views/courses-view.fxml");
    }

    public void loadLessonView() throws IOException {
        loadSceneView("views/lessons-view.fxml");
    }

    public void loadExerciseView() throws IOException {
        loadSceneView("views/exercises-view.fxml");
    }

    public void loadEnrollmentView() throws IOException {
        loadSceneView("views/enrollments-view.fxml");
    }

    public void loadCommentsView() throws IOException {
        loadSceneView("views/comments-view.fxml");
    }

    public void loadStatisticsView() throws IOException {
        loadSceneView("views/statistics-view.fxml");
    }

    public void loadFeedbackView() throws IOException {
        loadSceneView("views/feedback-view.fxml");
    }

    public void logout(ActionEvent event) throws IOException {
        String filePath = "token.json";
        Gson gson = new Gson();
        JsonObject jsonObject;
        try(FileReader reader = new FileReader(filePath)) {
            jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        }
        jsonObject.remove("token");
        try(FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(jsonObject, writer);
        }
        switchView("views/login-form.fxml", event);
    }

    static public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void buttonHoover(Button button) {
        button.setStyle("-fx-background-color: #5F8CDC;");
    }

    public void buttonExit(Button button) {
        button.setStyle("-fx-background-color: #6495ED;");
    }

    public void buttonHooverHandler(MouseEvent event) {
        Button button = (Button) event.getSource();
        buttonHoover(button);
    }

    public void buttonExitHandler(MouseEvent event) {
        Button button = (Button) event.getSource();
        buttonExit(button);
    }
}
