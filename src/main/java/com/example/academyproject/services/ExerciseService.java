package com.example.academyproject.services;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.Course;
import com.example.academyproject.models.Exercise;
import com.example.academyproject.utils.API;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ExerciseService {
    public static ObservableList<Exercise> getAllExercises(ObservableList<Course> allCourse) {
        ArrayList<Exercise> exercises = new ArrayList<>();

        for(Course course : allCourse) {
            exercises.addAll(course.getExecises());
        }
        return FXCollections.observableArrayList(exercises);
    }

    public static void deleteExercise(Exercise exercise) {
        try {
            HttpURLConnection conn = API.connectAPI("courses/deleteExercise/" + exercise.getIdCourse() + "/" + exercise.getId());
            assert conn != null;
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "Bearer " + UserService.token);
            conn.setRequestProperty("Content-Type", "application/json");
            int responseCode = conn.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                SceneController.showAlert(Alert.AlertType.INFORMATION, "Success", "Exercise deleted successfully");
            }
            else {
                SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void addMultipleChoiceExercise(String courseId, String title, ArrayList<String> options, String correctOption) {
        Dotenv dotenv = Dotenv.load();

        try {
            URL url = new URL(dotenv.get("BASE_URL") + "courses/addExercise/" + courseId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + UserService.token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String optionsJson = String.join("\",\"", options);
            String jsonInputString = String.format(
                    "{ \"title\": \"%s\", \"description\": \"description\", \"type\": \"multiple_choice\", \"difficulty\": \"medium\", \"options\": [\"%s\"], \"correctOption\": \"%s\" }",
                    title, optionsJson, correctOption
            );

            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            int responseCode = conn.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                SceneController.showAlert(Alert.AlertType.INFORMATION, "Success", "Multiple choice exercise added successfully");
            }
            else {
                SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void addFillInTheBlankExercise(String courseId, String title, String correctOption) {
        Dotenv dotenv = Dotenv.load();

        try {
            URL url = new URL(dotenv.get("BASE_URL") + "courses/addExercise/" + courseId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + UserService.token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format(
                    "{ \"title\": \"%s\", \"description\": \"description\", \"type\": \"fill_in_the_blank\", \"difficulty\": \"medium\", \"options\": [], \"correctOption\": \"%s\" }",
                    title, correctOption
            );

            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            int responseCode = conn.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                SceneController.showAlert(Alert.AlertType.INFORMATION, "Success", "Fill-in-the-blank exercise added successfully");
            }
            else {
                SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
