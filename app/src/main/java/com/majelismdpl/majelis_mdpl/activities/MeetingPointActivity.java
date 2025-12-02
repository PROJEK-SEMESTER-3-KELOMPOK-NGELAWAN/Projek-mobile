package com.majelismdpl.majelis_mdpl.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.api.ApiClient;
import com.majelismdpl.majelis_mdpl.api.ApiService;
import com.majelismdpl.majelis_mdpl.databinding.ActivityMeetingPointBinding;
import com.majelismdpl.majelis_mdpl.models.MeetingPoint;
import com.majelismdpl.majelis_mdpl.models.MeetingPointResponse;
import com.majelismdpl.majelis_mdpl.models.User;
import com.majelismdpl.majelis_mdpl.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingPointActivity extends AppCompatActivity {

    private ActivityMeetingPointBinding binding;
    private SessionManager sessionManager;
    private RecyclerView rvMeetingPoints;
    private LinearLayout emptyText;

    private MeetingPointAdapter adapter;
    private List<MeetingPoint> meetingPoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMeetingPointBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // PERBAIKAN: Gunakan binding untuk inisialisasi view
        emptyText = binding.emptyText;
        rvMeetingPoints = binding.rvMeetingPoints;

        sessionManager = SessionManager.getInstance(this);
        MaterialToolbar toolbar = binding.toolbar;
        toolbar.setNavigationOnClickListener(v -> finish());

        // Setup RecyclerView
        rvMeetingPoints.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MeetingPointAdapter(meetingPoints, this);
        rvMeetingPoints.setAdapter(adapter);

        User user = sessionManager.getUser();
        Log.d("MeetingPointActivity", "User login ID: " + (user != null ? user.getId() : "NULL"));
        if (user == null || user.getId() <= 0) {
            Toast.makeText(this, "User tidak ditemukan atau ID tidak valid!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        if (meetingPoints.isEmpty()) {
            rvMeetingPoints.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            Log.d("MeetingPointActivity", "✅ Simulating local data.");
        }
        // =========================================================================

        loadMeetingPointsFromApi(user.getId());
    }

    private void loadMeetingPointsFromApi(int idUser) {
        // Jika data simulasi ada, kosongkan dulu sebelum memuat dari API
        // meetingPoints.clear(); // Opsional: Hapus baris ini jika Anda ingin melihat simulasi dulu

        ApiService api = ApiClient.getApiService();
        Call<MeetingPointResponse> call = api.getUserMeetingPoint(idUser);
        call.enqueue(new Callback<MeetingPointResponse>() {
            @Override
            public void onResponse(@NonNull Call<MeetingPointResponse> call, @NonNull Response<MeetingPointResponse> response) {
                Log.d("MeetingPointActivity", "API Response success: " + (response.body() != null ? response.body().isSuccess() : "null"));

                // Mulai dengan asumsi tidak ada data
                meetingPoints.clear();
                adapter.notifyDataSetChanged();

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess() && response.body().getData() != null && !response.body().getData().isEmpty()) {
                    meetingPoints.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                    emptyText.setVisibility(View.GONE);
                    rvMeetingPoints.setVisibility(View.VISIBLE);
                    Log.d("MeetingPointActivity", "✅ Data API dimuat: " + meetingPoints.size() + " item.");
                } else {
                    showMeetingPointsNotFound();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MeetingPointResponse> call, @NonNull Throwable t) {
                Log.e("MeetingPointActivity", "API Failure: ", t);
                showMeetingPointsNotFound();
            }
        });
    }

    private void showMeetingPointsNotFound() {
        meetingPoints.clear();
        adapter.notifyDataSetChanged();
        rvMeetingPoints.setVisibility(View.GONE);
        emptyText.setVisibility(View.VISIBLE);
        Log.d("MeetingPointActivity", "❌ Menampilkan Empty State.");
    }

    private static Date parseTanggalTrip(String tanggal, String waktu) {
        if (tanggal == null) return null;
        tanggal = tanggal.trim();
        try {
            if (tanggal.length() > 10) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(tanggal);
            } else if (tanggal.length() == 10 && waktu != null && waktu.length() >= 5) {
                String full = tanggal + " " + waktu;
                if (waktu.length() == 5) full += ":00";
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(full);
            } else {
                return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(tanggal);
            }
        } catch (Exception e) {
            Log.e("MeetingPointActivity", "Gagal parsing tanggal/waktu: " + tanggal + " " + waktu, e);
            return null;
        }
    }

    private static class MeetingPointAdapter extends RecyclerView.Adapter<MeetingPointAdapter.MeetingPointViewHolder> {
        // ... (Kode adapter tidak berubah)

        private List<MeetingPoint> meetingPoints;
        private Context context;

        public MeetingPointAdapter(List<MeetingPoint> meetingPoints, Context context) {
            this.meetingPoints = meetingPoints;
            this.context = context;
        }

        @NonNull
        @Override
        public MeetingPointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_meeting_point, parent, false);
            return new MeetingPointViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MeetingPointViewHolder holder, int position) {
            MeetingPoint mp = meetingPoints.get(position);
            holder.tvLocationName.setText(mp.getNamaLokasi());
            holder.tvLocationAddress.setText(!TextUtils.isEmpty(mp.getAlamatLokasi()) ? mp.getAlamatLokasi() : "-");
            if (!TextUtils.isEmpty(mp.getInformasiTambahan())) {
                holder.tvInfoTambahan.setText(mp.getInformasiTambahan().replace("\\n", "\n"));
            } else {
                holder.tvInfoTambahan.setText("-");
            }

            String mapsQuery = mp.getLinkMap();

            holder.btnOpenMaps.setOnClickListener(v -> {
                if (TextUtils.isEmpty(mapsQuery)) {
                    String fallbackQuery = "geo:0,0?q=" + Uri.encode(mp.getNamaLokasi() + " " + mp.getAlamatLokasi());
                    try {
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackQuery));
                        mapIntent.setPackage("com.google.android.apps.maps");
                        context.startActivity(mapIntent);
                    } catch (Exception e) {
                        Toast.makeText(context, "Gagal membuka Google Maps", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                try {
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapsQuery));
                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);
                } catch (Exception e) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapsQuery));
                        context.startActivity(browserIntent);
                    } catch (Exception ex) {
                        Toast.makeText(context, "Link maps tidak valid", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Timer countdown tiap item
            if (holder.countDownTimer != null) {
                holder.countDownTimer.cancel();
            }

            Date targetDateTime = parseTanggalTrip(mp.getTanggalTrip(), mp.getWaktuKumpul());
            if (targetDateTime != null) {
                long millisTarget = targetDateTime.getTime();
                long millisNow = System.currentTimeMillis();
                long millisUntil = millisTarget - millisNow;
                if (millisUntil < 0) millisUntil = 0;

                holder.countDownTimer = new CountDownTimer(millisUntil, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long days = millisUntilFinished / (1000 * 60 * 60 * 24);
                        long hours = (millisUntilFinished / (1000 * 60 * 60)) % 24;
                        long minutes = (millisUntilFinished / (1000 * 60)) % 60;
                        long seconds = (millisUntilFinished / 1000) % 60;
                        String text = days + " hari " + hours + " jam " + minutes + " menit " + seconds + " detik";
                        holder.tvCountdown.setText(text);
                    }

                    @Override
                    public void onFinish() {
                        holder.tvCountdown.setText("Berangkat Sekarang!");
                    }
                }.start();
            } else {
                holder.tvCountdown.setText("Tanggal tidak valid");
            }
        }

        @Override
        public int getItemCount() {
            return meetingPoints.size();
        }

        @Override
        public void onViewRecycled(@NonNull MeetingPointViewHolder holder) {
            super.onViewRecycled(holder);
            if (holder.countDownTimer != null) {
                holder.countDownTimer.cancel();
                holder.countDownTimer = null;
            }
        }

        static class MeetingPointViewHolder extends RecyclerView.ViewHolder {
            TextView tvLocationName, tvLocationAddress, tvInfoTambahan, tvCountdown;
            Button btnOpenMaps;
            CountDownTimer countDownTimer;

            public MeetingPointViewHolder(@NonNull View itemView) {
                super(itemView);
                tvLocationName = itemView.findViewById(R.id.tvLocationName);
                tvLocationAddress = itemView.findViewById(R.id.tvLocationAddress);
                tvInfoTambahan = itemView.findViewById(R.id.tvInfoTambahan);
                tvCountdown = itemView.findViewById(R.id.tvCountdown);
                btnOpenMaps = itemView.findViewById(R.id.btnOpenMaps);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Timer sudah dibatalkan otomatis lewat onViewRecycled
    }
}