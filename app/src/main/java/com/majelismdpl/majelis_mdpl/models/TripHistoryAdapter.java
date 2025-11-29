package com.majelismdpl.majelis_mdpl.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.majelismdpl.majelis_mdpl.databinding.ItemTripHistoryBinding;

import java.util.List;
import java.util.Locale;

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.TripViewHolder> {
    private final List<TripHistoryItem> tripList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TripHistoryItem trip);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public TripHistoryAdapter(List<TripHistoryItem> tripList) {
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTripHistoryBinding binding = ItemTripHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new TripViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        TripHistoryItem currentTrip = tripList.get(position);
        Context context = holder.itemView.getContext();

        holder.binding.tvMountainName.setText(currentTrip.getMountainName());
        holder.binding.tvTripDate.setText(currentTrip.getDate());
        holder.binding.tvStatus.setText(currentTrip.getStatus());
        holder.binding.tvRating.setText(String.valueOf(currentTrip.getRating()));

        Glide.with(context)
                .load(currentTrip.getImageUrl())
                .into(holder.binding.ivMountainIcon);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentTrip);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripList != null ? tripList.size() : 0;
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        ItemTripHistoryBinding binding;
        public TripViewHolder(ItemTripHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
