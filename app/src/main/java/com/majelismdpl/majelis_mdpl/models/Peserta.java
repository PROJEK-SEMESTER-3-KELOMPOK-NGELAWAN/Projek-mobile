// Lokasi: com/majelismdpl/majelis_mdpl/models/Peserta.java
package com.majelismdpl.majelis_mdpl.models;

import java.util.Objects;

public class Peserta {
    private String id;
    private String nama;
    private String email;
    private String status;
    private String avatarUrl; // <-- 1. TAMBAHKAN FIELD INI

    public Peserta() {
        // Constructor kosong
    }

    // 2. PERBARUI CONSTRUCTOR (sekarang butuh 5 argumen)
    public Peserta(String id, String nama, String email, String status, String avatarUrl) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.status = status;
        this.avatarUrl = avatarUrl; // <-- TAMBAHKAN INI
    }

    // (Getters yang sudah ada)
    public String getId() { return id; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
    public String getStatus() { return status; }

    // 3. INI PERBAIKANNYA: Tambahkan getter ini
    public String getAvatarUrl() {
        return avatarUrl;
    }

    // (Opsional: Setter)
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Peserta peserta = (Peserta) o;
        // Perbandingan utama cukup berdasarkan ID
        return Objects.equals(id, peserta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}