package com.majelismdpl.majelis_mdpl.fragments;

// Import yang perlu ditambahkan
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// Import baru untuk MaterialButton
import com.google.android.material.button.MaterialButton;
import com.majelismdpl.majelis_mdpl.R;

// (BARU) Import ini diaktifkan agar tombol Peserta & Dokumentasi berfungsi
import com.majelismdpl.majelis_mdpl.activities.PesertaActivity;
import com.majelismdpl.majelis_mdpl.activities.DokumentasiActivity;


public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // --- Inisialisasi Data Statis (Kode Anda yang sudah ada) ---
        TextView tvTripTitle = view.findViewById(R.id.tv_trip_title);
        TextView tvTripDate = view.findViewById(R.id.tv_trip_date);
        ImageView ivTripImage = view.findViewById(R.id.iv_trip_image);

        if (tvTripTitle != null) {
            tvTripTitle.setText("Pendakian Gunung Ijen");
        }
        if (tvTripDate != null) {
            tvTripDate.setText("12-13 Agustus 2025");
        }
        if (ivTripImage != null) {
            ivTripImage.setImageResource(R.drawable.ic_gunung_ijen);
        }


        // --- Listener Grup WhatsApp (Kode Anda yang sudah ada) ---
        RelativeLayout menuGrupWhatsapp = view.findViewById(R.id.menu_grup_whatsapp);

        // (DIUBAH) Link WhatsApp sudah diganti sesuai permintaan Anda
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

        // --- (BARU) Listener Titik Kumpul (Buka Google Maps) ---
        RelativeLayout menuTitikKumpul = view.findViewById(R.id.menu_titik_kumpul);
        if (menuTitikKumpul != null) {
            menuTitikKumpul.setOnClickListener(v -> {
                // GANTI KOORDINAT: (Contoh: Kawah Ijen)
                String latitude = "-8.0583";
                String longitude = "114.2434";
                String label = "Titik Kumpul Ijen";

                String uri = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(" + label + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps"); // Coba paksa buka di Google Maps

                try {
                    startActivity(intent);
                } catch (Exception e) {
                    // Jika Google Maps tidak ada, coba buka di browser
                    String webUri = "https://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(webUri)));
                    } catch (Exception e2) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Tidak ada aplikasi Peta", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        // --- (DIUBAH) Listener Peserta Trip (Buka Activity Baru) ---
        RelativeLayout menuPesertaTrip = view.findViewById(R.id.menu_peserta_trip);
        if (menuPesertaTrip != null) {
            menuPesertaTrip.setOnClickListener(v -> {
                if (getContext() == null) return;

                // (DIUBAH) Kode ini diaktifkan agar Activity terbuka
                Intent intent = new Intent(getContext(), PesertaActivity.class);
                startActivity(intent);
            });
        }

        // --- (DIUBAH) Listener Dokumentasi (Buka Activity Baru) ---
        RelativeLayout menuDokumentasi = view.findViewById(R.id.menu_dokumentasi);
        if (menuDokumentasi != null) {
            menuDokumentasi.setOnClickListener(v -> {
                if (getContext() == null) return;

                // (DIUBAH) Kode ini diaktifkan agar Activity terbuka
                Intent intent = new Intent(getContext(), DokumentasiActivity.class);
                startActivity(intent);
            });
        }

        // --- (BARU) Listener Tombol SOS (Buka Dialer) ---
        MaterialButton tombolSOS = view.findViewById(R.id.tv_sos_button);
        if (tombolSOS != null) {
            tombolSOS.setOnClickListener(v -> {
                // GANTI NOMOR: (Contoh: Nomor darurat nasional)
                String nomorTelepon = "112";

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + nomorTelepon));

                try {
                    startActivity(intent);
                } catch (Exception e) {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Tidak dapat membuka dialer", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}