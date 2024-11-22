package com.example.academyproject.utils;

import io.github.cdimascio.dotenv.Dotenv;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class API {
    public static HttpURLConnection connectAPI(String route) {
        Dotenv dotenv = Dotenv.load();

        try {
            String apiUrl = dotenv.get("BASE_URL") + route;
            URL url = new URI(apiUrl).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            return conn;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
