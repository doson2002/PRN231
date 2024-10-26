package com.example.prn231.DTO;

import java.util.List;

public class DaySlot {

    private String date;
    private List<Slot> slotList;

    public DaySlot(String date, List<Slot> slotList) {
        this.date = date;
        this.slotList = slotList;
    }

    public String getDate() {
        return date;
    }

    public List<Slot> getSlotList() {
        return slotList;
    }
}