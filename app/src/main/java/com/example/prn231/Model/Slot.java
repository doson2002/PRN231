package com.example.prn231.Model;

public class Slot {
    private String id;
    private String startTime;
    private String endTime;
    private String date;
    private boolean isOnline;
    private String note;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public boolean isOnline() { return isOnline; }
    public void setOnline(boolean online) { isOnline = online; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}