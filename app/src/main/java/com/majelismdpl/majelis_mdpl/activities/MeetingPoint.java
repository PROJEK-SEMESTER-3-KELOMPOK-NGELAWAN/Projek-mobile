package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.majelismdpl.majelis_mdpl.databinding.ActivityMeetingPointBinding;

public class MeetingPoint extends AppCompatActivity {

    private ActivityMeetingPointBinding binding;
    private CountDownTimer countdownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMeetingPointBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar back button + set title center
        MaterialToolbar toolbar = binding.toolbar;
        toolbar.setNavigationOnClickListener(v -> finish());

        // Set title TextView (title berada di tengah)
        TextView toolbarTitle = binding.toolbarTitle;
        toolbarTitle.setText("Titik Kumpul");

        // Mulai countdown
        startCountdown();

        // Koordinat lokasi basecamp
        final String basecampLatLng = "-7.772566,110.222611";

        // Button buka Google Maps
        binding.btnOpenMaps.setOnClickListener(v -> {
            try {
                Uri uri = Uri.parse("geo:0,0?q=" + basecampLatLng + "(Basecamp Cemoro Sewu)");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            } catch (Exception e) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://maps.google.com/?q=" + basecampLatLng));
                startActivity(browserIntent);
            }
        });
    }

    private void startCountdown() {
        long threeDaysMillis = 3 * 24 * 60 * 60 * 1000L; // Hitung mundur 3 hari

        countdownTimer = new CountDownTimer(threeDaysMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long days = millisUntilFinished / (1000 * 60 * 60 * 24);
                long hours = (millisUntilFinished / (1000 * 60 * 60)) % 24;
                long minutes = (millisUntilFinished / (1000 * 60)) % 60;
                long seconds = (millisUntilFinished / 1000) % 60;

                String text = days + " hari " + hours + " jam " + minutes + " menit " + seconds + " detik";
                binding.tvCountdown.setText(text);
            }

            @Override
            public void onFinish() {
                binding.tvCountdown.setText("Berangkat Sekarang!");
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countdownTimer != null) {
            countdownTimer.cancel();
        }
    }
}
