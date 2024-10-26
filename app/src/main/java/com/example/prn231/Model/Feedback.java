package com.example.prn231.Model;

public class Feedback {
    private String groupId;
    private String content;
    private String scheduleId;
    private int rating;

    public Feedback(String groupId, String content, String scheduleId, int rating) {
        this.groupId = groupId;
        this.content = content;
        this.scheduleId = scheduleId;
        this.rating = rating;
    }
}