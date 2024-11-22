module com.example.academyproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires java.desktop;
    requires javafx.swing;
    requires javafx.media;
    requires jakarta.mail;
    requires java.dotenv;


    opens com.example.academyproject to javafx.fxml;
    exports com.example.academyproject;
    exports com.example.academyproject.controllers;
    opens com.example.academyproject.controllers to javafx.fxml, com.google.gson;
    exports com.example.academyproject.models;
    opens com.example.academyproject.models to javafx.fxml;
    exports com.example.academyproject.utils;
    opens com.example.academyproject.utils to javafx.fxml;
    exports com.example.academyproject.utils.tablecells;
    opens com.example.academyproject.utils.tablecells to com.google.gson, javafx.fxml;
    exports com.example.academyproject.services;
    opens com.example.academyproject.services to com.google.gson, javafx.fxml;
}