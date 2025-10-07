package com.majelismdpl.majelis_mdpl.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.majelis_mdpl.R;

public class HomeFragment extends Fragment {

    // Koordinat Gunung Ijen
    private static final double IJEN_LATITUDE = -8.0580;
    private static final double IJEN_LONGITUDE = 114.2420;
    private static final String IJEN_LOCATION_NAME = "Gunung Ijen";

    // Data pembayaran
    private static final String STATUS_PEMBAYARAN = "LUNAS";
    private static final String TANGGAL_BAYAR = "10 Maret 2024";
    private static final String METODE_BAYAR = "Transfer Bank BCA";
    private static final String NOMINAL_BAYAR = "Rp 450.000";
    private static final String NO_REFERENSI = "TRX-2024-00321";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("HomeFragment", "onCreateView dipanggil");
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("HomeFragment", "onViewCreated dipanggil");

        setupClickListeners(view);
        setupHeaderData(view);
        setupTripData(view);
    }

    private void setupHeaderData(View view) {
        try {
            // Set current date
            TextView textDate = view.findViewById(R.id.text_date);
            if (textDate != null) {
                textDate.setText("Selasa, 12 April");
            }

            // Set trip title header
            TextView textTripTitleHeader = view.findViewById(R.id.text_trip_title_header);
            if (textTripTitleHeader != null) {
                textTripTitleHeader.setText("Perjalanan Saya");
            }

        } catch (Exception e) {
            Log.e("HomeFragment", "Error setupHeaderData: " + e.getMessage());
        }
    }

    private void setupTripData(View view) {
        try {
            // Set trip title
            TextView textTripTitle = view.findViewById(R.id.text_trip_title);
            if (textTripTitle != null) {
                textTripTitle.setText("Pendakian Gunung Ijen");
            }

            // Set trip date
            TextView textTripDate = view.findViewById(R.id.text_trip_date);
            if (textTripDate != null) {
                textTripDate.setText("12 - 14 April 2024");
            }

            // Set payment status
            TextView textPaymentStatus = view.findViewById(R.id.text_payment_status);
            if (textPaymentStatus != null) {
                textPaymentStatus.setText("Lunas");
                textPaymentStatus.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark));
            }

            // Set jumlah peserta
            TextView textJumlahPeserta = view.findViewById(R.id.text_jumlah_peserta);
            if (textJumlahPeserta != null) {
                textJumlahPeserta.setText("20 Orang");
            }

        } catch (Exception e) {
            Log.e("HomeFragment", "Error setupTripData: " + e.getMessage());
        }
    }

    private void setupClickListeners(View view) {
        try {
            Log.d("HomeFragment", "Setup click listeners dimulai");

            // 1. Status Pembayaran
            View paymentStatusLayout = view.findViewById(R.id.layout_payment_status);
            if (paymentStatusLayout != null) {
                paymentStatusLayout.setOnClickListener(v -> lihatDetailPembayaran());
            }

            // 2. Menu Items
            setupMenuClickListeners(view);

            Toast.makeText(requireActivity(), "Trip details siap digunakan!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("HomeFragment", "Error setupClickListeners: " + e.getMessage(), e);
            Toast.makeText(requireActivity(), "Error setup: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setupMenuClickListeners(View view) {
        // Titik Kumpul
        View titikKumpul = view.findViewById(R.id.layout_titik_kumpul);
        if (titikKumpul != null) {
            titikKumpul.setOnClickListener(v -> bukaPetaTitikKumpul());
        }

        // Peserta Trip
        View pesertaTrip = view.findViewById(R.id.layout_peserta_trip);
        if (pesertaTrip != null) {
            pesertaTrip.setOnClickListener(v -> tampilkanDaftarPeserta());
        }

        // Grup WhatsApp
        View whatsapp = view.findViewById(R.id.layout_whatsapp);
        if (whatsapp != null) {
            whatsapp.setOnClickListener(v -> gabungGrupWhatsApp());
        }

        // Dokumentasi
        View dokumentasi = view.findViewById(R.id.layout_dokumentasi);
        if (dokumentasi != null) {
            dokumentasi.setOnClickListener(v -> bukaDokumentasiFoto());
        }

        // Tombol Darurat
        View tombolDarurat = view.findViewById(R.id.layout_tombol_darurat);
        if (tombolDarurat != null) {
            tombolDarurat.setOnClickListener(v -> tekanTombolDarurat());
        }
    }

    private void lihatDetailPembayaran() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("💳 Detail Pembayaran");

            View dialogView = createPaymentDetailView();
            builder.setView(dialogView);

            builder.setPositiveButton("Tutup", (dialog, which) -> dialog.dismiss());

            if ("LUNAS".equals(STATUS_PEMBAYARAN)) {
                builder.setNeutralButton("📥 Unduh Bukti", (dialog, which) -> unduhBuktiPembayaran());
            }

            AlertDialog dialog = builder.create();
            dialog.show();

            Log.d("HomeFragment", "Dialog pembayaran ditampilkan");

        } catch (Exception e) {
            Log.e("HomeFragment", "Error lihatDetailPembayaran: " + e.getMessage());
            Toast.makeText(requireActivity(), "Error menampilkan detail pembayaran", Toast.LENGTH_SHORT).show();
        }
    }

    private View createPaymentDetailView() {
        try {
            return createCustomPaymentView();
        } catch (Exception e) {
            Log.w("HomeFragment", "Gagal membuat custom view, menggunakan fallback");
            return createFallbackPaymentView();
        }
    }

    private View createCustomPaymentView() {
        LinearLayout layout = new LinearLayout(requireActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dpToPx(20), dpToPx(16), dpToPx(20), dpToPx(16));

        String[][] paymentData = {
                {"Status Pembayaran", STATUS_PEMBAYARAN},
                {"Tanggal Bayar", TANGGAL_BAYAR},
                {"Metode Bayar", METODE_BAYAR},
                {"Nominal Bayar", NOMINAL_BAYAR},
                {"No. Referensi", NO_REFERENSI}
        };

        for (int i = 0; i < paymentData.length; i++) {
            String[] data = paymentData[i];

            LinearLayout itemLayout = new LinearLayout(requireActivity());
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setPadding(0, dpToPx(8), 0, dpToPx(8));

            TextView labelView = new TextView(requireActivity());
            labelView.setText(data[0] + ":");
            labelView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            labelView.setLayoutParams(labelParams);
            labelView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));

            TextView valueView = new TextView(requireActivity());
            valueView.setText(data[1]);
            valueView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            LinearLayout.LayoutParams valueParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            valueView.setLayoutParams(valueParams);
            valueView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));

            if (data[0].equals("Status Pembayaran")) {
                if ("LUNAS".equals(data[1])) {
                    valueView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark));
                } else {
                    valueView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark));
                }
            }

            if (data[0].equals("Nominal Bayar")) {
                valueView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark));
                valueView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            }

            itemLayout.addView(labelView);
            itemLayout.addView(valueView);
            layout.addView(itemLayout);

            if (i < paymentData.length - 1) {
                View separator = new View(requireActivity());
                separator.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1));
                params.setMargins(0, dpToPx(8), 0, dpToPx(8));
                separator.setLayoutParams(params);
                layout.addView(separator);
            }
        }

        return layout;
    }

    private View createFallbackPaymentView() {
        LinearLayout layout = new LinearLayout(requireActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

        String[] details = {
                "Status: " + STATUS_PEMBAYARAN,
                "Tanggal: " + TANGGAL_BAYAR,
                "Metode: " + METODE_BAYAR,
                "Nominal: " + NOMINAL_BAYAR,
                "Referensi: " + NO_REFERENSI
        };

        for (String detail : details) {
            TextView textView = new TextView(requireActivity());
            textView.setText(detail);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setPadding(0, dpToPx(8), 0, dpToPx(8));

            if (detail.contains("Status: LUNAS")) {
                textView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark));
            }

            layout.addView(textView);
        }

        return layout;
    }

    private int dpToPx(int dp) {
        try {
            float density = getResources().getDisplayMetrics().density;
            return Math.round(dp * density);
        } catch (Exception e) {
            return dp * 3;
        }
    }

    private void unduhBuktiPembayaran() {
        Toast.makeText(requireActivity(), "Mengunduh bukti pembayaran...", Toast.LENGTH_SHORT).show();
        try {
            String downloadUrl = "https://example.com/bukti-pembayaran-" + NO_REFERENSI + ".pdf";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(downloadUrl));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(requireActivity(), "Tidak bisa membuka bukti pembayaran", Toast.LENGTH_SHORT).show();
        }
    }

    private void bukaPetaTitikKumpul() {
        try {
            String uri = "http://maps.google.com/maps?q=" + IJEN_LATITUDE + "," + IJEN_LONGITUDE + "(" + IJEN_LOCATION_NAME + ")";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");

            if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/place/" + IJEN_LATITUDE + "," + IJEN_LONGITUDE));
                startActivity(browserIntent);
            }

            Toast.makeText(requireActivity(), "Membuka peta titik kumpul", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(requireActivity(), "Error membuka peta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void tampilkanDaftarPeserta() {
        Toast.makeText(requireActivity(), "Menampilkan daftar 20 peserta trip", Toast.LENGTH_SHORT).show();
        showPesertaDialog();
    }

    private void gabungGrupWhatsApp() {
        try {
            String whatsappGroupLink = "https://chat.whatsapp.com/EXAMPLELINK123";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(whatsappGroupLink));

            if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(requireActivity(), "WhatsApp tidak terinstall", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(requireActivity(), "Error membuka WhatsApp", Toast.LENGTH_SHORT).show();
        }
    }

    private void bukaDokumentasiFoto() {
        Toast.makeText(requireActivity(), "Membuka galeri dokumentasi", Toast.LENGTH_SHORT).show();
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivity(Intent.createChooser(intent, "Pilih aplikasi galeri"));
        } catch (Exception e) {
            Toast.makeText(requireActivity(), "Tidak ada aplikasi galeri", Toast.LENGTH_SHORT).show();
        }
    }

    private void tekanTombolDarurat() {
        Toast.makeText(requireActivity(), "🚨 Tombol Darurat Ditekan! Hubungi 505", Toast.LENGTH_LONG).show();
        kirimPesanDarurat();
    }

    private void showPesertaDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("👥 Daftar Peserta (20 Orang)");
        builder.setMessage("1. Alex Morgan (Leader)\n2. Budi Santoso\n3. Siti Rahayu\n4. ... dan 17 peserta lainnya");
        builder.setPositiveButton("Tutup", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void kirimPesanDarurat() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String pesanDarurat = "🚨 DARURAT! Saya butuh bantuan di Gunung Ijen. Lokasi: " +
                    IJEN_LATITUDE + ", " + IJEN_LONGITUDE +
                    ". Tolong hubungi 505.";
            intent.putExtra(Intent.EXTRA_TEXT, pesanDarurat);
            startActivity(Intent.createChooser(intent, "Kirim pesan darurat melalui"));
        } catch (Exception e) {
            Toast.makeText(requireActivity(), "Tidak bisa mengirim pesan darurat", Toast.LENGTH_SHORT).show();
        }
    }
}