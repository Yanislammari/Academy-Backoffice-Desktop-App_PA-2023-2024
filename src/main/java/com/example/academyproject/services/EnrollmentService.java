package com.example.academyproject.services;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.Enrollment;
import com.example.academyproject.models.Progression;
import com.example.academyproject.utils.API;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class EnrollmentService {
    public static ObservableList<Enrollment> getEnrollments() {
        ObservableList<Enrollment> enrollments = FXCollections.observableArrayList();
        try {
            HttpURLConnection conn = API.connectAPI("enrollments");
            assert conn != null;
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + UserService.token);
            conn.setRequestProperty("Content-Type", "application/json");
            int responseCode = conn.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
                try(BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while((inputLine = in.readLine()) != null){
                        response.append(inputLine);
                    }
                    Gson gson = new Gson();
                    JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
                    JsonArray jsonArray = jsonResponse.has("enrollments") ? jsonResponse.getAsJsonArray("enrollments") : new JsonArray();
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

                    for(JsonElement jsonElement : jsonArray) {
                        JsonObject jsonEnrollment = jsonElement.getAsJsonObject();
                        String id = jsonEnrollment.has("_id") ? jsonEnrollment.get("_id").getAsString() : "";
                        String studentId = jsonEnrollment.has("studentId") ? jsonEnrollment.get("studentId").getAsString() : "";
                        String courseId = jsonEnrollment.has("courseId") ? jsonEnrollment.get("courseId").getAsString() : "";
                        OffsetDateTime enrolledDateODT = jsonEnrollment.has("enrolledDate") ? OffsetDateTime.parse(jsonEnrollment.get("enrolledDate").getAsString(), dateFormatter) : OffsetDateTime.now();
                        Date enrolledDate = Date.from(enrolledDateODT.toInstant());
                        JsonObject jsonProgression = jsonEnrollment.has("progression") ? jsonEnrollment.getAsJsonObject("progression") : null;
                        Progression progression = null;

                        if(jsonProgression != null) {
                            String progressionId = jsonProgression.has("_id") ? jsonProgression.get("_id").getAsString() : "";
                            ArrayList<String> lessonsFinish = new ArrayList<>();
                            JsonArray lessonsArray = jsonProgression.has("lessonsFinish") ? jsonProgression.getAsJsonArray("lessonsFinish") : new JsonArray();

                            for(JsonElement lesson : lessonsArray) {
                                lessonsFinish.add(lesson.getAsString());
                            }

                            ArrayList<String> exercisesSuccess = new ArrayList<>();
                            JsonArray exercisesArray = jsonProgression.has("exercicesSuccess") ? jsonProgression.getAsJsonArray("exercicesSuccess") : new JsonArray();

                            for(JsonElement exercise : exercisesArray) {
                                exercisesSuccess.add(exercise.getAsString());
                            }

                            int attempts = jsonProgression.get("attempts").getAsInt();
                            progression = new Progression(progressionId, lessonsFinish, exercisesSuccess, attempts);
                        }
                        Enrollment enrollment = new Enrollment(id, studentId, courseId, enrolledDate, progression);
                        enrollments.add(enrollment);
                    }
                }
            }
            else {
                SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    public static void deleteEnrollment(Enrollment enrollment) {
        try {
            HttpURLConnection conn = API.connectAPI("enrollments/" + enrollment.getId());
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
                SceneController.showAlert(Alert.AlertType.INFORMATION, "Success", "Enrollment deleted successfully");
            }
            else {
                SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Enrollment> getEnrollmentsByUserId(String userId) {
        ObservableList<Enrollment> enrollments = FXCollections.observableArrayList();
        try {
            HttpURLConnection conn = API.connectAPI("enrollments/" + userId);
            assert conn != null;
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + UserService.token);
            conn.setRequestProperty("Content-Type", "application/json");
            int responseCode = conn.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
                try(BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    Gson gson = new Gson();
                    JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
                    JsonArray jsonArray = jsonResponse.has("enrollments") ? jsonResponse.getAsJsonArray("enrollments") : new JsonArray();
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

                    for(JsonElement jsonElement : jsonArray) {
                        JsonObject jsonEnrollment = jsonElement.getAsJsonObject();
                        String id = jsonEnrollment.has("_id") ? jsonEnrollment.get("_id").getAsString() : "";
                        String studentId = jsonEnrollment.has("studentId") ? jsonEnrollment.get("studentId").getAsString() : "";
                        String courseId = jsonEnrollment.has("courseId") ? jsonEnrollment.get("courseId").getAsString() : "";
                        OffsetDateTime enrolledDateODT = jsonEnrollment.has("enrolledDate") ? OffsetDateTime.parse(jsonEnrollment.get("enrolledDate").getAsString(), dateFormatter) : OffsetDateTime.now();
                        Date enrolledDate = Date.from(enrolledDateODT.toInstant());
                        JsonObject jsonProgression = jsonEnrollment.has("progression") ? jsonEnrollment.getAsJsonObject("progression") : null;
                        Progression progression = null;

                        if(jsonProgression != null) {
                            String progressionId = jsonProgression.has("_id") ? jsonProgression.get("_id").getAsString() : "";
                            ArrayList<String> lessonsFinish = new ArrayList<>();
                            JsonArray lessonsArray = jsonProgression.has("lessonsFinish") ? jsonProgression.getAsJsonArray("lessonsFinish") : new JsonArray();

                            for(JsonElement lesson : lessonsArray) {
                                lessonsFinish.add(lesson.getAsString());
                            }

                            ArrayList<String> exercisesSuccess = new ArrayList<>();
                            JsonArray exercisesArray = jsonProgression.has("exercicesSuccess") ? jsonProgression.getAsJsonArray("exercicesSuccess") : new JsonArray();

                            for(JsonElement exercise : exercisesArray) {
                                exercisesSuccess.add(exercise.getAsString());
                            }

                            int attempts = jsonProgression.get("attempts").getAsInt();
                            progression = new Progression(progressionId, lessonsFinish, exercisesSuccess, attempts);
                        }
                        Enrollment enrollment = new Enrollment(id, studentId, courseId, enrolledDate, progression);
                        enrollments.add(enrollment);
                    }
                }
            }
            else {
                SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return enrollments;
    }
}
