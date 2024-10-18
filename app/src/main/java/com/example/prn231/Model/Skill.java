package com.example.prn231.Model;

import java.util.List;

public class Skill {
    public String skillName;
    public String skillDesciption;
    public String skillCategoryType;
    public String createdOnUtc;
    public List<Certificate> cetificates;
    // Getters and Setters


    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillDesciption() {
        return skillDesciption;
    }

    public void setSkillDesciption(String skillDesciption) {
        this.skillDesciption = skillDesciption;
    }

    public String getSkillCategoryType() {
        return skillCategoryType;
    }

    public void setSkillCategoryType(String skillCategoryType) {
        this.skillCategoryType = skillCategoryType;
    }

    public String getCreatedOnUtc() {
        return createdOnUtc;
    }

    public void setCreatedOnUtc(String createdOnUtc) {
        this.createdOnUtc = createdOnUtc;
    }

    public List<Certificate> getCetificates() {
        return cetificates;
    }

    public void setCetificates(List<Certificate> cetificates) {
        this.cetificates = cetificates;
    }
}
