package com.majelismdpl.majelis_mdpl.models;

import com.google.gson.annotations.SerializedName;

public class TripHistoryItem {
    @SerializedName("id_booking")
    private int idBooking;
    @SerializedName("id_trip")
    private int idTrip;
    @SerializedName("mountain_name")
    private String mountainName;
    @SerializedName("date")
    private String date;
    @SerializedName("duration")
    private String duration;
    @SerializedName("participants")
    private int participants;
    @SerializedName("status")
    private String status;
    @SerializedName("rating")
    private double rating;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("jenis_trip")
    private String jenisTrip;
    @SerializedName("total_harga")
    private int totalHarga;
    @SerializedName("slot")
    private int slot;

    public int getIdBooking() { return idBooking; }
    public int getIdTrip() { return idTrip; }
    public String getMountainName() { return mountainName; }
    public String getDate() { return date; }
    public String getDuration() { return duration; }
    public int getParticipants() { return participants; }
    public String getStatus() { return status; }
    public double getRating() { return rating; }
    public String getImageUrl() { return imageUrl; }
    public String getJenisTrip() { return jenisTrip; }
    public int getTotalHarga() { return totalHarga; }
    public int getSlot() { return slot; }
}
