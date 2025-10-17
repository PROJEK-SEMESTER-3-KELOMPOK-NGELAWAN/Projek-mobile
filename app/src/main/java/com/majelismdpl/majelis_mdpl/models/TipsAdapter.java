package com.majelismdpl.majelis_mdpl.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.models.Tip;

import java.util.List;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.TipViewHolder> {

    private List<Tip> tipList;

    public TipsAdapter(List<Tip> tipList) {
        this.tipList = tipList;
    }

    @NonNull
    @Override
    public TipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tips_and_tricks, parent, false);
        return new TipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipViewHolder holder, int position) {
        Tip tip = tipList.get(position);
        holder.tipTitle.setText(tip.getTitle());
        holder.tipDescription.setText(tip.getDescription());
        holder.tipImage.setImageResource(tip.getImageResId());
    }

    @Override
    public int getItemCount() {
        return tipList.size();
    }

    static class TipViewHolder extends RecyclerView.ViewHolder {
        ImageView tipImage;
        TextView tipTitle;
        TextView tipDescription;

        TipViewHolder(@NonNull View itemView) {
            super(itemView);
            tipImage = itemView.findViewById(R.id.iv_tip_image);
            tipTitle = itemView.findViewById(R.id.tv_tip_title);
            tipDescription = itemView.findViewById(R.id.tv_tip_description);
        }
    }
}
