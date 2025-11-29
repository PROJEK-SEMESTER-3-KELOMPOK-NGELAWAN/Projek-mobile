package com.majelismdpl.majelis_mdpl.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.models.TripDokumentasiResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TripDokumentasiAdapter extends RecyclerView.Adapter<TripDokumentasiAdapter.ViewHolder> {

    private static final String TAG = "TripDokumentasiAdapter";
    private Context context;
    private List<TripDokumentasiResponse.DokumentasiData> tripList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onOpenDriveClick(TripDokumentasiResponse.DokumentasiData trip);
    }

    public TripDokumentasiAdapter(Context context, List<TripDokumentasiResponse.DokumentasiData> tripList, OnItemClickListener listener) {
        this.context = context;
        this.tripList = tripList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trip_dokumentasi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TripDokumentasiResponse.DokumentasiData trip = tripList.get(position);

        // Set data
        holder.tvTripName.setText(trip.getNamaGunung());
        holder.tvGaleryName.setText(trip.getGaleryName());

        // Format tanggal dengan durasi
        String tanggalInfo = formatDate(trip.getTanggal());
        if (trip.getDurasi() != null && !trip.getDurasi().isEmpty()) {
            tanggalInfo += " • " + trip.getDurasi();
        }
        holder.tvTanggal.setText(tanggalInfo);

        // Load gambar trip dengan Glide
        String gambarUrl = trip.getGambar();

        Log.d(TAG, "Loading image for " + trip.getNamaGunung() + ": " + gambarUrl);

        if (gambarUrl != null && !gambarUrl.isEmpty()) {
            Glide.with(context)
                    .load(gambarUrl)
                    .placeholder(R.drawable.ic_camera_alt)
                    .error(R.drawable.ic_camera_alt)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(holder.ivTripImage);
        } else {
            Log.w(TAG, "Empty image URL for: " + trip.getNamaGunung());
            holder.ivTripImage.setImageResource(R.drawable.ic_camera_alt);
        }

        // Button click
        holder.btnOpenDrive.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOpenDriveClick(trip);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripList != null ? tripList.size() : 0;
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: " + dateStr);
            return dateStr;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView ivTripImage;
        TextView tvTripName, tvGaleryName, tvTanggal;
        MaterialButton btnOpenDrive;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_trip);
            ivTripImage = itemView.findViewById(R.id.iv_trip_image);
            tvTripName = itemView.findViewById(R.id.tv_trip_name);
            tvGaleryName = itemView.findViewById(R.id.tv_galery_name);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            btnOpenDrive = itemView.findViewById(R.id.btn_open_drive);
        }
    }
}
