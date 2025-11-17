package com.majelismdpl.majelis_mdpl.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TripParticipantsResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<Peserta> data;

    @SerializedName("total_participants")
    private int totalParticipants;

    // Constructor
    public TripParticipantsResponse() {
    }

    public TripParticipantsResponse(boolean success, String message, List<Peserta> data, int totalParticipants) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.totalParticipants = totalParticipants;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Peserta> getData() {
        return data;
    }

    public void setData(List<Peserta> data) {
        this.data = data;
    }

    public int getTotalParticipants() {
        return totalParticipants;
    }

    public void setTotalParticipants(int totalParticipants) {
        this.totalParticipants = totalParticipants;
    }
}
