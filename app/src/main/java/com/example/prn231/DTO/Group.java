package com.example.prn231.DTO;

import java.util.List;

public class Group {
    private String id;
    private String name;
    private String stack;
    private String mentorName;
    private String leaderName;
    private String projectName;
    private List<Member> members;

    // Constructor, Getters, and Setters
    public Group(String id, String name, String mentorName, String leaderName, String projectName, List<Member> members) {
        this.id = id;
        this.name = name;
        this.mentorName = mentorName;
        this.leaderName = leaderName;
        this.projectName = projectName;
        this.members = members;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStack() {
        return stack;
    }

    public String getMentorName() {
        return mentorName;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public String getProjectName() {
        return projectName;
    }

    public List<Member> getMembers() {
        return members;
    }
}