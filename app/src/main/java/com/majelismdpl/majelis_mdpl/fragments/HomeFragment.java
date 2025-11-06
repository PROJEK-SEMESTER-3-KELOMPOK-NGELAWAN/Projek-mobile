package com.majelismdpl.majelis_mdpl.fragments;

// (BARU) Import untuk Izin, Lokasi, dan SMS
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
// (DIHAPUS) import android.telephony.SmsManager;
// (BARU) Import untuk ActivityResultLauncher (Izin modern)
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
// (BARU) Import untuk Cek Izin
import androidx.core.content.ContextCompat;

// (BARU) Import untuk Google Play Services Location
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

// Import yang sudah ada
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.majelismdpl.majelis_mdpl.R;

import com.majelismdpl.majelis_mdpl.activities.PesertaActivity;
import com.majelismdpl.majelis_mdpl.activities.DokumentasiActivity;
import com.majelismdpl.majelis_mdpl.activities.MeetingPoint;

// (BARU) Import ini SEKARANG DIPERLUKAN untuk WhatsApp
import java.net.URLEncoder;
// (BARU) Import untuk Map (dibutuhkan oleh ActivityResultLauncher)
import java.util.Map;


public class HomeFragment extends Fragment {

    // --- (BARU) Variabel untuk Logika SOS + Lokasi ---

    // Klien untuk mendapatkan lokasi
    private FusedLocationProviderClient fusedLocationClient;
    // Launcher untuk meminta izin
    private ActivityResultLauncher<String[]> requestMultiplePermissionsLauncher;

    // (DIUBAH) GANTI DENGAN NOMOR TUJUAN WHATSAPP (Format 62...)
    private static final String NOMOR_WHATSAPP_TUJUAN_SOS = "6283853493130";

    // Variabel untuk menyimpan data pengguna, diambil dari TextView
    private String namaPengguna = "Peserta";
    private String namaTrip = "Trip";
    // ----------------------------------------------

    public HomeFragment() {
        // Required empty public constructor
    }

    // --- (BARU) Method onCreate ditambahkan ---
    // Diperlukan untuk inisialisasi FusedLocationProviderClient dan ActivityResultLauncher
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pastikan context tidak null
        if (getActivity() == null) return;

        // Inisialisasi FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Inisialisasi launcher untuk meminta izin
        requestMultiplePermissionsLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                (Map<String, Boolean> permissions) -> {
                    // Cek apakah semua izin diberikan
                    // (DIUBAH) Izin SMS tidak dicek lagi
                    boolean fineLocationGranted = Boolean.TRUE.equals(permissions.get(Manifest.permission.ACCESS_FINE_LOCATION));
                    boolean coarseLocationGranted = Boolean.TRUE.equals(permissions.get(Manifest.permission.ACCESS_COARSE_LOCATION));

                    if (fineLocationGranted && coarseLocationGranted) {
                        // Jika IZIN LOKASI diberikan, ulangi proses untuk mendapatkan lokasi
                        Toast.makeText(getContext(), "Izin lokasi diberikan. Mengambil lokasi...", Toast.LENGTH_SHORT).show();
                        // (DIUBAH) Panggil fungsi WhatsApp
                        getCurrentLocationAndSendWhatsApp();
                    } else {
                        // Jika izin lokasi ditolak
                        Toast.makeText(getContext(), "Fitur SOS tidak dapat berjalan tanpa izin Lokasi.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Ini sudah benar, tidak diubah
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // --- Inisialisasi Data Statis (Sesuai XML Baru) ---
        TextView tvTripTitle = view.findViewById(R.id.tv_trip_title);
        TextView tvTripDate = view.findViewById(R.id.tv_trip_date);
        ImageView ivTripImage = view.findViewById(R.id.iv_trip_image);

        TextView tvUsername = view.findViewById(R.id.tv_username);
        TextView tvStatus = view.findViewById(R.id.tv_status_pembayaran);

        // Set data statis
        if (tvTripTitle != null) {
            tvTripTitle.setText("Pendakian Gunung Ijen");
            // (BARU) Simpan nama trip ke variabel class
            this.namaTrip = tvTripTitle.getText().toString();
        }
        if (tvTripDate != null) {
            tvTripDate.setText("12-13 Agustus 2025");
        }
        if (ivTripImage != null) {
            ivTripImage.setImageResource(R.drawable.ic_gunung_ijen);
        }
        if (tvUsername != null) {
            tvUsername.setText("Alex Morgan"); // Anda bisa ganti dengan data user
            // (BARU) Simpan nama pengguna ke variabel class
            this.namaPengguna = tvUsername.getText().toString();
        }
        if (tvStatus != null) {
            tvStatus.setText("Lunas"); // Anda bisa ganti dengan data status
        }


        // --- Listener Grup WhatsApp (Tidak Diubah) ---
        MaterialCardView menuGrupWhatsapp = view.findViewById(R.id.menu_grup_whatsapp);
        String urlGrupWhatsApp = "https://chat.whatsapp.com/Fi47AjpM3mT7wIUc3kg4Vy?mode=wwt";

        if (menuGrupWhatsapp != null) {
            menuGrupWhatsapp.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urlGrupWhatsApp));
                    startActivity(intent);
                } catch (Exception e) {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Tidak dapat membuka link", Toast.LENGTH_SHORT).show();
                    }
                    e.printStackTrace();
                }
            });
        }

        // --- Listener Titik Kumpul (Tidak Diubah) ---
        MaterialCardView menuTitikKumpul = view.findViewById(R.id.menu_titik_kumpul);
        if (menuTitikKumpul != null) {
            menuTitikKumpul.setOnClickListener(v -> {
                if (getContext() == null) return;
                Intent intent = new Intent(getContext(), MeetingPoint.class);
                startActivity(intent);
            });
        }

        // --- Listener Peserta Trip (Tidak Diubah) ---
        MaterialCardView menuPesertaTrip = view.findViewById(R.id.menu_peserta_trip);
        if (menuPesertaTrip != null) {
            menuPesertaTrip.setOnClickListener(v -> {
                if (getContext() == null) return;
                Intent intent = new Intent(getContext(), PesertaActivity.class);
                startActivity(intent);
            });
        }

        // --- Listener Dokumentasi (Tidak Diubah) ---
        MaterialCardView menuDokumentasi = view.findViewById(R.id.menu_dokumentasi);
        if (menuDokumentasi != null) {
            menuDokumentasi.setOnClickListener(v -> {
                if (getContext() == null) return;
                Intent intent = new Intent(getContext(), DokumentasiActivity.class);
                startActivity(intent);
            });
        }

        // --- (DIUBAH) Listener Tombol SOS (Sekarang menggunakan SMS + Lokasi) ---
        MaterialButton tombolSOS = view.findViewById(R.id.btn_sos);
        if (tombolSOS != null) {
            tombolSOS.setOnClickListener(v -> {
                // (BARU) Panggil fungsi untuk cek izin dan memulai proses SOS
                // Pastikan data nama pengguna dan trip sudah ter-update
                if (tvUsername != null) {
                    this.namaPengguna = tvUsername.getText().toString();
                }
                if (tvTripTitle != null) {
                    this.namaTrip = tvTripTitle.getText().toString();
                }

                checkPermissionsAndStartSos();
            });
        }
    }


    // --- (BARU) SEMUA METHOD DI BAWAH INI DITAMBAHKAN UNTUK LOGIKA SOS ---

    /**
     * Langkah 1: Cek Izin
     * Memeriksa apakah izin SMS dan Lokasi sudah diberikan.
     */
    private void checkPermissionsAndStartSos() {
        if (getContext() == null) return; // Pastikan fragment masih ter-attach

        // (DIUBAH) Pengecekan izin SMS dihapus
        boolean fineLocationPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean coarseLocationPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (fineLocationPermission && coarseLocationPermission) {
            // Jika izin LOKASI sudah ada, langsung ke langkah 2
            // (DIUBAH) Panggil fungsi WhatsApp
            getCurrentLocationAndSendWhatsApp();
        } else {
            // Jika izin lokasi belum ada, minta izin ke pengguna
            // (DIUBAH) Hanya minta izin Lokasi
            requestMultiplePermissionsLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    /**
     * (DIUBAH) Langkah 2: Dapatkan Lokasi Terkini (untuk WhatsApp)
     * Menggunakan FusedLocationProviderClient untuk mendapatkan lokasi paling update.
     */
    private void getCurrentLocationAndSendWhatsApp() {

        // Cek lagi (wajib) karena fungsi ini butuh izin
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getContext(), "Kesalahan: Izin lokasi tidak ada.", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getContext(), "Mencari lokasi terkini...", Toast.LENGTH_SHORT).show();

        // Kita gunakan getCurrentLocation untuk akurasi terbaik saat itu juga
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        // LOKASI BERHASIL DIDAPATKAN
                        // (DIUBAH) Langsung ke Langkah 3: Kirim WhatsApp
                        sendWhatsAppWithLocation(location.getLatitude(), location.getLongitude());
                    } else {
                        // Lokasi null, mungkin GPS mati atau di dalam gedung
                        // Kita coba minta update lokasi manual
                        requestLocationUpdates();
                    }
                })
                .addOnFailureListener(requireActivity(), e -> {
                    Toast.makeText(getContext(), "Gagal mendapatkan lokasi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    /**
     * (Cadangan) Jika getCurrentLocation gagal, minta update lokasi secara aktif.
     */
    private void requestLocationUpdates() {
        if (getContext() == null) return;

        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000) // 5 detik
                .setWaitForAccurateLocation(true)
                .setMinUpdateIntervalMillis(1000) // 1 detik
                .setMaxUpdateDelayMillis(10000) // 10 detik
                .build();

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                // Hentikan update setelah dapat lokasi pertama
                fusedLocationClient.removeLocationUpdates(this);

                if (locationResult.getLastLocation() != null) {
                    Location location = locationResult.getLastLocation();
                    // LOKASI BERHASIL DIDAPATKAN
                    // (DIUBAH) Langsung ke Langkah 3: Kirim WhatsApp
                    sendWhatsAppWithLocation(location.getLatitude(), location.getLongitude());
                } else {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Tidak bisa mendapatkan lokasi. Pastikan GPS aktif.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        // Cek izin sekali lagi
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }


    /**
     * (DIUBAH) Langkah 3: Kirim Pesan WhatsApp dengan menyertakan Lokasi
     * Membuat format pesan dan membukanya di WhatsApp.
     */
    private void sendWhatsAppWithLocation(double latitude, double longitude) {
        if (getContext() == null) return;

        // Buat link Google Maps
        String mapsLink = "https://maps.google.com/maps?q=" + latitude + "," + longitude;

        // Buat pesan lengkap (menggunakan variabel namaPengguna dan namaTrip dari class)
        String message = "SOS! SAYA DALAM BAHAYA.\n\n" +
                "Nama: " + this.namaPengguna + "\n" +
                "Trip: " + this.namaTrip + "\n\n" +
                "LOKASI SAYA SAAT INI:\n" +
                mapsLink + "\n\n" +
                "Mohon segera kirimkan bantuan!";

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // Encode pesan agar bisa dibaca oleh URL
            String url = "https://wa.me/" + NOMOR_WHATSAPP_TUJUAN_SOS + "?text=" + URLEncoder.encode(message, "UTF-8");
            intent.setData(Uri.parse(url));
            // Coba paksa buka dengan aplikasi WhatsApp
            intent.setPackage("com.whatsapp");
            startActivity(intent);
            Toast.makeText(getContext(), "Membuka WhatsApp...", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            // Jika WhatsApp tidak ter-install, akan ada error
            if (getContext() != null) {
                Toast.makeText(getContext(), "WhatsApp tidak terpasang.", Toast.LENGTH_SHORT).show();
            }
            e.printStackTrace();
            // (Fallback) Coba buka link di browser jika WhatsApp gagal
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                String url = "https://wa.me/" + NOMOR_WHATSAPP_TUJUAN_SOS + "?text=" + URLEncoder.encode(message, "UTF-8");
                browserIntent.setData(Uri.parse(url));
                startActivity(browserIntent);
            } catch (Exception e2) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Tidak dapat membuka aplikasi.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}