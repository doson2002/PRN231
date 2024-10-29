package com.example.prn231.Model;

import com.google.gson.annotations.SerializedName;

// MentorSlotRequestBody.java
public class MentorSlotRequestBody {
    @SerializedName("slotId")
    private String slotId;

    @SerializedName("subjectId")
    private String subjectId;

    @SerializedName("groupId")
    private String groupId;

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("endTime")
    private String endTime;

    public MentorSlotRequestBody(String slotId, String subjectId, String groupId, String startTime, String endTime) {
        this.slotId = slotId;
        this.subjectId = subjectId;
        this.groupId = groupId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    // Getters and setters (optional if using Gson/Retrofit to serialize/deserialize)
    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
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
}
