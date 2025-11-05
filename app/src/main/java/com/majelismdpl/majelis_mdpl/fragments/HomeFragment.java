package com.majelismdpl.majelis_mdpl.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.activities.MeetingPoint;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi views
        TextView tvTripTitle = view.findViewById(R.id.tv_trip_title);
        TextView tvTripDate = view.findViewById(R.id.tv_trip_date);
        ImageView ivTripImage = view.findViewById(R.id.iv_trip_image);

        // Set data statis
        tvTripTitle.setText("Pendakian Gunung Ijen");
        tvTripDate.setText("12-13 Agustus 2025");
        ivTripImage.setImageResource(R.drawable.ic_gunung_ijen);

        RelativeLayout menuTitikKumpul = view.findViewById(R.id.menu_titik_kumpul);
        menuTitikKumpul.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MeetingPoint.class);
            startActivity(intent);

        });
    }
}