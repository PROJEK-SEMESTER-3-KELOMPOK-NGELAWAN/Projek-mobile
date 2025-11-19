package com.majelismdpl.majelis_mdpl.models;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.majelismdpl.majelis_mdpl.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PesertaAdapterHistory extends RecyclerView.Adapter<PesertaAdapterHistory.PesertaViewHolder> {

    private List<PesertaHistory> pesertaList = new ArrayList<>();

    @NonNull
    @Override
    public PesertaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_peserta, parent, false);
        return new PesertaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PesertaViewHolder holder, int position) {
        PesertaHistory peserta = pesertaList.get(position);

        // Set Nama
        holder.tvNama.setText(peserta.getNama());

        // Set Status Text dan Warna
        String status = peserta.getBookingStatus();
        String statusText = "Belum Konfirmasi";
        int statusColor = holder.itemView.getContext().getColor(R.color.text_light);
        int iconRes = R.drawable.ic_help; // default icon

        if (status != null) {
            if (status.equalsIgnoreCase("confirmed") || status.equalsIgnoreCase("paid")) {
                statusText = "Hadir";
                statusColor = holder.itemView.getContext().getColor(R.color.status_hadir);
                iconRes = R.drawable.ic_check_circle;
            } else if (status.equalsIgnoreCase("cancelled")) {
                statusText = "Izin";
                statusColor = holder.itemView.getContext().getColor(R.color.status_izin);
                iconRes = R.drawable.ic_close;
            }
        }
        holder.tvStatus.setText(statusText);
        holder.tvStatus.setTextColor(statusColor);
        holder.imgStatus.setImageResource(iconRes);
        holder.imgStatus.setColorFilter(statusColor);

        // Set Avatar (load foto profil jika ada, fallback drawable jika tidak)
        String foto = peserta.getFotoProfil();
        if (foto != null && !foto.isEmpty()) {
            Glide.with(holder.imgAvatar.getContext())
                    .load(foto)
                    .placeholder(R.drawable.default_avatar)    // default avatar
                    .error(R.drawable.default_avatar)
                    .into(holder.imgAvatar);
        } else {
            holder.imgAvatar.setImageResource(R.drawable.default_avatar);
        }
    }

    @Override
    public int getItemCount() {
        return pesertaList.size();
    }

    public void submitList(List<PesertaHistory> data) {
        pesertaList.clear();
        if (data != null) pesertaList.addAll(data);
        notifyDataSetChanged();
    }

    static class PesertaViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgAvatar;
        ImageView imgStatus;
        TextView tvNama, tvStatus;

        PesertaViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            imgStatus = itemView.findViewById(R.id.img_status_icon);
            tvNama = itemView.findViewById(R.id.tv_nama_peserta);
            tvStatus = itemView.findViewById(R.id.tv_status_peserta);
        }
    }
}
