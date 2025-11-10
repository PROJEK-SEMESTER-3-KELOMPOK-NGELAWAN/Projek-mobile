package com.majelismdpl.majelis_mdpl.models;

import com.google.gson.annotations.SerializedName;

public class MeetingPoint {
    @SerializedName("id_trip")
    private int idTrip;

    @SerializedName("nama_gunung")
    private String namaGunung;

    @SerializedName("tanggal_trip")
    private String tanggalTrip;

    @SerializedName("waktu_kumpul")
    private String waktuKumpul;

    @SerializedName("nama_lokasi")
    private String namaLokasi;

    @SerializedName("alamat_lokasi")
    private String alamatLokasi;

    @SerializedName("link_map")
    private String linkMap;

    @SerializedName("informasi_tambahan")
    private String informasiTambahan;

    // Konstruktor kosong diperlukan untuk Gson
    public MeetingPoint() {}

    public int getIdTrip() { return idTrip; }
    public void setIdTrip(int idTrip) { this.idTrip = idTrip; }

    public String getNamaGunung() { return namaGunung; }
    public void setNamaGunung(String namaGunung) { this.namaGunung = namaGunung; }

    public String getTanggalTrip() { return tanggalTrip; }
    public void setTanggalTrip(String tanggalTrip) { this.tanggalTrip = tanggalTrip; }

    public String getWaktuKumpul() { return waktuKumpul; }
    public void setWaktuKumpul(String waktuKumpul) { this.waktuKumpul = waktuKumpul; }

    public String getNamaLokasi() { return namaLokasi; }
    public void setNamaLokasi(String namaLokasi) { this.namaLokasi = namaLokasi; }

    public String getAlamatLokasi() { return alamatLokasi; }
    public void setAlamatLokasi(String alamatLokasi) { this.alamatLokasi = alamatLokasi; }

    public String getLinkMap() { return linkMap; }
    public void setLinkMap(String linkMap) { this.linkMap = linkMap; }

    public String getInformasiTambahan() { return informasiTambahan; }
    public void setInformasiTambahan(String informasiTambahan) { this.informasiTambahan = informasiTambahan; }
}
