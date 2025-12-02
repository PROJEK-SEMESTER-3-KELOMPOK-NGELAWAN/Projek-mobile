package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.adapters.TripDokumentasiAdapter;
import com.majelismdpl.majelis_mdpl.api.ApiClient;
import com.majelismdpl.majelis_mdpl.api.ApiService;
import com.majelismdpl.majelis_mdpl.models.TripDokumentasiResponse;
import com.majelismdpl.majelis_mdpl.utils.Constants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DokumentasiActivity extends AppCompatActivity {

    private static final String TAG = "DokumentasiActivity";

    // UI Components
    private ProgressBar progressBar;
    private RecyclerView rvDokumentasi;
    private CardView emptyStateCard;
    private TextView tvEmptyTitle, tvEmptyDescription;
    private ImageView ivEmptyIcon;
    private MaterialButton btnRetry;

    // Data
    private int userId = 0;
    private TripDokumentasiAdapter adapter;
    private List<TripDokumentasiResponse.DokumentasiData> tripList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dokumentasi);

        initViews();
        setupToolbar();
        // Hapus setupWindowInsets() karena sudah dihandle oleh EdgeToEdge dan fitsSystemWindows="true"

        getUserId();

        if (userId > 0) {
            loadDokumentasi();
        }
    }

    private void initViews() {
        progressBar = findViewById(R.id.progress_bar);
        rvDokumentasi = findViewById(R.id.rv_dokumentasi);
        emptyStateCard = findViewById(R.id.empty_state_card);
        tvEmptyTitle = findViewById(R.id.tv_empty_title);
        tvEmptyDescription = findViewById(R.id.tv_empty_description);
        ivEmptyIcon = findViewById(R.id.iv_empty_icon);
        btnRetry = findViewById(R.id.btn_retry);

        // Setup RecyclerView
        rvDokumentasi.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TripDokumentasiAdapter(this, tripList, trip -> openGoogleDrive(trip.getGdriveLink()));
        rvDokumentasi.setAdapter(adapter);

        btnRetry.setOnClickListener(v -> loadDokumentasi());
    }

    private void setupToolbar() {
        // PERBAIKAN: Gunakan ID toolbar yang sebenarnya (R.id.toolbar)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Perbaikan ini (setTitle) tidak diperlukan jika menggunakan app:title di XML
            // Namun, karena ada di kode Anda, kita biarkan saja (atau hapus jika ingin lebih bersih)
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Dokumentasi Trip");
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void getUserId() {
        try {
            SharedPreferences sharedPref = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
            String userDataJson = sharedPref.getString(Constants.KEY_USER_DATA, "");

            if (!userDataJson.isEmpty()) {
                JSONObject userData = new JSONObject(userDataJson);
                if (userData.has("id_user")) {
                    userId = userData.getInt("id_user");
                    Log.d(TAG, "✅ User ID: " + userId);
                    return;
                }
            }

            userId = sharedPref.getInt("id_user", 0);
            if (userId > 0) {
                Log.d(TAG, "✅ User ID from key 'id_user': " + userId);
                return;
            }

            Log.e(TAG, "❌ User ID tidak ditemukan");
            showEmptyState("Sesi Tidak Valid", "Silakan login kembali.", false);

        } catch (Exception e) {
            Log.e(TAG, "❌ Error getting user ID: " + e.getMessage());
            showEmptyState("Error", "Gagal membaca data user.", false);
        }
    }

    private void loadDokumentasi() {
        if (userId <= 0) {
            showEmptyState("Sesi Tidak Valid", "Silakan login kembali.", false);
            return;
        }

        showLoading(true);
        Log.d(TAG, "🔄 Loading dokumentasi for User ID: " + userId);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<TripDokumentasiResponse> call = apiService.getTripDokumentasi(userId);

        call.enqueue(new Callback<TripDokumentasiResponse>() {
            @Override
            public void onResponse(Call<TripDokumentasiResponse> call, Response<TripDokumentasiResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    TripDokumentasiResponse dokumentasiResponse = response.body();

                    if (dokumentasiResponse.isSuccess() && dokumentasiResponse.getData() != null && !dokumentasiResponse.getData().isEmpty()) {
                        // ✅ Ada trip yang DONE
                        tripList.clear();
                        tripList.addAll(dokumentasiResponse.getData());
                        adapter.notifyDataSetChanged();

                        showTripList();

                        Log.d(TAG, "✅ Loaded " + tripList.size() + " trips");

                    } else {
                        // ❌ Tidak ada trip
                        String message = dokumentasiResponse.getMessage();
                        String status = dokumentasiResponse.getStatus();

                        if ("trip_not_done".equals(status)) {
                            showEmptyState("Trip Belum Selesai", "Anda belum memiliki trip yang selesai.", false);
                        } else {
                            // Mengambil pesan default dari layout jika API tidak menyediakan pesan yang spesifik
                            showEmptyState("Dokumentasi Tidak Tersedia", "Anda belum memiliki trip yang selesai.", false);
                        }
                    }
                } else {
                    showEmptyState("Gagal Memuat Data", "Terjadi kesalahan. Silakan coba lagi.", true);
                }
            }

            @Override
            public void onFailure(Call<TripDokumentasiResponse> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "❌ Error: " + t.getMessage());
                showEmptyState("Koneksi Gagal", "Tidak dapat terhubung ke server.", true);
            }
        });
    }

    private void openGoogleDrive(String gdriveLink) {
        if (gdriveLink == null || gdriveLink.isEmpty()) {
            Toast.makeText(this, "Link Google Drive tidak tersedia", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(gdriveLink));
            startActivity(intent);
            Log.d(TAG, "📂 Opening: " + gdriveLink);
        } catch (Exception e) {
            Log.e(TAG, "❌ Error opening Drive: " + e.getMessage());
            Toast.makeText(this, "Gagal membuka Google Drive.", Toast.LENGTH_LONG).show();
        }
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvDokumentasi.setVisibility(View.GONE);
        emptyStateCard.setVisibility(View.GONE);
    }

    private void showTripList() {
        progressBar.setVisibility(View.GONE);
        rvDokumentasi.setVisibility(View.VISIBLE);
        emptyStateCard.setVisibility(View.GONE);
    }

    private void showEmptyState(String title, String message, boolean showRetry) {
        progressBar.setVisibility(View.GONE);
        rvDokumentasi.setVisibility(View.GONE);
        emptyStateCard.setVisibility(View.VISIBLE);

        tvEmptyTitle.setText(title);
        // Mengubah pesan deskripsi di empty state
        tvEmptyDescription.setText(message);
        btnRetry.setVisibility(showRetry ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userId > 0) {
            Log.d(TAG, "🔄 onResume() - Reload");
            loadDokumentasi();
        }
    }
}