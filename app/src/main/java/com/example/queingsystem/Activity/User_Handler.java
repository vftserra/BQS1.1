package com.example.queingsystem.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.queingsystem.Fragment.Admin_Home;
import com.example.queingsystem.Fragment.Admin_ViewTime;
import com.example.queingsystem.Fragment.Driver_Queue_Fragment;
import com.example.queingsystem.Fragment.Driver_Time_Fragment;
import com.example.queingsystem.Fragment.Profile;
import com.example.queingsystem.Fragment.User_BusLocation;
import com.example.queingsystem.Fragment.User_Queue;
import com.example.queingsystem.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class User_Handler extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_container);

        BottomNavigationView bottomNavigationView = findViewById(R.id.user_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(( navListener ));

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new User_Queue()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            return false;
//            Intent selectedFragment;
                    Fragment selectedFragments = null;
                    switch (item.getItemId()){
                        case R.id.home:
                            selectedFragments = new User_BusLocation();
//                    startActivity(selectedFragments);
                            break;
                        case R.id.reservation:
                            selectedFragments = new User_Queue();
//                    startActivity(selectedFragments);
                            break;
                        case R.id.queues:
                            selectedFragments = new Driver_Time_Fragment();
                            break;
                        case R.id.schedule:
                            selectedFragments = new Profile();
                            break;
//                    startActivity(selectedFragment);
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragments).commit();
                    return true;
                }

            };

}
