package com.example.academyproject.services;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.Comment;
import com.example.academyproject.utils.API;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class CommentService {
    public static ObservableList<Comment> getComments() {
        ObservableList<Comment> comments = FXCollections.observableArrayList();
        try {
            HttpURLConnection conn = API.connectAPI("comments");
            assert conn != null;
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + UserService.token);
            conn.setRequestProperty("Content-Type", "application/json");
            int responseCode = conn.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
                try(BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    Gson gson = new Gson();
                    JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
                    JsonArray jsonArray = jsonResponse.getAsJsonArray("comments");

                    for(int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonComment = jsonArray.get(i).getAsJsonObject();
                        String id = jsonComment.get("_id").getAsString();
                        String userId = jsonComment.get("userId").getAsString();
                        String courseId = jsonComment.get("courseId").getAsString();
                        String content = jsonComment.get("content").getAsString();
                        String createdAt = jsonComment.get("createdAt").getAsString();
                        comments.add(new Comment(id, userId, courseId, content, createdAt));
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
        return comments;
    }

    public static void deleteComment(String commentId) {
        try {
            HttpURLConnection conn = API.connectAPI("comments/" + commentId);
            assert conn != null;
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "Bearer " + UserService.token);
            conn.setRequestProperty("Content-Type", "application/json");
            int responseCode = conn.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                SceneController.showAlert(Alert.AlertType.INFORMATION, "Success", "Comment deleted successfully");
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
