// Lokasi: com/majelismdpl/majelis_mdpl/models/Peserta.java
package com.majelismdpl.majelis_mdpl.models;

import java.util.Objects;

public class Peserta {
    private String id;
    private String nama;
    private String email;
    private String status;
    private String avatarUrl;
    private String nomorWa; // <-- 1. TAMBAHKAN FIELD INI

    public Peserta() {
        // Constructor kosong
    }

    // 2. PERBARUI CONSTRUCTOR (sekarang butuh 6 argumen)
    public Peserta(String id, String nama, String email, String status, String avatarUrl, String nomorWa) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.status = status;
        this.avatarUrl = avatarUrl;
        this.nomorWa = nomorWa; // <-- TAMBAHKAN INI
    }

    // (Getters yang sudah ada)
    public String getId() { return id; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
    public String getStatus() { return status; }
    public String getAvatarUrl() { return avatarUrl; }

    // 3. INI METHOD YANG ANDA MINTA
    public String getNomorWa() {
        return nomorWa;
    }

    // (Setter)
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    // 4. (Opsional tapi disarankan) Tambahkan setter untuk nomorWa
    public void setNomorWa(String nomorWa) {
        this.nomorWa = nomorWa;
    }


    // 5. PERBARUI EQUALS() (Penting untuk DiffUtil)
    //    'areContentsTheSame' akan menggunakan ini untuk membandingkan semua data.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Peserta peserta = (Peserta) o;
        return Objects.equals(id, peserta.id) &&
                Objects.equals(nama, peserta.nama) &&
                Objects.equals(email, peserta.email) &&
                Objects.equals(status, peserta.status) &&
                Objects.equals(avatarUrl, peserta.avatarUrl) &&
                Objects.equals(nomorWa, peserta.nomorWa); // <-- TAMBAHKAN INI
    }

    // 6. PERBARUI HASHCODE() (Penting untuk DiffUtil)
    @Override
    public int hashCode() {
        // Gunakan semua field yang ada di equals()
        return Objects.hash(id, nama, email, status, avatarUrl, nomorWa); // <-- PERBARUI INI
    }
}