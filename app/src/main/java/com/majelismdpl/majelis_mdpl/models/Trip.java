package com.majelismdpl.majelis_mdpl.models;

public class Trip {
    private String mountainName;
    private String date;
    private int participants;
    private String status;
    private double rating;
    private String imageUrl;

    public Trip(String mountainName, String date, int participants, String status, double rating, String imageUrl) {
        this.mountainName = mountainName;
        this.date = date;
        this.participants = participants;
        this.status = status;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    // Getter methods
    public String getMountainName() {
        return mountainName;
    }

    public String getDate() {
        return date;
    }

    public int getParticipants() {
        return participants;
    }

    public String getStatus() {
        return status;
    }

    public double getRating() {
        return rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
