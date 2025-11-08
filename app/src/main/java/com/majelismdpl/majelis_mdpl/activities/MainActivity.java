package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

// IMPORT BARU
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.fragments.HistoryFragment;
import com.majelismdpl.majelis_mdpl.fragments.HomeFragment;
import com.majelismdpl.majelis_mdpl.fragments.ProfileFragment;
import com.majelismdpl.majelis_mdpl.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    // (BARU) Ganti semua LinearLayout/ImageView/TextView dengan satu view ini
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cek session login (Ini tetap sama)
        if (!SessionManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // Inisialisasi View
        viewPager = findViewById(R.id.view_pager);
        // (BARU) Temukan BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation_view); // ID dari XML baru

        // Setup ViewPager (Adapter tetap sama)
        setupViewPager();

        // (BARU) Setup sinkronisasi otomatis
        setupNavigationSync();
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // (DISEDERHANAKAN) Hubungkan ViewPager -> BottomNav
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.nav_home);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.nav_history);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                        break;
                }
            }
        });
    }

    // (BARU) Metode untuk sinkronisasi BottomNav -> ViewPager
    private void setupNavigationSync() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                viewPager.setCurrentItem(0, true);
                return true;
            } else if (itemId == R.id.nav_history) {
                viewPager.setCurrentItem(1, true);
                return true;
            } else if (itemId == R.id.nav_profile) {
                viewPager.setCurrentItem(2, true);
                return true;
            }
            return false;
        });
    }

    // HAPUS SEMUA METODE LAMA DI BAWAH INI:
    // - initializeNavigation() <-- ERROR ANDA DI SINI
    // - updateNavFromViewPager()
    // - onNavItemClicked()
    // - setSelectedNav()
    // - resetNavItem()
    // - setNavItemSelected()
    // - animateMenuItem()


    // ViewPager Adapter (Ini tetap sama)
    private class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new HomeFragment();
                case 1:
                    return new HistoryFragment();
                case 2:
                    return new ProfileFragment();
                default:
                    return new HomeFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3; // Jumlah fragment sekarang 3
        }
    }
}