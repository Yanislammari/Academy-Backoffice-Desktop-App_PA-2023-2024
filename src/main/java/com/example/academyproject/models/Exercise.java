package com.example.academyproject.models;

import java.util.ArrayList;
import java.util.Objects;

public class Exercise {
    public String id;
    public String idCourse;
    public String title;
    public String description;
    public ExerciseType type;
    public ExerciseDifficulty difficulty;
    public ArrayList<String> options;
    public String correctOption;
    public String textWithBlanks;
    public ArrayList<String> correctAnswers;

    public Exercise(String id, String idCourse, String title, String description, ExerciseType type, ExerciseDifficulty difficulty, ArrayList<String> options, String correctOption, String textWithBlanks, ArrayList<String> correctAnswers) {
        this.id = id;
        this.idCourse = idCourse;
        this.title = title;
        this.description = description;
        this.type = type;
        this.difficulty = difficulty;
        this.options = options;
        this.correctOption = correctOption;
        this.textWithBlanks = textWithBlanks;
        this.correctAnswers = correctAnswers;
    }

    public Exercise(String id, String idCourse, String title, String description, ExerciseType type, ExerciseDifficulty difficulty, ArrayList<String> options, String correctOption) {
        this.id = id;
        this.idCourse = idCourse;
        this.title = title;
        this.description = description;
        this.type = type;
        this.difficulty = difficulty;
        this.options = options;
        this.correctOption = correctOption;
    }

    public Exercise(String id, String idCourse, String title, String description, ExerciseType type, ExerciseDifficulty difficulty, String textWithBlanks, ArrayList<String> correctAnswers) {
        this.id = id;
        this.idCourse = idCourse;
        this.title = title;
        this.description = description;
        this.type = type;
        this.difficulty = difficulty;
        this.textWithBlanks = textWithBlanks;
        this.correctAnswers = correctAnswers;
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

    public void setIdCourse(String idCourse) {
        this.idCourse = idCourse;
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

    public ExerciseType getType() {
        return this.type;
    }

    public void setType(ExerciseType type) {
        this.type = type;
    }

    public ExerciseDifficulty getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(ExerciseDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public ArrayList<String> getOptions() {
        return this.options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public String getCorrectOption() {
        return this.correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public String getTextWithBlanks() {
        return this.textWithBlanks;
    }

    public void setTextWithBlanks(String textWithBlanks) {
        this.textWithBlanks = textWithBlanks;
    }

    public ArrayList<String> getCorrectAnswers() {
        return this.correctAnswers;
    }

    public void setCorrectAnswers(ArrayList<String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    @Override
    public String toString() {
        return "Execise{" +
                "id='" + this.id + '\'' +
                ", idCourse='" + this.idCourse + '\'' +
                ", title='" + this.title + '\'' +
                ", description='" + this.description + '\'' +
                ", type=" + this.type +
                ", difficulty=" + this.difficulty +
                ", options=" + this.options +
                ", correctOption='" + this.correctOption + '\'' +
                ", textWithBlanks='" + this.textWithBlanks + '\'' +
                ", correctAnswers=" + this.correctAnswers +
                '}';
    }

    @Override
    public boolean equals(Object object ){
        if(this == object) {
            return true;
        }
        if(object == null || getClass() != object.getClass()) {
            return false;
        }
        Exercise execise = (Exercise) object;
        return Objects.equals(this.id, execise.id) && Objects.equals(this.idCourse, execise.idCourse) && Objects.equals(this.title, execise.title) && Objects.equals(this.description, execise.description) && this.type == execise.type && this.difficulty == execise.difficulty && Objects.equals(this.options, execise.options) && Objects.equals(this.correctOption, execise.correctOption) && Objects.equals(this.textWithBlanks, execise.textWithBlanks) && Objects.equals(this.correctAnswers, execise.correctAnswers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.idCourse, this.title, this.description, this.type, this.difficulty, this.options, this.correctOption, this.textWithBlanks, this.correctAnswers);
    }
}
