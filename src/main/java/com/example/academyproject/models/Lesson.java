package com.example.academyproject.models;

import java.util.Objects;

public class Lesson {
    public String id;
    public String idCourse;
    public String title;
    public int number;
    public String video;
    public String description;

    public Lesson(String id, String idCourse, String title, int number, String video, String description) {
        this.id = id;
        this.idCourse = idCourse;
        this.title = title;
        this.number = number;
        this.video = video;
        this.description = description;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCourse() {
        return this.idCourse;
    }

    public void setIdCourse(String id) {
        this.idCourse = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getVideo() {
        return this.video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id='" + this.id + '\'' +
                ", title='" + this.title + '\'' +
                ", number=" + this.number +
                ", video='" + this.video + '\'' +
                ", description='" + this.description + '\'' +
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
        Lesson lesson = (Lesson) object;
        return this.number == lesson.number && Objects.equals(this.id, lesson.id) && Objects.equals(this.title, lesson.title) && Objects.equals(this.video, lesson.video) && Objects.equals(this.description, lesson.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.title, this.number, this.video, this.description);
    }
}
