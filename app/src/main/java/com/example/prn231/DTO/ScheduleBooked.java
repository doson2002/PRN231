package com.example.prn231.DTO;

public class ScheduleBooked {
    private String id;
    private String groupId;
    private String date;
    private String startTime;
    private String endTime;
    private String groupName;
    private boolean isFeedBack;
    private int isAccepted;

    public ScheduleBooked() {
    }

    public ScheduleBooked(String id,String date, String startTime, String endTime, String groupName, boolean isFeedBack, int isAccepted) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.groupName = groupName;
        this.isFeedBack = isFeedBack;
        this.isAccepted = isAccepted;
    }

    public int getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(int isAccepted) {
        this.isAccepted = isAccepted;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
