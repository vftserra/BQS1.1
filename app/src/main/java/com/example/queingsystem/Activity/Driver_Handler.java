package com.example.queingsystem.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.queingsystem.Fragment.Admin_Home;
import com.example.queingsystem.Fragment.Driver_Home;
import com.example.queingsystem.Fragment.Driver_Queue_Fragment;
import com.example.queingsystem.Fragment.Driver_Time_Fragment;
import com.example.queingsystem.Fragment.Profile;
import com.example.queingsystem.Fragment.User_BusLocation;
import com.example.queingsystem.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class Driver_Handler extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_container);

        BottomNavigationView bottomNavigationView = findViewById(R.id.driver_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(( navListener ));

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Driver_Home()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragments = null;
                    switch (item.getItemId()){
                        case R.id.profile:
                            selectedFragments = new Profile();
//                    startActivity(selectedFragments);
                            break;
                        case R.id.location:
                            selectedFragments = new User_BusLocation();
                            break;
                        case R.id.sendLocation:
                            selectedFragments = new Driver_Home();
                            break;
                        case R.id.queue:
                            selectedFragments = new Driver_Time_Fragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragments).commit();
                    return true;
                }

            };
}
