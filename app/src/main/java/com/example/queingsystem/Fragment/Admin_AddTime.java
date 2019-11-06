package com.example.queingsystem.Fragment;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.queingsystem.Activity.Register;
import com.example.queingsystem.Activity.User_Handler;
import com.example.queingsystem.Model_User;
import com.example.queingsystem.R;
import com.example.queingsystem.RequestHandler;
import com.example.queingsystem.SharedPrefManager;
import com.example.queingsystem.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Admin_AddTime extends Fragment {
    EditText editTextTime;
    Button btnAdd;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.admin_addtime, container, false);

        editTextTime = v.findViewById(R.id.editTextTime);
        btnAdd = v.findViewById(R.id.btnAdd);

        editTextTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);

                        TimePickerDialog datePickerDialog = new TimePickerDialog(getContext(),
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                        if(hourOfDay < 10 || minute < 10){
                                            String hourpadded = String.format("%02d" , hourOfDay);
                                            String minutepadded = String.format("%02d" , minute);
                                            editTextTime.setText(hourpadded+":"+minutepadded+":00");
                                        }else{
                                            editTextTime.setText(hourOfDay+":"+minute+":00");
                                        }
                                    }
                                }, hour, minute, true);
                        datePickerDialog.show();
                        break;
                }
                return false;
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime();
            }
        });

        return v;
    }

    private void addTime() {
        final String time = editTextTime.getText().toString().trim();
        final String role = "User";

        //first we will do the validations
        if (TextUtils.isEmpty(time)) {
            editTextTime.setError("Please enter time");
            editTextTime.requestFocus();
            return;
        }

        //if it passes all the validations
        class AddTime extends AsyncTask<Void, Void, String> {
            ProgressDialog progressDialog;
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("time", time);
                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_ADD_TIME, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressDialog = ProgressDialog.show(getContext(), "Loading...", "Processing User Details..",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
//                progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getContext(), "Time Added !!", Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        AddTime ru = new AddTime();
        ru.execute();
    }
}
