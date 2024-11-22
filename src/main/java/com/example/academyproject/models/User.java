package com.example.academyproject.models;

import java.util.Objects;

public class User {
    String id;
    String firstName;
    String lastName;
    String email;
    Role role;
    String profilePicture;

    public User(String id, String firstName, String lastName, String email, Role role, String profilePicture) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.profilePicture = profilePicture;
    }

    public User(String firstName, String lastName, String email, Role role, String profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.profilePicture = profilePicture;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getProfilePicture() {
        return this.profilePicture;
    }

    public void setProfilePicture(String profilePicture)  {
        this.profilePicture = profilePicture;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + this.id +
                ", firstName='" + this.firstName + '\'' +
                ", lastName='" + this.lastName + '\'' +
                ", email='" + this.email + '\'' +
                ", role=" + this.role +
                ", profilePicture='" + this.profilePicture + '\'' +
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
        User user = (User) object;
        return Objects.equals(this.id, user.id) && Objects.equals(this.firstName, user.firstName) && Objects.equals(this.lastName, user.lastName) && Objects.equals(this.email, user.email) && this.role == user.role && Objects.equals(this.profilePicture, user.profilePicture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.firstName, this.lastName, this.email, this.role, this.profilePicture);
    }
}
