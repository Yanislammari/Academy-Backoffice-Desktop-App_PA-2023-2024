package com.example.academyproject.controllers;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.Role;
import com.example.academyproject.services.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.FileWriter;
import java.io.IOException;

public class LoginFormController {
    @FXML
    private TextField emailInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private Label errorMessage;

    @FXML
    protected void loginAction(ActionEvent event) throws IOException {
        String token = UserService.connectUser(this.emailInput.getText(), this.passwordInput.getText());

        if(token != null) {
            if(UserService.tokenToUser(token).getRole() != Role.ADMIN) {
                this.errorMessage.setText("This platform is for administrators only!");
            }
            else {
                saveTokenToFile(token);
                UserService.token = token;
                UserService.userConnected = UserService.tokenToUser(token);
                SceneController sceneController = new SceneController();
                sceneController.switchView("views/principal-view.fxml", event);
            }
        }
        else {
            this.errorMessage.setText("Incorrect information, please try again!");
        }
    }

    private void saveTokenToFile(String token) {
        try(FileWriter fileWriter = new FileWriter("token.json")) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("token", token);
            Gson gson = new Gson();
            gson.toJson(jsonObject, fileWriter);
        }
        catch(IOException e) {
            e.printStackTrace();
            this.errorMessage.setText("Error while saving the token.");
        }
    }
}
