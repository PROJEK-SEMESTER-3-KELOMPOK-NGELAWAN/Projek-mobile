package com.majelismdpl.majelis_mdpl.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.fragments.HistoryFragment;
import com.majelismdpl.majelis_mdpl.fragments.HomeFragment;
import com.majelismdpl.majelis_mdpl.fragments.ProfileFragment;
import com.majelismdpl.majelis_mdpl.utils.SharedPrefManager;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private LinearLayout navHome, navHistory, navProfile;
    private ImageView iconHome, iconHistory, iconProfile;
    private TextView labelHome, labelHistory, labelProfile;
    private int selectedNavItem = R.id.nav_home;
    private boolean isUserSwipe = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cek session login menggunakan SharedPrefManager (konsisten dengan LoginActivity)
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return; // PENTING: return agar kode di bawah tidak dieksekusi
        }

        setContentView(R.layout.activity_main);

        // Initialize ViewPager2
        viewPager = findViewById(R.id.view_pager);
        setupViewPager();

        // Initialize navigation items
        initializeNavigation();

        // Set default page
        if (savedInstanceState == null) {
            viewPager.setCurrentItem(0, false);
            setSelectedNav(R.id.nav_home);
        }
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Listener untuk sinkronisasi swipe dengan navbar
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (isUserSwipe) {
                    updateNavFromViewPager(position);
                }
            }
        });
    }

    private void updateNavFromViewPager(int position) {
        switch (position) {
            case 0:
                setSelectedNav(R.id.nav_home);
                break;
            case 1:
                setSelectedNav(R.id.nav_history);
                break;
            case 2:
                setSelectedNav(R.id.nav_profile);
                break;
        }
    }

    private void initializeNavigation() {
        // Find all navigation items
        navHome = findViewById(R.id.nav_home);
        navHistory = findViewById(R.id.nav_history);
        navProfile = findViewById(R.id.nav_profile);

        // Find all icons
        iconHome = findViewById(R.id.icon_home);
        iconHistory = findViewById(R.id.icon_history);
        iconProfile = findViewById(R.id.icon_profile);

        // Find all labels
        labelHome = findViewById(R.id.label_home);
        labelHistory = findViewById(R.id.label_history);
        labelProfile = findViewById(R.id.label_profile);

        // Set click listeners
        navHome.setOnClickListener(v -> onNavItemClicked(R.id.nav_home));
        navHistory.setOnClickListener(v -> onNavItemClicked(R.id.nav_history));
        navProfile.setOnClickListener(v -> onNavItemClicked(R.id.nav_profile));
    }

    private void onNavItemClicked(int navId) {
        int position = 0;

        if (navId == R.id.nav_home) {
            position = 0;
        } else if (navId == R.id.nav_history) {
            position = 1;
        } else if (navId == R.id.nav_profile) {
            position = 2;
        }

        isUserSwipe = false;
        viewPager.setCurrentItem(position, true);
        setSelectedNav(navId);
        isUserSwipe = true;
    }

    private void setSelectedNav(int navId) {
        selectedNavItem = navId;

        // Reset all items to unselected state
        resetNavItem(navHome, iconHome, labelHome);
        resetNavItem(navHistory, iconHistory, labelHistory);
        resetNavItem(navProfile, iconProfile, labelProfile);

        // Set selected item
        if (navId == R.id.nav_home) {
            setNavItemSelected(navHome, iconHome, labelHome);
        } else if (navId == R.id.nav_history) {
            setNavItemSelected(navHistory, iconHistory, labelHistory);
        } else if (navId == R.id.nav_profile) {
            setNavItemSelected(navProfile, iconProfile, labelProfile);
        }
    }

    private void resetNavItem(LinearLayout navItem, ImageView icon, TextView label) {
        // Reset background ke transparent
        navItem.setBackgroundResource(R.drawable.nav_item_background);
        icon.setColorFilter(ContextCompat.getColor(this, R.color.navUnselected));
        label.setVisibility(View.GONE);
        label.setTextColor(ContextCompat.getColor(this, R.color.navUnselected));
    }

    private void setNavItemSelected(LinearLayout navItem, ImageView icon, TextView label) {
        // Get primary color from resources
        int primaryColor = ContextCompat.getColor(this, R.color.navSelected);

        // Set background coklat untuk item yang dipilih
        navItem.setBackgroundResource(R.drawable.nav_item_selected_bg);

        icon.setColorFilter(primaryColor);
        label.setVisibility(View.VISIBLE);
        label.setTextColor(primaryColor);

        // Animate icon
        animateMenuItem(icon);
    }

    // Fungsi animasi scale pada ikon yang dipilih
    private void animateMenuItem(@NonNull ImageView iconView) {
        // Membuat animasi zoom in, lalu zoom out
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(iconView, View.SCALE_X, 1f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(iconView, View.SCALE_Y, 1f, 1.2f, 1f);
        scaleX.setDuration(250);
        scaleY.setDuration(250);
        scaleX.start();
        scaleY.start();
    }

    // ViewPager Adapter
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
                    return new HomeFragment(); // Fallback
            }
        }

        @Override
        public int getItemCount() {
            return 3; // Jumlah fragment sekarang 3
        }
    }
}