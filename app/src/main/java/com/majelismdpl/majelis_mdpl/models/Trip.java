// File: models/Trip.java
package com.majelismdpl.majelis_mdpl.models;

import java.util.Objects;

public class Trip {
    private String id; // <-- 1. TAMBAHKAN FIELD ID
    private String mountainName;
    private String date;
    private int participants;
    private String status;
    private double rating;
    private String imageUrl;

    // (Constructor kosong jika perlu)
    public Trip() {}

    // 2. PERBARUI CONSTRUCTOR untuk menerima ID
    public Trip(String id, String mountainName, String date, int participants, String status, double rating, String imageUrl) {
        this.id = id;
        this.mountainName = mountainName;
        this.date = date;
        this.participants = participants;
        this.status = status;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    // 3. TAMBAHKAN GETTER UNTUK ID
    public String getId() {
        return id;
    }

    // (Getters Anda yang lain)
    public String getMountainName() { return mountainName; }
    public String getDate() { return date; }
    public int getParticipants() { return participants; }
    public String getStatus() { return status; }
    public double getRating() { return rating; }
    public String getImageUrl() { return imageUrl; }

    // (Equals & HashCode jika ada, pastikan 'id' disertakan)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Objects.equals(id, trip.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}