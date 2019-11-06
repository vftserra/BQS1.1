package com.example.queingsystem.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.queingsystem.Model_User;
import com.example.queingsystem.R;
import com.example.queingsystem.SharedPrefManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Profile extends Fragment {
    TextView txtUsername, txtEmail;
    Button btnLogout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile, container, false);

        txtUsername = v.findViewById(R.id.txtUsername);
        txtEmail =  v.findViewById(R.id.txtEmail);
        btnLogout = v.findViewById(R.id.btnLogout);

        Model_User user = SharedPrefManager.getInstance(this.getContext()).getUser();

        txtUsername.setText(user.getUsername());
        txtEmail.setText(user.getEmail());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(getContext()).logout();
            }
        });

        return v;
    }
}
