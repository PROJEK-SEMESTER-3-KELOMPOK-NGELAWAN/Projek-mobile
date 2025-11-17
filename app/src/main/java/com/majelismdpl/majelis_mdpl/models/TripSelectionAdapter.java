package com.majelismdpl.majelis_mdpl.models;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.databinding.ItemTripSelectionBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TripSelectionAdapter extends ListAdapter<UserTripsResponse.TripItem, TripSelectionAdapter.ViewHolder> {

    private static final String TAG = "TripSelectionAdapter";
    private OnTripClickListener listener;

    public interface OnTripClickListener {
        void onTripClick(UserTripsResponse.TripItem trip);
    }

    public TripSelectionAdapter(OnTripClickListener listener) {
        super(new TripDiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTripSelectionBinding binding = ItemTripSelectionBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserTripsResponse.TripItem trip = getItem(position);
        if (trip != null) {
            holder.bind(trip, listener);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTripSelectionBinding binding;

        public ViewHolder(@NonNull ItemTripSelectionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(UserTripsResponse.TripItem trip, OnTripClickListener listener) {
            // Set nama trip
            binding.tvTripName.setText(trip.getNamaGunung() + " - " + trip.getJenisTrip());

            // Set tanggal
            binding.tvTripDate.setText(formatDate(trip.getTanggal()));

            // Set durasi
            binding.tvTripDuration.setText(trip.getDurasi());

            // Set jumlah peserta
            String pesertaText = trip.getTotalPeserta() + " Peserta";
            binding.tvTripParticipants.setText(pesertaText);

            // Set status booking
            String statusText = formatBookingStatus(trip.getBookingStatus());
            binding.tvBookingStatus.setText(statusText);

            // Set warna status badge
            int statusColor;
            switch (trip.getBookingStatus().toLowerCase()) {
                case "paid":
                    statusColor = 0xFF4CAF50; // Green
                    break;
                case "confirmed":
                    statusColor = 0xFF2196F3; // Blue
                    break;
                case "pending":
                    statusColor = 0xFFFFC107; // Amber
                    break;
                default:
                    statusColor = 0xFF757575; // Gray
                    break;
            }
            binding.tvBookingStatus.setBackgroundColor(statusColor);

            // Load gambar trip dengan logging
            String gambarUrl = trip.getGambarUrl();


            if (gambarUrl != null && !gambarUrl.isEmpty()) {
                Glide.with(binding.getRoot().getContext())
                        .load(gambarUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache gambar
                        .placeholder(R.drawable.ic_gunung_ijen)
                        .error(R.drawable.ic_gunung_ijen)
                        .centerCrop()
                        .into(binding.ivTripImage);

            } else {

                binding.ivTripImage.setImageResource(R.drawable.ic_gunung_ijen);
            }

            // Click listener
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTripClick(trip);
                }
            });
        }

        private String formatDate(String dateStr) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));
                Date date = inputFormat.parse(dateStr);
                return outputFormat.format(date);
            } catch (Exception e) {
                Log.e(TAG, "Error formatting date: " + dateStr, e);
                return dateStr;
            }
        }

        private String formatBookingStatus(String status) {
            switch (status.toLowerCase()) {
                case "confirmed":
                    return "Dikonfirmasi";
                case "paid":
                    return "Lunas";
                case "pending":
                    return "Menunggu";
                default:
                    return status;
            }
        }
    }

    static class TripDiffCallback extends DiffUtil.ItemCallback<UserTripsResponse.TripItem> {
        @Override
        public boolean areItemsTheSame(@NonNull UserTripsResponse.TripItem oldItem, @NonNull UserTripsResponse.TripItem newItem) {
            return oldItem.getIdTrip() == newItem.getIdTrip();
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserTripsResponse.TripItem oldItem, @NonNull UserTripsResponse.TripItem newItem) {
            return oldItem.getIdTrip() == newItem.getIdTrip() &&
                    oldItem.getTotalPeserta() == newItem.getTotalPeserta() &&
                    oldItem.getBookingStatus().equals(newItem.getBookingStatus()) &&
                    oldItem.getGambarUrl().equals(newItem.getGambarUrl()); // Tambahkan pengecekan URL gambar
        }
    }
}
