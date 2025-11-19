package com.majelismdpl.majelis_mdpl.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.majelismdpl.majelis_mdpl.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PreviewPesertaAdapter extends RecyclerView.Adapter<PreviewPesertaAdapter.AvatarHolder> {
    private final List<String> imgList = new ArrayList<>();

    @NonNull
    @Override
    public AvatarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_avatar_peserta, parent, false);
        return new AvatarHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarHolder holder, int position) {
        String foto = imgList.get(position);
        Glide.with(holder.avatar.getContext())
                .load(foto)
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    // dipanggil di DetailTripActivity setelah adapter di-set ke RecyclerView
    public void submitList(List<String> data) {
        imgList.clear();
        if (data != null) imgList.addAll(data);
        notifyDataSetChanged();
    }

    static class AvatarHolder extends RecyclerView.ViewHolder {
        CircleImageView avatar;
        AvatarHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatarImage);
        }
    }
}
