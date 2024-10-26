package com.example.prn231.DTO;

public class Slot {

    private String startTime;
    private String endTime;
    private String date;
    private boolean isOnline;
    private String note;
    private int month;
    private boolean isBook;

    // Constructor, getter và setter
    public Slot(String startTime, String endTime, String date, boolean isOnline, String note, int month, boolean isBook) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.isOnline = isOnline;
        this.note = note;
        this.month = month;
        this.isBook = isBook;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDate() {
        return date;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public String getNote() {
        return note;
    }

    public int getMonth() {
        return month;
    }

    public boolean isBook() {
        return isBook;
    }
}
