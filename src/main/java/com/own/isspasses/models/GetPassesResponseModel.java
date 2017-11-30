package com.own.isspasses.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetPassesResponseModel {

    private String message;

    @SerializedName("response")
    private ArrayList<PassDetailsModel> passListResponse;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<PassDetailsModel> getPassListResponse() {
        return passListResponse;
    }

    public void setPassListResponse(ArrayList<PassDetailsModel> passListResponse) {
        this.passListResponse = passListResponse;
    }
}
