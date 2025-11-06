package com.majelismdpl.majelis_mdpl.models;

import android.content.Context; // <-- IMPORT DITAMBAHKAN
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
// (TextView dan ImageView tidak perlu di-import lagi)

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// <-- IMPORT PENTING DITAMBAHKAN
import com.bumptech.glide.Glide;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.databinding.ItemTripHistoryBinding; // <-- IMPORT VIEW BINDING

import java.util.List;
import java.util.Locale;

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.TripViewHolder> {

    private List<Trip> tripList;

    // --- 1. LOGIKA LISTENER DITAMBAHKAN ---
    private OnItemClickListener listener;

    // Interface yang akan "didengar" oleh HistoryFragment
    public interface OnItemClickListener {
        void onItemClick(Trip trip);
    }

    // Metode untuk HistoryFragment mendaftarkan dirinya sebagai listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    // --- BATAS TAMBAHAN ---


    public TripHistoryAdapter(List<Trip> tripList) {
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // --- 2. PERBAIKAN: MENGGUNAKAN VIEW BINDING ---
        // Mengganti 'View view = ...'
        ItemTripHistoryBinding binding = ItemTripHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new TripViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip currentTrip = tripList.get(position);
        Context context = holder.itemView.getContext(); // Ambil context untuk Glide

        // --- 3. PERBAIKAN: MENGGUNAKAN 'holder.binding' ---
        holder.binding.tvMountainName.setText(currentTrip.getMountainName());
        holder.binding.tvTripDate.setText(currentTrip.getDate());
        holder.binding.tvParticipantCount.setText(String.format(Locale.getDefault(), "%d peserta", currentTrip.getParticipants()));
        holder.binding.tvStatus.setText(currentTrip.getStatus());
        holder.binding.tvRating.setText(String.valueOf(currentTrip.getRating()));

        // --- 4. PERBAIKAN: IMPLEMENTASI GLIDE (BUKAN KOMENTAR) ---
        // (Asumsi model Anda punya method getImageUrl())
        Glide.with(context)
                .load(currentTrip.getImageUrl())
                .placeholder(R.drawable.ic_gunung_ijen) // Gambar default saat loading
                .error(R.drawable.ic_gunung_ijen)       // Gambar jika URL error
                .into(holder.binding.ivMountainIcon);

        // --- 5. LOGIKA LISTENER DITAMBAHKAN ---
        // Menetapkan listener ke seluruh item view
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentTrip);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    // --- 6. PERBAIKAN: VIEW HOLDER MENGGUNAKAN VIEW BINDING ---
    public static class TripViewHolder extends RecyclerView.ViewHolder {
        // Hanya butuh satu variabel binding
        ItemTripHistoryBinding binding;

        public TripViewHolder(ItemTripHistoryBinding binding) { // Parameter diubah
            super(binding.getRoot());
            this.binding = binding; // Simpan binding
            // Tidak perlu findViewById lagi
        }
    }
}