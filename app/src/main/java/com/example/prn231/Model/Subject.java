package com.example.prn231.Model;

public class Subject {
    public String id;
    public String name;

    public Subject(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getGroupId() {
        return id;
    }

    public void setGroupId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
