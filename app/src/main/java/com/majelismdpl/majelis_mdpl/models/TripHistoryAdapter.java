package com.majelismdpl.majelis_mdpl.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView; // Pastikan TextView di-import
// Import ImageView jika Anda ingin memuat gambar
// import android.widget.ImageView;
// import com.google.android.material.imageview.ShapeableImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.majelismdpl.majelis_mdpl.R;
// Import library untuk gambar, contoh: Glide
// import com.bumptech.glide.Glide;
import java.util.List;
import java.util.Locale; // Import untuk format string

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.TripViewHolder> {

    private List<Trip> tripList;

    public TripHistoryAdapter(List<Trip> tripList) {
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_history, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        // Ambil data di posisi ini
        Trip trip = tripList.get(position);

        // === Mulai Set Data ke View ===

        // 1. Set Nama Gunung
        holder.tvMountainName.setText(trip.getNamaGunung());

        // 2. Set Tanggal
        holder.tvTripDate.setText(trip.getTanggal());

        // 3. Set Status
        holder.tvStatus.setText(trip.getStatus());

        // 4. Set Jumlah Peserta (format string agar lebih baik)
        String participantText = String.format(Locale.getDefault(), "%d Peserta", trip.getPeserta());
        holder.tvParticipantCount.setText(participantText);

        // 5. Set Rating (ubah double/angka ke String)
        holder.tvRating.setText(String.valueOf(trip.getRating()));

        // 6. Set Gambar (Opsional - Perlu library tambahan seperti Glide)
        // Jika Anda ingin memuat gambar dari 'url_rinjani', 'url_semeru', dll.
        // Hapus komentar di bawah ini dan tambahkan Glide ke build.gradle Anda
        /*
        Glide.with(holder.itemView.getContext())
             .load(trip.getImageUrl()) // Asumsi getImageUrl() mengembalikan URL/resource
             .placeholder(R.color.colorIconBackground) // Gambar sementara
             .into(holder.ivMountainIcon);
        */
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    // Fungsi filter (sudah benar)
    public void filterList(List<Trip> filteredList) {
        this.tripList = filteredList;
        notifyDataSetChanged();
    }

    // === ViewHolder (Sudah Disesuaikan dengan ID XML) ===
    public static class TripViewHolder extends RecyclerView.ViewHolder {

        // Deklarasikan semua View dari XML
        // ShapeableImageView ivMountainIcon;
        TextView tvMountainName;
        TextView tvTripDate;
        TextView tvParticipantCount;
        TextView tvStatus;
        TextView tvRating;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);

            // Hubungkan View dengan ID di file XML
            // ivMountainIcon = itemView.findViewById(R.id.iv_mountain_icon);
            tvMountainName = itemView.findViewById(R.id.tv_mountain_name);
            tvTripDate = itemView.findViewById(R.id.tv_trip_date);
            tvParticipantCount = itemView.findViewById(R.id.tv_participant_count);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvRating = itemView.findViewById(R.id.tv_rating);
        }
    }
}