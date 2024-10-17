package com.example.prn231.Model;

import java.util.List;

public class Mentor {

    private String id;
    private String fullName;
    private String email;
    private int point;
    private String createdOnUtc;
    private List<String> skills;

    // Constructor
    public Mentor(String id, String fullName, String email, int point, String createdOnUtc, List<String> skills) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.point = point;
        this.createdOnUtc = createdOnUtc;
        this.skills = skills;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getCreatedOnUtc() {
        return createdOnUtc;
    }

    public void setCreatedOnUtc(String createdOnUtc) {
        this.createdOnUtc = createdOnUtc;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}

