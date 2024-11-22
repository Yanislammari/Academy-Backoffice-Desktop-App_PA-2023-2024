package com.example.academyproject.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Course {
    private String id;
    private String title;
    private String description;
    private ArrayList<Lesson> lessons;
    private CourseLevel level;
    private ArrayList<Exercise> execises;
    private int rating;
    private String teacherId;
    private Date createdAt;
    private String imageUrl;

    public Course(String id, String title, String description, ArrayList<Lesson> lessons, CourseLevel level, ArrayList<Exercise> execises, int rating, String teacherId, Date createdAt, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.lessons = lessons;
        this.level = level;
        this.execises = execises;
        this.rating = rating;
        this.teacherId = teacherId;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Lesson> getLessons() {
        return this.lessons;
    }

    public void setLessons(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }

    public CourseLevel getLevel() {
        return this.level;
    }

    public void setLevel(CourseLevel level) {
        this.level = level;
    }

    public ArrayList<Exercise> getExecises() {
        return this.execises;
    }

    public void setExecises(ArrayList<Exercise> execises) {
        this.execises = execises;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTeacherId() {
        return this.teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id='" + this.id + '\'' +
                ", title='" + this.title + '\'' +
                ", description='" + this.description + '\'' +
                ", lessons=" + this.lessons +
                ", level=" + this.level +
                ", execises=" + this.execises +
                ", rating=" + this.rating +
                ", teacherId='" + this.teacherId + '\'' +
                ", createdAt=" + this.createdAt +
                ", imageUrl=" + this.imageUrl +
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
        Course course = (Course) object;
        return this.rating == course.rating && Objects.equals(this.id, course.id) && Objects.equals(this.title, course.title) && Objects.equals(this.description, course.description) && Objects.equals(this.lessons, course.lessons) && this.level == course.level && Objects.equals(this.execises, course.execises) && Objects.equals(this.teacherId, course.teacherId) && Objects.equals(this.createdAt, course.createdAt) && Objects.equals(this.imageUrl, course.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.title, this.description, this.lessons, this.level, this.execises, this.rating, this.teacherId, this.createdAt, this.imageUrl);
    }
}
