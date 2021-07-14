package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.myapplication.menuFragments.HomeFragment;
import com.example.myapplication.menuFragments.OrderFragment;
import com.example.myapplication.menuFragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hooks
        bottomNavigationView = findViewById(R.id.bottom_navigation_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_layout, new HomeFragment()).commit();
    }

    //Switch fragment according to selected navigation menu
    final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {

                case R.id.menu_home:
                    selectedFragment = new HomeFragment();
                    break;

                case R.id.menu_orders:
                    selectedFragment = new OrderFragment();
                    break;

                case R.id.menu_profile:
                    selectedFragment = new ProfileFragment();
                    break;

            }
            //display fragment code

            getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_layout, selectedFragment).commit();
            return true;
        }
    };

}

// Material Bottom navigation bar in Android||Customize Material Bottom navigation bar||Tutorial-2020
// https://www.youtube.com/watch?v=6U_vgrwZ4Lc
// BottomNavigationBar with Fragments,Backstack||BackStack Like PhonePe App||Android Tutorial-2020
// https://www.youtube.com/watch?v=1GkSLwcGZhc
