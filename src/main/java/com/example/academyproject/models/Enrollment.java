package com.example.academyproject.models;

import java.util.Date;
import java.util.Objects;

public class Enrollment {
    private String id;
    private String studentId;
    private String courseId;
    private Date enrolledDate;
    private Progression progression;

    public Enrollment(String id, String studentId, String courseId, Date enrolledDate, Progression progression) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrolledDate = enrolledDate;
        this.progression = progression;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Date getEnrolledDate() {
        return this.enrolledDate;
    }

    public void setEnrolledDate(Date enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    public Progression getProgression() {
        return this.progression;
    }

    public void setProgression(Progression progression) {
        this.progression = progression;
    }

    @Override
    public String toString() {
        return "Enrollement{" +
                "id='" + this.id + '\'' +
                ", studentId='" + this.studentId + '\'' +
                ", courseId='" + this.courseId + '\'' +
                ", enrolledDate=" + this.enrolledDate +
                ", progression=" + this.progression +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if(this == object) {
            return true;
        }
        if(object == null || getClass() != object.getClass()) {
            return false;
        }
        Enrollment enrollment = (Enrollment) object;
        return Objects.equals(this.id, enrollment.id) && Objects.equals(this.studentId, enrollment.studentId) && Objects.equals(this.courseId, enrollment.courseId) && Objects.equals(this.enrolledDate, enrollment.enrolledDate) && Objects.equals(this.progression, enrollment.progression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.studentId, this.courseId, this.enrolledDate, this.progression);
    }
}
