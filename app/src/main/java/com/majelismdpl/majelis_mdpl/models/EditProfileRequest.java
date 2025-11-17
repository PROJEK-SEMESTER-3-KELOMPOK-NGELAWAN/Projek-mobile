package com.majelismdpl.majelis_mdpl.models;

import com.google.gson.annotations.SerializedName;

/**
 * ============================================
 * Edit Profile Request Model
 * Model untuk request update profil user
 * ============================================
 */
public class EditProfileRequest {

    @SerializedName("id_user")
    private int idUser;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("whatsapp")
    private String whatsapp;

    @SerializedName("alamat")
    private String alamat;

    @SerializedName("password")
    private String password;

    // Constructor
    public EditProfileRequest(int idUser, String username, String email,
                              String whatsapp, String alamat, String password) {
        this.idUser = idUser;
        this.username = username;
        this.email = email;
        this.whatsapp = whatsapp;
        this.alamat = alamat;
        this.password = password;
    }

    // Getters
    public int getIdUser() {
        return idUser;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
