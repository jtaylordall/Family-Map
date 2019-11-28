package com.example.familymap.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class RegisterRequest implements Serializable {
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;

    public RegisterRequest(String userName, String password, String email,
                           String firstName, String lastName, String gender,
                           String fatherID, String motherID, String spouseID) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public String getMotherID() {
        return motherID;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    @Override
    @NonNull
    public String toString() {
        return "username: " + userName +
                "\npassword: " + password +
                "\nfirst: " + firstName +
                "\nlast: " + lastName +
                "\nemail: " + email +
                "\ngender: " + gender;
    }
}
