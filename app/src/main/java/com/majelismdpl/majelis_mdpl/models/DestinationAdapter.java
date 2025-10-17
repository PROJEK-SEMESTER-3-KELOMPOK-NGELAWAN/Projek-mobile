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

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {

    private List<Destination> destinationList;

    public DestinationAdapter(List<Destination> destinationList) {
        this.destinationList = destinationList;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_destination, parent, false);
        return new DestinationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        Destination destination = destinationList.get(position);
        holder.destinationName.setText(destination.getName());
        holder.destinationLocation.setText(destination.getLocation());
        holder.destinationImage.setImageResource(destination.getImageResId());
        // Untuk gambar dari URL, gunakan library seperti Glide atau Picasso
    }

    @Override
    public int getItemCount() {
        return destinationList.size();
    }

    static class DestinationViewHolder extends RecyclerView.ViewHolder {
        ImageView destinationImage;
        TextView destinationName;
        TextView destinationLocation;

        DestinationViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationImage = itemView.findViewById(R.id.iv_destination_image);
            destinationName = itemView.findViewById(R.id.tv_destination_name);
            destinationLocation = itemView.findViewById(R.id.tv_destination_location);
        }
    }
}
