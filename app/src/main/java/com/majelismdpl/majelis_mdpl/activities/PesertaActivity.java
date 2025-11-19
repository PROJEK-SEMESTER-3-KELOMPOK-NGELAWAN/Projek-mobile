package com.majelismdpl.majelis_mdpl.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.api.ApiClient;
import com.majelismdpl.majelis_mdpl.api.ApiService;
import com.majelismdpl.majelis_mdpl.models.PesertaHistory;
import com.majelismdpl.majelis_mdpl.models.PesertaAdapterHistory;
import com.majelismdpl.majelis_mdpl.models.TripParticipantsHistoryResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesertaActivity extends AppCompatActivity {
    private int idTrip = -1;
    private PesertaAdapterHistory pesertaAdapter;
    private RecyclerView rvPeserta;
    private TextView tvPesertaTitle, tvPesertaEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peserta);

        Toolbar toolbar = findViewById(R.id.toolbar_peserta);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        rvPeserta = findViewById(R.id.rv_peserta);
        tvPesertaTitle = findViewById(R.id.tv_peserta_trip_title);
        tvPesertaEmpty = findViewById(R.id.tvEmptyState);

        pesertaAdapter = new PesertaAdapterHistory();
        rvPeserta.setLayoutManager(new LinearLayoutManager(this));
        rvPeserta.setAdapter(pesertaAdapter);

        idTrip = getIntent().getIntExtra("TRIP_ID", -1);

        if (tvPesertaTitle != null) {
            tvPesertaTitle.setText(idTrip > 0 ? "Peserta Trip #" + idTrip : "Peserta Trip");
        }

        if (idTrip > 0) {
            loadPesertaFromApi(idTrip);
        } else {
            showEmptyState("ID Trip tidak ditemukan.");
        }
    }

    private void loadPesertaFromApi(int idTrip) {
        ApiService apiService = ApiClient.getInstance().create(ApiService.class);
        Call<TripParticipantsHistoryResponse> call = apiService.getTripParticipantsHistory(idTrip);

        call.enqueue(new Callback<TripParticipantsHistoryResponse>() {
            @Override
            public void onResponse(Call<TripParticipantsHistoryResponse> call, Response<TripParticipantsHistoryResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<PesertaHistory> pesertaList = response.body().getData();
                    if (pesertaList != null && !pesertaList.isEmpty()) {
                        pesertaAdapter.submitList(pesertaList);
                        rvPeserta.setVisibility(View.VISIBLE);
                        if (tvPesertaEmpty != null) tvPesertaEmpty.setVisibility(View.GONE);
                        tvPesertaTitle.setText("Peserta Trip (" + pesertaList.size() + ")");
                    } else {
                        showEmptyState("Tidak ada peserta di trip ini.");
                    }
                } else {
                    showEmptyState("Gagal memuat data peserta.");
                    Toast.makeText(PesertaActivity.this, "Gagal memuat data peserta.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TripParticipantsHistoryResponse> call, Throwable t) {
                showEmptyState("Koneksi gagal: " + t.getMessage());
                Toast.makeText(PesertaActivity.this, "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEmptyState(String message) {
        rvPeserta.setVisibility(View.GONE);
        if (tvPesertaEmpty != null) {
            tvPesertaEmpty.setVisibility(View.VISIBLE);
            tvPesertaEmpty.setText(message);
        } else if (tvPesertaTitle != null) {
            tvPesertaTitle.setText(message);
        }
    }
}
