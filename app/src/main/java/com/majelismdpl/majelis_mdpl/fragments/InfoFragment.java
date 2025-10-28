package com.majelismdpl.majelis_mdpl.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.activities.LoginActivity;
import com.majelismdpl.majelis_mdpl.utils.SharedPrefManager;

public class InfoFragment extends Fragment {

    public InfoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        Button btnLogout = view.findViewById(R.id.btn_logout);

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                if (getActivity() == null) {
                    return;
                }

                // Logout menggunakan SharedPrefManager yang sudah ada
                SharedPrefManager.getInstance(requireActivity()).logout();

                // Bersihkan fragment stack
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                // Intent ke LoginActivity
                Intent intent = new Intent(requireActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                // Tutup activity
                requireActivity().finishAffinity();
            });
        }

        return view;
    }
}
