package com.majelismdpl.majelis_mdpl.models;

import android.content.ActivityNotFoundException; // <- IMPORT DITAMBAHKAN
import android.content.Context;
import android.content.Intent; // <- IMPORT DITAMBAHKAN
import android.graphics.PorterDuff;
import android.net.Uri; // <- IMPORT DITAMBAHKAN
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast; // <- IMPORT DITAMBAHKAN

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.databinding.ItemPesertaTripBinding;


public class PesertaAdapter extends ListAdapter<Peserta, PesertaAdapter.PesertaViewHolder> {

    public PesertaAdapter() {
        super(new PesertaDiffCallback());
    }

    @NonNull
    @Override
    public PesertaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPesertaTripBinding binding = ItemPesertaTripBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new PesertaViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PesertaViewHolder holder, int position) {
        Peserta peserta = getItem(position);
        if (peserta != null) {
            holder.bind(peserta);
        }
    }

    /**
     * ViewHolder (Ini bagian yang diperbaiki)
     */
    class PesertaViewHolder extends RecyclerView.ViewHolder {

        private final ItemPesertaTripBinding binding;
        private final Context context;

        // --- CONSTRUCTOR DIUBAH ---
        public PesertaViewHolder(@NonNull ItemPesertaTripBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = itemView.getContext();

            // --- PERMINTAAN 1: WARNA HIJAU KONSISTEN ---
            // (Saya asumsikan R.color.status_hadir adalah warna hijau Anda)
            int colorHijau = ContextCompat.getColor(context, R.color.status_hadir);
            binding.imgStatusIcon.setColorFilter(colorHijau, PorterDuff.Mode.SRC_IN);

            // --- PERMINTAAN 2: BUKA CHAT WHATSAPP SAAT DIKLIK ---
            binding.imgStatusIcon.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    return; // Posisi tidak valid
                }

                Peserta peserta = getItem(position);
                // Pastikan model Peserta Anda punya method getNomorWa()
                String nomorWa = peserta.getNomorWa();

                if (nomorWa == null || nomorWa.isEmpty()) {
                    Toast.makeText(context, "Nomor WA tidak tersedia", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Format nomor: ganti "08..." jadi "628...", hapus "+"
                String formattedNomor = nomorWa.trim().replace("+", "");
                if (formattedNomor.startsWith("0")) {
                    formattedNomor = "62" + formattedNomor.substring(1);
                }

                try {
                    // Buat Intent untuk membuka chat WA
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://wa.me/" + formattedNomor));
                    intent.setPackage("com.whatsapp"); // Langsung targetkan WhatsApp
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // Jika WhatsApp tidak terinstal
                    Toast.makeText(context, "WhatsApp tidak terinstal.", Toast.LENGTH_SHORT).show();
                    // Fallback: Coba buka di browser
                    try {
                        Intent intentFallback = new Intent(Intent.ACTION_VIEW);
                        intentFallback.setData(Uri.parse("https://wa.me/" + formattedNomor));
                        context.startActivity(intentFallback);
                    } catch (ActivityNotFoundException e2) {
                        Toast.makeText(context, "Tidak ada aplikasi untuk membuka link ini.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        // --- METODE BIND DIUBAH (dihapus setColorFilter) ---
        public void bind(Peserta peserta) {

            // 1. Set Nama
            binding.tvNamaPeserta.setText(peserta.getNama());

            // 2. Set Avatar (Logika ini tetap sama)
            String drawableName = peserta.getAvatarUrl();
            if (drawableName != null && !drawableName.isEmpty()) {
                int drawableId = context.getResources().getIdentifier(
                        drawableName, "drawable", context.getPackageName());

                if (drawableId != 0) {
                    binding.imgAvatar.setImageResource(drawableId);
                } else {
                    binding.imgAvatar.setImageResource(R.drawable.dimasdwi); // Fallback
                }
            } else {
                binding.imgAvatar.setImageResource(R.drawable.dimas_gontor); // Fallback
            }

            // 3. Set Logika Status (TANPA MENGUBAH WARNA ICON)
            String status = peserta.getStatus();

            if (status != null && status.equalsIgnoreCase("HADIR")) {
                binding.tvStatusPeserta.setText("Hadir");
                binding.imgStatusIcon.setImageResource(R.drawable.ic_whatsapp);
                // [DIHAPUS] baris setColorFilter

            } else if (status != null && status.equalsIgnoreCase("IZIN")) {
                binding.tvStatusPeserta.setText("Izin");
                binding.imgStatusIcon.setImageResource(R.drawable.ic_whatsapp); // Placeholder
                // [DIHAPUS] baris setColorFilter

            } else {
                binding.tvStatusPeserta.setText(status != null ? status : "Belum Konfirmasi");
                binding.imgStatusIcon.setImageResource(R.drawable.ic_whatsapp); // Placeholder
                // [DIHAPUS] baris setColorFilter
            }
        }
    }

    /**
     * DiffUtil (Tidak ada perubahan)
     */
    static class PesertaDiffCallback extends DiffUtil.ItemCallback<Peserta> {
        @Override
        public boolean areItemsTheSame(@NonNull Peserta oldItem, @NonNull Peserta newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Peserta oldItem, @NonNull Peserta newItem) {
            return oldItem.equals(newItem);
        }
    }
}