package com.example.prn231.DTO;

public class SkillMentor {
    public String id;
    public String skillName;
    public String skillDescription;
    public String skillCategoryType;

    public SkillMentor() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public void setSkillDescription(String skillDescription) {
        this.skillDescription = skillDescription;
    }

    public String getSkillCategoryType() {
        return skillCategoryType;
    }

    public void setSkillCategoryType(String skillCategoryType) {
        this.skillCategoryType = skillCategoryType;
    }
    @Override
    public String toString() {
        return skillName; // Trả về tên của skill
    }
}
