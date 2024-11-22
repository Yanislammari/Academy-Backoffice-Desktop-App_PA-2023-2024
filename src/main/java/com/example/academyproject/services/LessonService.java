package com.example.academyproject.services;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.Course;
import com.example.academyproject.models.Lesson;
import com.example.academyproject.utils.API;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class LessonService {
    public static ObservableList<Lesson> getAllLessons(ObservableList<Course> allCourse) {
        ArrayList<Lesson> lessons = new ArrayList<>();

        for(Course course : allCourse){
            lessons.addAll(course.getLessons());
        }
        return FXCollections.observableArrayList(lessons);
    }

    public static void deleteLesson(Lesson lesson) {
        try {
            HttpURLConnection conn = API.connectAPI("courses/deleteLesson/" + lesson.getIdCourse() + "/" + lesson.getId());
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
                SceneController.showAlert(Alert.AlertType.INFORMATION, "Success", "Lesson deleted successfully");
            }
            else {
                SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void addLesson(String courseId, File videoFile, String lessonTitle, String lessonDescription) {
        Dotenv dotenv = Dotenv.load();

        try {
            HttpPost post = new HttpPost(dotenv.get("BASE_URL") + "courses/addLesson/" + courseId);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("title", lessonTitle, ContentType.TEXT_PLAIN);
            builder.addTextBody("description", lessonDescription, ContentType.TEXT_PLAIN);
            builder.addBinaryBody("video", videoFile, ContentType.APPLICATION_OCTET_STREAM, videoFile.getName());
            builder.addTextBody("number", "0", ContentType.TEXT_PLAIN);

            post.setEntity(builder.build());
            post.setHeader("Authorization", "Bearer " + UserService.token);
            try(CloseableHttpClient client = HttpClients.createDefault();

            var response = client.execute(post)) {
                int statusCode = response.getCode();

                if(statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_CREATED) {
                    SceneController.showAlert(Alert.AlertType.INFORMATION, "Success", "Lesson added successfully");
                    HttpEntity entity = response.getEntity();
                    if(entity != null) {
                        try(BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()))){
                            StringBuilder responseBody = new StringBuilder();
                            String line;
                            while((line = reader.readLine()) != null) {
                                responseBody.append(line);
                            }
                            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
                        }
                    }
                }
                else {
                    SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
                    HttpEntity entity = response.getEntity();
                    if(entity != null) {
                        try(BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()))) {
                            StringBuilder responseBody = new StringBuilder();
                            String line;
                            while((line = reader.readLine()) != null) {
                                responseBody.append(line);
                            }
                            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
                        }
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateLesson(String courseId, String lessonId, String lessonTitle, String lessonDescription, File videoFile) {
        Dotenv dotenv = Dotenv.load();

        try {
            HttpPut put = new HttpPut(dotenv.get("BASE_URL") + "courses/updateLesson/" + courseId + "/" + lessonId);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.STRICT);
            builder.addTextBody("title", lessonTitle, ContentType.TEXT_PLAIN);
            builder.addTextBody("description", lessonDescription, ContentType.TEXT_PLAIN);
            builder.addTextBody("number", "0", ContentType.TEXT_PLAIN);

            if(videoFile != null && videoFile.exists()) {
                builder.addBinaryBody("video", videoFile, ContentType.APPLICATION_OCTET_STREAM, videoFile.getName());
            }
            put.setEntity(builder.build());
            put.setHeader("Authorization", "Bearer " + UserService.token);
            try(CloseableHttpClient client = HttpClients.createDefault();

            CloseableHttpResponse response = client.execute(put)) {
                int statusCode = response.getCode();

                if(statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_NO_CONTENT) {
                    System.out.println("Lesson updated successfully");
                }
                else {
                    SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
                    HttpEntity entity = response.getEntity();
                    if(entity != null) {
                        try(BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()))) {
                            StringBuilder responseBody = new StringBuilder();
                            String line;
                            while((line = reader.readLine()) != null) {
                                responseBody.append(line);
                            }
                            SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
                        }
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
