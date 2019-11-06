package com.example.queingsystem.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.queingsystem.Fragment.Admin_Home;
import com.example.queingsystem.Fragment.Profile;
import com.example.queingsystem.Fragment.User_BusLocation;
import com.example.queingsystem.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class Admin_Handler extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_container);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(( navListener ));

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Admin_Home()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            return false;
//            Intent selectedFragment;
            Fragment selectedFragments = null;
            switch (item.getItemId()){
                case R.id.bottom_home:
                    selectedFragments = new Admin_Home();
//                    startActivity(selectedFragments);
                    break;
                case R.id.bottom_reservations:
                    selectedFragments = new User_BusLocation();
//                    startActivity(selectedFragments);
                    break;
                case R.id.bottom_profile:
                    selectedFragments = new Profile();
                    break;
//                    startActivity(selectedFragment);
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragments).commit();
            return true;
        }

    };

}
