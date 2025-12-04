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

    // URL lengkap foto profil dari server
    @SerializedName("foto_url")
    private String fotoUrl;

    // Path relatif foto profil (fallback)
    @SerializedName("foto_profil")
    private String fotoProfil;

    @SerializedName("password")
    private String password;

    @SerializedName("role")
    private String role;

    public User() {}

    // ===== GETTER =====
    public int getId() {
        return idUser;
    }

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

    public String getFotoUrl() {
        return fotoUrl;
    }

    public String getFotoProfil() {
        return fotoProfil;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    /**
     * Helper method untuk mendapatkan URL foto yang valid
     * Prioritas: foto_url > foto_profil
     */
    public String getValidPhotoUrl() {
        if (fotoUrl != null && !fotoUrl.isEmpty()) {
            return fotoUrl;
        }
        return fotoProfil;
    }

    // ===== SETTER =====
    public void setId(int idUser) {
        this.idUser = idUser;
    }

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

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public void setFotoProfil(String fotoProfil) {
        this.fotoProfil = fotoProfil;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", fotoUrl='" + fotoUrl + '\'' +
                '}';
    }
}
