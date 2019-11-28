package com.example.familymap.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class LoginRequest implements Serializable {
    private String userName;
    private String password;

    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    @NonNull
    public String toString() {
        return "username: " + userName +
                "\npassword: " + password;
    }
}
