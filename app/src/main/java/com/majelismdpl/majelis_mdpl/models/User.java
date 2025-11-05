package com.majelismdpl.majelis_mdpl.models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    private int id;

    @SerializedName("nama")
    private String nama;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    // --- PERBAIKAN 1 ---
    // Diubah dari 'no_hp' ke 'whatsapp' agar konsisten dengan EditProfileActivity
    // Pastikan @SerializedName("...") cocok dengan JSON key dari API Anda
    @SerializedName("whatsapp") // <-- Pastikan ini nama key di JSON Anda (atau "no_hp")
    private String whatsapp;

    @SerializedName("alamat")
    private String alamat;

    @SerializedName("foto_url")
    private String foto_url;

    // --- PERBAIKAN 2 ---
    // Menambahkan field 'password' yang dibutuhkan EditProfileActivity
    @SerializedName("password")
    private String password;


    // Constructor kosong (diperlukan untuk Gson)
    public User() {
    }

    // --- GETTERS (Untuk Membaca Data) ---

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getFotoUrl() {
        return foto_url;
    }

    // --- PERBAIKAN 1 (lanjutan) ---
    public String getWhatsapp() { // <-- Diubah dari getNoHp()
        return whatsapp;
    }

    // --- PERBAIKAN 2 (lanjutan) ---
    public String getPassword() { // <-- Method baru
        return password;
    }


    // --- PERBAIKAN 3: MENAMBAHKAN SETTERS ---
    // (Ini adalah inti masalah dari error Anda)
    // Dibutuhkan oleh EditProfileActivity untuk menyimpan perubahan

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWhatsapp(String whatsapp) { // <-- Method baru
        this.whatsapp = whatsapp;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setPassword(String password) { // <-- Method baru
        this.password = password;
    }
}