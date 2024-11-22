package com.example.academyproject.controllers;

import com.example.academyproject.SceneController;
import com.example.academyproject.models.User;
import com.example.academyproject.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.List;
import java.util.Properties;
import io.github.cdimascio.dotenv.Dotenv;

public class FeedbackViewController {
    @FXML
    private ComboBox<String> emailComboBox;
    @FXML
    private TextField subjectTextField;
    @FXML
    private TextArea bodyTextArea;

    @FXML
    public void initialize() {
        List<User> users = UserService.getUsers();
        ObservableList<String> emails = FXCollections.observableArrayList();

        for(User user : users) {
            emails.add(user.getEmail());
        }
        this.emailComboBox.setItems(emails);
    }

    @FXML
    private void sendEmailAction() {
        String selectedEmail = this.emailComboBox.getValue();
        String subject = this.subjectTextField.getText();
        String body = this.bodyTextArea.getText();

        if(selectedEmail == null || subject.isEmpty() || body.isEmpty()) {
            SceneController.showAlert(AlertType.ERROR, "Error", "All fields must be filled in.");
            return;
        }
        try {
            this.sendEmail(selectedEmail, subject, body);
            SceneController.showAlert(AlertType.INFORMATION, "Success", "Email sent successfully.");
        }
        catch(Exception e) {
            SceneController.showAlert(AlertType.ERROR, "Error", "Failed to send the email.");
            e.printStackTrace();
        }
    }

    private void sendEmail(String recipientEmail, String subject, String body) throws Exception {
        Dotenv dotenv = Dotenv.load();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(dotenv.get("MAIL_NAME"), dotenv.get("MAIL_PASSWORD"));
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(dotenv.get("MAIL_NAME")));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(subject);
        message.setText(body);
        Transport.send(message);
    }
}
