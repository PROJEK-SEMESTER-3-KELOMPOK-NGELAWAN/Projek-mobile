package com.majelismdpl.majelis_mdpl.models;

import com.google.gson.annotations.SerializedName;

public class GetUserTripResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Trip data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Trip getData() { return data; }
}
