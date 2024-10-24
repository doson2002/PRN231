package com.example.prn231.DTO;

public class ScheduleBooked {
    private String date;
    private String startTime;
    private String endTime;
    private String groupName;
    private boolean isFeedBack;

    public ScheduleBooked() {
    }

    public ScheduleBooked(String date, String startTime, String endTime, String groupName, boolean isFeedBack) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.groupName = groupName;
        this.isFeedBack = isFeedBack;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isFeedBack() {
        return isFeedBack;
    }

    public void setFeedBack(boolean feedBack) {
        isFeedBack = feedBack;
    }
}
