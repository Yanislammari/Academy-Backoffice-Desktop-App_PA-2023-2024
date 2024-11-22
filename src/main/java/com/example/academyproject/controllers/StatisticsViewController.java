package com.example.academyproject.controllers;

import com.example.academyproject.models.Enrollment;
import com.example.academyproject.services.EnrollmentService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class StatisticsViewController implements Initializable {
    @FXML
    private LineChart<String, Number> enrollmentsLineChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Enrollment> enrollments = EnrollmentService.getEnrollments();
        Map<String, Integer> enrollmentsByDate = new HashMap<>();

        for(Enrollment enrollment : enrollments) {
            String dateStr = dateFormat.format(enrollment.getEnrolledDate());
            enrollmentsByDate.put(dateStr, enrollmentsByDate.getOrDefault(dateStr, 0) + 1);
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Enrollments");

        for(Map.Entry<String, Integer> entry : enrollmentsByDate.entrySet()){
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        this.enrollmentsLineChart.getData().add(series);
    }
}
