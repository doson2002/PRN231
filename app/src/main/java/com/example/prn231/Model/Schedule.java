package com.example.prn231.Model;

public class Schedule {
    public String id;
    public String startTime;
    public String endTime;
    public String date;
    public int month;
    public String note;
    public Boolean isOnline;
    public Boolean isBook;

    public Schedule(String id, String startTime, String endTime, String date, int month, String note, Boolean isOnline, Boolean isBooked) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.month = month;
        this.note = note;
        this.isOnline = isOnline;
        this.isBook = isBooked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    public Boolean getBooked() {
        return isBook;
    }

    public void setBooked(Boolean booked) {
        isBook = booked;
    }
}
