package com.example.academyproject.models;

import java.util.ArrayList;
import java.util.Objects;

public class Progression {
    private String id;
    private ArrayList<String> lessonsFinish;
    private ArrayList<String> exercisesSuccess;
    private int attempts;

    public Progression(String id, ArrayList<String> lessonsFinish, ArrayList<String> exercisesSuccess, int attempts) {
        this.id = id;
        this.lessonsFinish = lessonsFinish;
        this.exercisesSuccess = exercisesSuccess;
        this.attempts = attempts;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getLessonsFinish() {
        return this.lessonsFinish;
    }

    public void setLessonsFinish(ArrayList<String> lessonsFinish) {
        this.lessonsFinish = lessonsFinish;
    }

    public ArrayList<String> getExercisesSuccess() {
        return this.exercisesSuccess;
    }

    public void setExercisesSuccess(ArrayList<String> exercisesSuccess) {
        this.exercisesSuccess = exercisesSuccess;
    }

    public int getAttempts() {
        return this.attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    @Override
    public String toString() {
        return "Progression{" +
                "id='" + this.id + '\'' +
                ", lessonsFinish=" + this.lessonsFinish +
                ", exercisesSuccess=" + this.exercisesSuccess +
                ", attempts =" + this.attempts +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Progression progression = (Progression) object;
        return Objects.equals(this.id, progression.id) && Objects.equals(this.lessonsFinish, progression.lessonsFinish) && Objects.equals(this.exercisesSuccess, progression.exercisesSuccess) && Objects.equals(this.attempts, progression.attempts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.lessonsFinish, this.exercisesSuccess, this.attempts);
    }
}
