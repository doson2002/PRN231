package com.example.prn231.Model;

public class Schedule {
    public String id;
    public String start;
    public String end;
    public int type;
    public Boolean isBooked;

    public Schedule(String id, String start, String end, int type, Boolean isBooked) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.type = type;
        this.isBooked = isBooked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Boolean getBooked() {
        return isBooked;
    }

    public void setBooked(Boolean booked) {
        isBooked = booked;
    }
}
