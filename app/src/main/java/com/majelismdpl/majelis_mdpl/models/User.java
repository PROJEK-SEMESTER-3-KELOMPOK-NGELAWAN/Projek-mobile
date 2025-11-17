package com.majelismdpl.majelis_mdpl.models;

import com.google.gson.annotations.SerializedName;

public class User {

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

    @SerializedName("foto_url")
    private String foto_url;

    @SerializedName("password")
    private String password;

    @SerializedName("role")
    private String role;

    public User() {}

    // Getters
    public int getId() { return idUser; } // Untuk kompatibilitas legacy
    public int getIdUser() { return idUser; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getAlamat() { return alamat; }
    public String getFotoUrl() { return foto_url; }
    public String getWhatsapp() { return whatsapp; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    // Setters
    public void setId(int idUser) { this.idUser = idUser; } // Untuk kompatibilitas legacy
    public void setIdUser(int idUser) { this.idUser = idUser; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public void setPassword(String password) { this.password = password; }
    public void setFotoUrl(String foto_url) { this.foto_url = foto_url; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
