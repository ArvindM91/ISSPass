package com.own.isspasses.models;

import com.google.gson.annotations.SerializedName;

public class PassDetailsModel {
    private int duration;

    @SerializedName("risetime")
    private long timeStamp;


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
