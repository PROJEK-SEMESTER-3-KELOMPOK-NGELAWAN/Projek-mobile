package com.majelismdpl.majelis_mdpl.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.majelismdpl.majelis_mdpl.activities.PesertaTripActivity;
import com.majelismdpl.majelis_mdpl.activities.MeetingPointActivity;
import com.majelismdpl.majelis_mdpl.databinding.FragmentHomeBinding;
import com.majelismdpl.majelis_mdpl.databinding.ItemTripBinding;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.utils.ApiConfig;
import com.majelismdpl.majelis_mdpl.utils.SessionManager;
import com.majelismdpl.majelis_mdpl.models.Trip;
import com.majelismdpl.majelis_mdpl.models.User;
import com.majelismdpl.majelis_mdpl.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private SessionManager sessionManager;
    private RequestQueue requestQueue;
    private List<Trip> tripList = new ArrayList<>();
    private TripsAdapter tripsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    // Lebar card item fix (harus sama dengan item_trip.xml)
    private int itemWidthPx = 0;
    private boolean hasCenteredFirstItem = false;

    // Tambahan: komponen empty state
    private LinearLayout layoutEmptyState;
    private MaterialButton btnCariTripBaru;
    private MaterialButton btnLihatRiwayat;

    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = SessionManager.getInstance(requireContext());
        requestQueue = Volley.newRequestQueue(requireContext());

        tripsAdapter = new TripsAdapter(tripList);
        LinearLayoutManager lm = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvTrips.setLayoutManager(lm);
        binding.rvTrips.setAdapter(tripsAdapter);

        int spacingPx = Math.round(16 * getResources().getDisplayMetrics().density);
        int edgePaddingPx = Math.round(56 * getResources().getDisplayMetrics().density);

        binding.rvTrips.setClipToPadding(false);
        binding.rvTrips.setPadding(edgePaddingPx, 0, edgePaddingPx, 0);

        binding.rvTrips.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                int itemCount = parent.getAdapter() != null ? parent.getAdapter().getItemCount() : 0;
                if (position == 0) {
                    outRect.left = 0;
                    outRect.right = spacingPx / 2;
                } else if (position == itemCount - 1) {
                    outRect.left = spacingPx / 2;
                    outRect.right = 0;
                } else {
                    outRect.left = spacingPx / 2;
                    outRect.right = spacingPx / 2;
                }
            }
        });

        new LinearSnapHelper().attachToRecyclerView(binding.rvTrips);

        // Samakan dengan item_trip.xml layout_width
        itemWidthPx = Math.round(310 * getResources().getDisplayMetrics().density);

        binding.rvTrips.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                binding.rvTrips.removeOnLayoutChangeListener(this);
                centerFirstItemIfPossible();
            }
        });

        swipeRefreshLayout = requireView().findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (getActivity() != null) {
                getActivity().recreate();
            }
        });

        // Tambahan: binding empty state dan tombol
        layoutEmptyState = binding.getRoot().findViewById(R.id.layoutEmptyState);
        btnCariTripBaru = binding.getRoot().findViewById(R.id.btnCariTripBaru);

        btnCariTripBaru.setOnClickListener(v -> {
            String url = ApiConfig.getNgrokWebUrl();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        });


        loadUserData();
        loadTripData();

        binding.menuTitikKumpul.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), MeetingPointActivity.class)));

        binding.menuPesertaTrip.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), PesertaTripActivity.class)));
    }

    private void centerFirstItemIfPossible() {
        if (hasCenteredFirstItem) return;
        if (!(binding.rvTrips.getLayoutManager() instanceof LinearLayoutManager)) return;

        LinearLayoutManager lm = (LinearLayoutManager) binding.rvTrips.getLayoutManager();
        int recyclerViewWidth = binding.rvTrips.getWidth();
        int offset = (recyclerViewWidth - itemWidthPx) / 2;

        lm.scrollToPositionWithOffset(0, offset);
        hasCenteredFirstItem = true;
        Log.d(TAG, "Centering offset: " + offset);
    }

    private void loadUserData() {
        User user = sessionManager.getUser();
        if (user != null) {
            String username = user.getUsername();
            if (username != null && !username.isEmpty()) {
                binding.tvUsername.setText(capitalizeFirstLetter(username));
            } else {
                binding.tvUsername.setText("Pengguna");
            }
            binding.tvGreeting.setText("Selamat datang kembali,");
        } else {
            binding.tvUsername.setText("Guest");
            binding.tvGreeting.setText("Selamat datang,");
        }
    }

    private void loadTripData() {
        User user = sessionManager.getUser();
        if (user == null) {
            setDefaultTripData();
            swipeRefreshLayout.setRefreshing(false);
            layoutEmptyState.setVisibility(View.VISIBLE);
            binding.rvTrips.setVisibility(View.GONE);
            return;
        }
        String url = Constants.getUserTripUrl();
        Log.d(TAG, "Request get-user-trip: " + url);

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("id_user", user.getId());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    requestBody,
                    response -> {
                        try {
                            boolean success = response.optBoolean("success", false);
                            if (success && response.has("data") && !response.isNull("data")) {
                                JSONArray dataArray = response.getJSONArray("data");
                                tripList.clear();
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject tripJson = dataArray.getJSONObject(i);
                                    Trip trip = parseTripFromJson(tripJson);
                                    tripList.add(trip);
                                }
                                tripsAdapter.notifyDataSetChanged();
                                if (!tripList.isEmpty()) {
                                    updateTripUI(tripList.get(0));
                                    layoutEmptyState.setVisibility(View.GONE);
                                    binding.rvTrips.setVisibility(View.VISIBLE);
                                } else {
                                    setDefaultTripData();
                                    layoutEmptyState.setVisibility(View.VISIBLE);
                                    binding.rvTrips.setVisibility(View.GONE);
                                }
                            } else {
                                setDefaultTripData();
                                layoutEmptyState.setVisibility(View.VISIBLE);
                                binding.rvTrips.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            setDefaultTripData();
                            layoutEmptyState.setVisibility(View.VISIBLE);
                            binding.rvTrips.setVisibility(View.GONE);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    },
                    error -> {
                        setDefaultTripData();
                        layoutEmptyState.setVisibility(View.VISIBLE);
                        binding.rvTrips.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                    }
            );
            requestQueue.add(request);
        } catch (JSONException e) {
            setDefaultTripData();
            layoutEmptyState.setVisibility(View.VISIBLE);
            binding.rvTrips.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private Trip parseTripFromJson(JSONObject data) throws JSONException {
        Trip trip = new Trip();
        trip.setIdBooking(data.optInt("id_booking"));
        trip.setIdTrip(data.optInt("id_trip"));
        trip.setNamaGunung(data.optString("nama_gunung", "-"));
        trip.setJenisTrip(data.optString("jenis_trip", "-"));
        trip.setTanggalTrip(data.optString("tanggal_trip", "-"));
        trip.setTanggalDisplay(data.optString("tanggal_display", "-"));
        trip.setDurasi(data.optString("durasi", "-"));
        trip.setViaGunung(data.optString("via_gunung", "-"));
        trip.setGambarUrl(data.optString("gambar_url", ""));
        trip.setJumlahOrang(data.optInt("jumlah_orang", 1));
        trip.setTotalHarga(data.optInt("total_harga", 0));
        trip.setStatusBooking(data.optString("status_booking", ""));
        trip.setStatusPembayaran(data.optString("status_pembayaran", ""));
        trip.setNamaLokasi(data.optString("nama_lokasi", "-"));
        trip.setWaktuKumpul(data.optString("waktu_kumpul", "-"));
        trip.setLinkMap(data.optString("link_map", ""));
        return trip;
    }

    private void updateTripUI(Trip trip) {
        if (trip != null && binding != null) {
            binding.tvStatusPembayaran.setText(trip.getStatusPembayaran());
            if ("Lunas".equalsIgnoreCase(trip.getStatusPembayaran())) {
                binding.tvStatusPembayaran.setBackgroundResource(R.drawable.bg_status_lunas);
            } else {
                binding.tvStatusPembayaran.setBackgroundResource(R.drawable.bg_status_belum_lunas);
            }
        }
    }

    private void setDefaultTripData() {
        if (binding != null) {
            binding.tvStatusPembayaran.setText("Belum Ada");
            binding.tvStatusPembayaran.setBackgroundResource(R.drawable.bg_status_belum_lunas);
        }
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripViewHolder> {
        private final List<Trip> trips;

        public TripsAdapter(List<Trip> trips) {
            this.trips = trips;
        }

        @NonNull
        @Override
        public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemTripBinding itemBinding = ItemTripBinding.inflate(inflater, parent, false);
            return new TripViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
            Trip trip = trips.get(position);
            holder.bind(trip);
        }

        @Override
        public int getItemCount() {
            return trips.size();
        }

        class TripViewHolder extends RecyclerView.ViewHolder {
            private final ItemTripBinding itemBinding;

            public TripViewHolder(ItemTripBinding binding) {
                super(binding.getRoot());
                itemBinding = binding;
            }

            void bind(Trip trip) {
                itemBinding.tvTripTitle.setText(trip.getNamaGunung());
                itemBinding.tvTripDate.setText(trip.getTanggalDisplay());

                if (trip.getGambarUrl() != null && !trip.getGambarUrl().isEmpty()) {
                    Glide.with(itemBinding.getRoot())
                            .load(trip.getGambarUrl())
                            .placeholder(R.drawable.ic_gunung_ijen)
                            .error(R.drawable.ic_gunung_ijen)
                            .centerCrop()
                            .into(itemBinding.ivTripImage);
                } else {
                    itemBinding.ivTripImage.setImageResource(R.drawable.ic_gunung_ijen);
                }
            }
        }
    }
}
