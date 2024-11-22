package com.example.academyproject.services;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.Role;
import com.example.academyproject.models.User;
import com.example.academyproject.utils.API;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.StringBody;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import org.apache.hc.core5.http.HttpEntity;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class UserService {
    public static User userConnected;
    public static String token;

    public static String connectUser(String emailInput, String passwordInput) {
        try {
            HttpURLConnection conn = API.connectAPI("auth/login");
            assert conn != null;
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            Gson gson = new Gson();
            JsonObject json = new JsonObject();
            json.addProperty("email", emailInput);
            json.addProperty("password", passwordInput);
            String jsonString = gson.toJson(json);

            try(DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.writeBytes(jsonString);
                wr.flush();
            }
            int responseCode = conn.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
                try(BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
                    return jsonResponse.get("token").getAsString();
                }
            }
            else {
                return null;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void registerUser(String firstName, String lastName, String email, String password, String role, Image profilePicture) {
        Dotenv dotenv = Dotenv.load();

        try {
            File tempFile = new File("profile_picture.png");
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(profilePicture, null);
            ImageIO.write(bufferedImage, "png", tempFile);
            HttpPost post = new HttpPost(dotenv.get("BASE_URL") + "auth/register");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            builder.setMode(HttpMultipartMode.STRICT);
            builder.addPart("firstName", new StringBody(firstName, ContentType.TEXT_PLAIN));
            builder.addPart("lastName", new StringBody(lastName, ContentType.TEXT_PLAIN));
            builder.addPart("email", new StringBody(email, ContentType.TEXT_PLAIN));
            builder.addPart("password", new StringBody(password, ContentType.TEXT_PLAIN));
            builder.addPart("role", new StringBody(role, ContentType.TEXT_PLAIN));
            builder.addPart("profilePicture", new FileBody(tempFile, ContentType.IMAGE_PNG));

            post.setEntity(builder.build());
            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(post);
            try {
                int statusCode = response.getCode();
                if(statusCode == HttpURLConnection.HTTP_OK) {
                    SceneController.showAlert(Alert.AlertType.INFORMATION, "Success", "User registered successfully");
                }
                else {
                    SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
                    HttpEntity entity = response.getEntity();
                    if(entity != null){
                        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                        StringBuilder responseBody = new StringBuilder();
                        String line;
                        while((line = reader.readLine()) != null){
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

    public static User tokenToUser(String token) {
        try {
            HttpURLConnection conn = API.connectAPI("auth");
            assert conn != null;
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token);
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

                    String id = jsonResponse.get("_id").getAsString();
                    String firstName = jsonResponse.get("firstName").getAsString();
                    String lastName = jsonResponse.get("lastName").getAsString();
                    String email = jsonResponse.get("email").getAsString();
                    Role role = Role.valueOf(jsonResponse.get("role").getAsString().toUpperCase());
                    String profilePicture = jsonResponse.has("profilePicture") ? jsonResponse.get("profilePicture").getAsString() : null;

                    return new User(id, firstName, lastName, email, role, profilePicture);
                }
            }
            else {
                return null;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ObservableList<User> getUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        try {
            HttpURLConnection conn = API.connectAPI("users");
            assert conn != null;
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token);
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
                    JsonArray jsonArray = gson.fromJson(response.toString(), JsonArray.class);

                    for(int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonUser = jsonArray.get(i).getAsJsonObject();
                        String id = jsonUser.get("_id").getAsString();
                        String firstName = jsonUser.get("firstName").getAsString();
                        String lastName = jsonUser.get("lastName").getAsString();
                        String email = jsonUser.get("email").getAsString();
                        String profilePicture = jsonUser.has("profilePicture") ? jsonUser.get("profilePicture").getAsString() : null;  // Récupération de la photo de profil
                        Role role = Role.valueOf(jsonUser.get("role").getAsString().toUpperCase());
                        users.add(new User(id, firstName, lastName, email, role, profilePicture));
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
        return users;
    }

    public static void deleteUser(User user) {
        try {
            HttpURLConnection conn = API.connectAPI("users/" + user.getId());
            assert conn != null;
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "Bearer " + token);
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
                SceneController.showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully");
            }
            else {
                SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error Server");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static User getUserById(String id) {
        try {
            HttpURLConnection conn = API.connectAPI("users/" + id);
            assert conn != null;
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token);
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
                    JsonElement jsonElement = gson.fromJson(response.toString(), JsonElement.class);
                    if(jsonElement.isJsonArray()) {
                        JsonArray jsonArray = jsonElement.getAsJsonArray();

                        for(int i = 0; i < jsonArray.size(); i++) {
                            JsonObject jsonUser = jsonArray.get(i).getAsJsonObject();
                            if(jsonUser.get("_id").getAsString().equals(id)) {
                                return parseUser(jsonUser);
                            }
                        }
                        SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error JSON");
                    }
                    else if(jsonElement.isJsonObject()) {
                        JsonObject jsonUser = jsonElement.getAsJsonObject();
                        if(jsonUser.get("_id").getAsString().equals(id)) {
                            return parseUser(jsonUser);
                        }
                    }
                    else {
                        SceneController.showAlert(Alert.AlertType.ERROR, "Error", "Error JSON");
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
        return null;
    }

    private static User parseUser(JsonObject jsonUser) {
        String id = jsonUser.get("_id").getAsString();
        String firstName = jsonUser.get("firstName").getAsString();
        String lastName = jsonUser.get("lastName").getAsString();
        String email = jsonUser.get("email").getAsString();
        String profilePicture = jsonUser.has("profilePicture") ? jsonUser.get("profilePicture").getAsString() : null;
        Role role = Role.valueOf(jsonUser.get("role").getAsString().toUpperCase());
        return new User(id, firstName, lastName, email, role, profilePicture);
    }

    public static void editUser(String id, String firstName, String lastName, String email, String password, String role, Image profilePicture) {
        Dotenv dotenv = Dotenv.load();

        try {
            File tempFile = new File("profile_picture.png");
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(profilePicture, null);
            ImageIO.write(bufferedImage, "png", tempFile);
            HttpPut put = new HttpPut(dotenv.get("BASE_URL") + "users/" + id);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            builder.setMode(HttpMultipartMode.STRICT);
            builder.addTextBody("firstName", firstName, ContentType.TEXT_PLAIN);
            builder.addTextBody("lastName", lastName, ContentType.TEXT_PLAIN);
            builder.addTextBody("email", email, ContentType.TEXT_PLAIN);
            builder.addTextBody("password", password, ContentType.TEXT_PLAIN);
            builder.addTextBody("role", role, ContentType.TEXT_PLAIN);
            builder.addBinaryBody("profilePicture", tempFile, ContentType.IMAGE_PNG, tempFile.getName());

            put.setEntity(builder.build());
            put.setHeader("Authorization", "Bearer " + token);
            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(put);
            try {
                int statusCode = response.getCode();
                if(statusCode == HttpURLConnection.HTTP_OK) {
                    SceneController.showAlert(Alert.AlertType.INFORMATION, "Success", "User updated successfully");
                }
                else {
                    HttpEntity entity = response.getEntity();
                    if(entity != null) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                        StringBuilder responseBody = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
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
