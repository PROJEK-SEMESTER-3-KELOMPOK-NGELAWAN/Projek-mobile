package com.majelismdpl.majelis_mdpl.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TripHistoryResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<TripHistoryItem> data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<TripHistoryItem> getData() { return data; }
}
