package com.example.academyproject.models;

import java.util.Objects;

public class Comment {
    public String id;
    public String userId;
    public String courseId;
    public String content;
    public String createdAt;

    public Comment(String id, String userId, String courseId, String content, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + this.id + '\'' +
                ", userId='" + this.userId + '\'' +
                ", courseId='" + this.courseId + '\'' +
                ", content='" + this.content + '\'' +
                ", createdAt='" + this.createdAt + '\'' +
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
        Comment comment = (Comment) object;
        return Objects.equals(this.id, comment.id) && Objects.equals(this.userId, comment.userId) && Objects.equals(this.courseId, comment.courseId) && Objects.equals(this.content, comment.content) && Objects.equals(this.createdAt, comment.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.userId, this.courseId, this.content, this.createdAt);
    }
}
