package com.example.prn231.DTO;

public class Member {
    private String userId;
    private String email;
    private String fullName;

    // Constructor, Getters, and Setters
    public Member(String email, String fullName) {

        this.email = email;
        this.fullName = fullName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }
}