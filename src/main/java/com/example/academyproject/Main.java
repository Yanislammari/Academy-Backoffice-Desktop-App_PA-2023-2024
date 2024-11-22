package com.example.academyproject;

import com.example.academyproject.services.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.FileReader;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String token = readTokenFromFile("token.json");
        FXMLLoader fxmlLoader;

        if(!token.isEmpty()) {
            try {
                UserService.userConnected = UserService.tokenToUser(token);
                if(UserService.userConnected == null) {
                    fxmlLoader = new FXMLLoader(Main.class.getResource("views/login-form.fxml"));
                }
                else {
                    UserService.token = token;
                    fxmlLoader = new FXMLLoader(Main.class.getResource("views/principal-view.fxml"));
                }
            }
            catch(Exception ignored) {
                fxmlLoader = new FXMLLoader(Main.class.getResource("views/login-form.fxml"));
            }
        }
        else {
            fxmlLoader = new FXMLLoader(Main.class.getResource("views/login-form.fxml"));
        }

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Academy Back Office");
        stage.setScene(scene);
        stage.setWidth(1215);
        stage.setHeight(700);
        stage.show();
    }

    private String readTokenFromFile(String filePath) {
        try(FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            return jsonObject.has("token") ? jsonObject.get("token").getAsString() : "";
        }
        catch(IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args){
        launch();
    }
}
