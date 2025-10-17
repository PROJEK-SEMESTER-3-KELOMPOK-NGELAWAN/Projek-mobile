package com.majelismdpl.majelis_mdpl.models;

public class Destination {
    private String name;
    private String location;
    private int imageResId; // Menggunakan int untuk ID drawable

    public Destination(String name, String location, int imageResId) {
        this.name = name;
        this.location = location;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getImageResId() {
        return imageResId;
    }
}
