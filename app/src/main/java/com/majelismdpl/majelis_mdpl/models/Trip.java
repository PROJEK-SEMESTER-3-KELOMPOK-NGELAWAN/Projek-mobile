package com.majelismdpl.majelis_mdpl.models;

public class Trip {
    String namaGunung;
    String tanggal;
    int peserta;    // <-- Tipe datanya 'int'
    String status;
    double rating;  // <-- Tipe datanya 'double'
    String imageUrl;

    // Constructor
    public Trip(String namaGunung, String tanggal, int peserta, String status, double rating, String imageUrl) {
        this.namaGunung = namaGunung;
        this.tanggal = tanggal;
        this.peserta = peserta;
        this.status = status;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    // --- GETTERS (Ini yang penting untuk adapter & filter) ---

    public String getNamaGunung() {
        return namaGunung;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getStatus() {
        return status;
    }

    // ==========================================
    // TAMBAHKAN DUA METODE DI BAWAH INI
    // ==========================================

    /**
     * Getter untuk jumlah peserta.
     * @return Tipe data int
     */
    public int getPeserta() {
        return peserta;
    }

    /**
     * Getter untuk rating.
     * @return Tipe data double
     */
    public double getRating() {
        return rating;
    }

    // (Opsional) Getter untuk URL gambar
    public String getImageUrl() {
        return imageUrl;
    }
}