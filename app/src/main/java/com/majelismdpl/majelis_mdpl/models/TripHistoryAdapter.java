package com.majelismdpl.majelis_mdpl.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majelismdpl.majelis_mdpl.R;

import java.util.List;
import java.util.Locale;

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.TripViewHolder> {

    private List<Trip> tripList;

    public TripHistoryAdapter(List<Trip> tripList) {
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip_history, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip currentTrip = tripList.get(position);

        holder.mountainName.setText(currentTrip.getMountainName());
        holder.tripDate.setText(currentTrip.getDate());
        holder.participantCount.setText(String.format(Locale.getDefault(), "%d peserta", currentTrip.getParticipants()));
        holder.status.setText(currentTrip.getStatus());
        holder.rating.setText(String.valueOf(currentTrip.getRating()));

        // Di sini Anda bisa menggunakan library seperti Glide atau Picasso untuk memuat gambar
        // Contoh: Glide.with(holder.itemView.getContext()).load(currentTrip.getImageUrl()).into(holder.mountainIcon);
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        ImageView mountainIcon;
        TextView mountainName;
        TextView tripDate;
        TextView participantCount;
        TextView status;
        TextView rating;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            mountainIcon = itemView.findViewById(R.id.iv_mountain_icon);
            mountainName = itemView.findViewById(R.id.tv_mountain_name);
            tripDate = itemView.findViewById(R.id.tv_trip_date);
            participantCount = itemView.findViewById(R.id.tv_participant_count);
            status = itemView.findViewById(R.id.tv_status);
            rating = itemView.findViewById(R.id.tv_rating);
        }
    }
}
