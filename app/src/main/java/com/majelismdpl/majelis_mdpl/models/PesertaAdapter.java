package com.majelismdpl.majelis_mdpl.models; // <- DIUBAH ke 'models'

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.databinding.ItemPesertaTripBinding;
// 'Peserta' sekarang berada di package yang sama, import ini secara teknis tidak apa-apa


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

        public PesertaViewHolder(@NonNull ItemPesertaTripBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = itemView.getContext();

            // Hapus import Glide jika Anda tidak lagi menggunakannya
            // import com.bumptech.glide.Glide;
            // Pastikan import R sudah ada: import com.majelismdpl.majelis_mdpl.R;
        }

        // --- GANTI SELURUH ISI METODE BIND ANDA DENGAN INI ---
        public void bind(Peserta peserta) {

            // 1. Set Nama
            binding.tvNamaPeserta.setText(peserta.getNama());

            // 2. Set Avatar (INI PERBAIKAN UTAMANYA untuk drawable)
            String drawableName = peserta.getAvatarUrl(); // Ini akan berisi "dimasdwi", "daffa", dll.
            if (drawableName != null && !drawableName.isEmpty()) {
                // Dapatkan resource ID dari nama drawable
                int drawableId = context.getResources().getIdentifier(
                        drawableName, "drawable", context.getPackageName());

                if (drawableId != 0) { // Jika drawable ditemukan
                    binding.imgAvatar.setImageResource(drawableId);
                } else {
                    // Fallback jika nama drawable tidak ditemukan
                    binding.imgAvatar.setImageResource(R.drawable.dimasdwi); // Ganti dengan placeholder Anda
                }
            } else {
                // Fallback jika avatarUrl kosong
                binding.imgAvatar.setImageResource(R.drawable.dimas_gontor); // Ganti dengan placeholder Anda
            }

            // 3. Set Logika Status (Kode ini seharusnya sudah benar dari sebelumnya)
            String status = peserta.getStatus();

            if (status != null && status.equalsIgnoreCase("HADIR")) {
                binding.tvStatusPeserta.setText("Hadir");
                binding.imgStatusIcon.setImageResource(R.drawable.ic_check_circle);
                int colorHadir = ContextCompat.getColor(context, R.color.status_hadir);
                binding.imgStatusIcon.setColorFilter(colorHadir, PorterDuff.Mode.SRC_IN);

            } else if (status != null && status.equalsIgnoreCase("IZIN")) {
                binding.tvStatusPeserta.setText("Izin");
                // (Ganti dengan icon izin jika ada)
                binding.imgStatusIcon.setImageResource(R.drawable.ic_check_circle); // Placeholder
                int colorIzin = ContextCompat.getColor(context, R.color.status_izin);
                binding.imgStatusIcon.setColorFilter(colorIzin, PorterDuff.Mode.SRC_IN);

            } else {
                binding.tvStatusPeserta.setText(status != null ? status : "Belum Konfirmasi");
                // (Ganti dengan icon default jika ada)
                binding.imgStatusIcon.setImageResource(R.drawable.ic_check_circle); // Placeholder
                int colorDefault = ContextCompat.getColor(context, R.color.text_light);
                binding.imgStatusIcon.setColorFilter(colorDefault, PorterDuff.Mode.SRC_IN);
            }
        }
    }

    /**
     * DiffUtil (Diperbarui untuk model baru)
     */
    static class PesertaDiffCallback extends DiffUtil.ItemCallback<Peserta> {
        @Override
        public boolean areItemsTheSame(@NonNull Peserta oldItem, @NonNull Peserta newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Peserta oldItem, @NonNull Peserta newItem) {
            // Gunakan .equals() dari model Peserta.java
            return oldItem.equals(newItem);
        }
    }
}