package com.majelismdpl.majelis_mdpl.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TripParticipantsHistoryResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<PesertaHistory> data;
    @SerializedName("total_participants")
    private int totalParticipants;
    @SerializedName("trip_status")
    private String tripStatus;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<PesertaHistory> getData() { return data; }
    public int getTotalParticipants() { return totalParticipants; }
    public String getTripStatus() { return tripStatus; }
}
