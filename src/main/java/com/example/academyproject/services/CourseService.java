package com.example.academyproject.services;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.*;
import com.example.academyproject.utils.API;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class CourseService {
    public static ObservableList<Course> getCourses() {
        ObservableList<Course> courses = FXCollections.observableArrayList();
        try {
            HttpURLConnection conn = API.connectAPI("courses");
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
                    JsonArray jsonArray = jsonResponse.has("courses") ? jsonResponse.getAsJsonArray("courses") : new JsonArray();
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

                    for(JsonElement jsonElement : jsonArray) {
                        JsonObject jsonCourse = jsonElement.getAsJsonObject();
                        String id = jsonCourse.has("_id") ? jsonCourse.get("_id").getAsString() : "";
                        String title = jsonCourse.has("title") ? jsonCourse.get("title").getAsString() : "";
                        String description = jsonCourse.has("description") ? jsonCourse.get("description").getAsString() : "";
                        String imageUrl = jsonCourse.has("imageUrl") ? jsonCourse.get("imageUrl").getAsString() : "";
                        JsonArray lessonsArray = jsonCourse.has("lessons") ? jsonCourse.getAsJsonArray("lessons") : new JsonArray();
                        ArrayList<Lesson> lessons = new ArrayList<>();

                        for(JsonElement lessonElement : lessonsArray) {
                            Lesson lesson = gson.fromJson(lessonElement, Lesson.class);
                            lesson.setIdCourse(id);
                            lessons.add(lesson);
                        }

                        CourseLevel level = jsonCourse.has("level") ? CourseLevel.valueOf(jsonCourse.get("level").getAsString().toUpperCase()) : CourseLevel.BEGINNER;
                        JsonArray exercisesArray = jsonCourse.has("exercises") ? jsonCourse.getAsJsonArray("exercises") : new JsonArray();
                        ArrayList<Exercise> exercises = new ArrayList<>();

                        for(JsonElement exerciseElement : exercisesArray) {
                            Exercise exercise = gson.fromJson(exerciseElement, Exercise.class);
                            exercise.setIdCourse(id);
                            exercises.add(exercise);
                        }

                        int rating = jsonCourse.has("rating") ? jsonCourse.get("rating").getAsInt() : 0;
                        String teacherId = jsonCourse.has("teacherID") ? jsonCourse.get("teacherID").getAsString() : "";
                        OffsetDateTime createdAt = jsonCourse.has("createdAt") ? OffsetDateTime.parse(jsonCourse.get("createdAt").getAsString(), dateFormatter) : OffsetDateTime.now();
                        Course course = new Course(id, title, description, lessons, level, exercises, rating, teacherId, Date.from(createdAt.toInstant()), imageUrl);
                        courses.add(course);
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
        return courses;
    }


    public static Course getCourseById(String courseId) {
        Course course = null;
        try {
            HttpURLConnection conn = API.connectAPI("courses/" + courseId);
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
                    JsonObject jsonCourse = jsonResponse.getAsJsonObject("course");
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

                    String id = jsonCourse.has("_id") ? jsonCourse.get("_id").getAsString() : "";
                    String title = jsonCourse.has("title") ? jsonCourse.get("title").getAsString() : "";
                    String description = jsonCourse.has("description") ? jsonCourse.get("description").getAsString() : "";
                    String imageUrl = jsonCourse.has("imageUrl") ? jsonCourse.get("imageUrl").getAsString() : "";
                    JsonArray lessonsArray = jsonCourse.has("lessons") ? jsonCourse.getAsJsonArray("lessons") : new JsonArray();
                    ArrayList<Lesson> lessons = new ArrayList<>();

                    for(JsonElement lessonElement : lessonsArray) {
                        Lesson lesson = gson.fromJson(lessonElement, Lesson.class);
                        lesson.setIdCourse(id);
                        lessons.add(lesson);
                    }

                    CourseLevel level = jsonCourse.has("level") ? CourseLevel.valueOf(jsonCourse.get("level").getAsString().toUpperCase()) : CourseLevel.BEGINNER;
                    JsonArray exercisesArray = jsonCourse.has("exercises") ? jsonCourse.getAsJsonArray("exercises") : new JsonArray();
                    ArrayList<Exercise> exercises = new ArrayList<>();

                    for(JsonElement exerciseElement : exercisesArray) {
                        Exercise exercise = gson.fromJson(exerciseElement, Exercise.class);
                        exercises.add(exercise);
                    }

                    int rating = jsonCourse.has("rating") ? jsonCourse.get("rating").getAsInt() : 0;
                    String teacherId = jsonCourse.has("teacherID") ? jsonCourse.get("teacherID").getAsString() : "";
                    OffsetDateTime createdAt = jsonCourse.has("createdAt") ? OffsetDateTime.parse(jsonCourse.get("createdAt").getAsString(), dateFormatter) : OffsetDateTime.now();
                    course = new Course(id, title, description, lessons, level, exercises, rating, teacherId, Date.from(createdAt.toInstant()), imageUrl);
                }
            }
            else {
                SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return course;
    }

    public static void deleteCourse(Course course) {
        try {
            HttpURLConnection conn = API.connectAPI("courses/delete/" + course.getId());
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
                SceneController.showAlert(Alert.AlertType.INFORMATION, "Success", "Course deleted successfully");
            }
            else {
                SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void editCourse(String id, String title, String description, String level, Image imageUrl) {
        Dotenv dotenv = Dotenv.load();

        try {
            File tempFile = new File("course_image.png");
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageUrl, null);
            ImageIO.write(bufferedImage, "png", tempFile);
            HttpPut put = new HttpPut(dotenv.get("BASE_URL") + "courses/update/" + id);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            builder.setMode(HttpMultipartMode.STRICT);
            builder.addTextBody("title", title, ContentType.TEXT_PLAIN);
            builder.addTextBody("description", description, ContentType.TEXT_PLAIN);
            builder.addTextBody("level", level, ContentType.TEXT_PLAIN);
            builder.addBinaryBody("imageUrl", tempFile, ContentType.IMAGE_PNG, tempFile.getName());

            put.setEntity(builder.build());
            put.setHeader("Authorization", "Bearer " + UserService.token);
            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(put);
            try {
                int statusCode = response.getCode();
                if(statusCode == HttpURLConnection.HTTP_OK) {
                    SceneController.showAlert(Alert.AlertType.INFORMATION, "Success", "Course updated successfully");
                }
                else {
                    SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
                    HttpEntity entity = response.getEntity();
                    if(entity != null) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                        StringBuilder responseBody = new StringBuilder();
                        String line;
                        while((line = reader.readLine()) != null) {
                            responseBody.append(line);
                        }
                        SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
                    }
                }
            }
            finally {
                response.close();
                client.close();
                tempFile.delete();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void addCourse(String teacherId, String title, String description, String level, Image imageUrl) {
        Dotenv dotenv = Dotenv.load();

        try {
            File tempFile = new File("course_image.png");
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageUrl, null);
            ImageIO.write(bufferedImage, "png", tempFile);
            HttpPost post = new HttpPost(dotenv.get("BASE_URL") + "courses/createCourse/" + teacherId);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            builder.setMode(HttpMultipartMode.STRICT);
            builder.addTextBody("title", title, ContentType.TEXT_PLAIN);
            builder.addTextBody("description", description, ContentType.TEXT_PLAIN);
            builder.addTextBody("level", level, ContentType.TEXT_PLAIN);
            builder.addBinaryBody("imageUrl", tempFile, ContentType.IMAGE_PNG, tempFile.getName());

            post.setEntity(builder.build());
            post.setHeader("Authorization", "Bearer " + UserService.token);
            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(post);
            try {
                int statusCode = response.getCode();
                if(statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_CREATED) {
                    SceneController.showAlert(Alert.AlertType.INFORMATION, "Success", "Course added successfully");
                    HttpEntity entity = response.getEntity();
                    if(entity != null) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                        StringBuilder responseBody = new StringBuilder();
                        String line;
                        while((line = reader.readLine()) != null) {
                            responseBody.append(line);
                        }
                        SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
                    }
                }
                else {
                    SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
                    HttpEntity entity = response.getEntity();
                    if(entity != null) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                        StringBuilder responseBody = new StringBuilder();
                        String line;
                        while((line = reader.readLine()) != null) {
                            responseBody.append(line);
                        }
                        SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
                    }
                }
            }
            finally {
                response.close();
                client.close();
                tempFile.delete();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
