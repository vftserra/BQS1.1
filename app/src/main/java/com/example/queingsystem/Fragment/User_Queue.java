package com.example.queingsystem.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.queingsystem.Adapter.Admin_Time;
import com.example.queingsystem.List.Admin_Time_List;
import com.example.queingsystem.Model_User;
import com.example.queingsystem.R;
import com.example.queingsystem.RequestHandler;
import com.example.queingsystem.SharedPrefManager;
import com.example.queingsystem.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class User_Queue extends Fragment {
    Spinner scheduleSpinner;
    Button btnQueue;
    ProgressDialog progressDialog;
    ArrayList<String> TimeList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_queue, container, false);
        Model_User user = SharedPrefManager.getInstance(this.getContext()).getUser();
        final String userID = String.valueOf(user.getId());

        TimeList = new ArrayList<>();

        scheduleSpinner = v.findViewById(R.id.scheduleSpinner);
        btnQueue = v.findViewById(R.id.btnQueue);

        getTime();
        scheduleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(),
//                        "" + parent.getItemAtPosition(position).toString(),
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                Toast.makeText(getContext(), "No Item Selected !!" , Toast.LENGTH_SHORT).show();
            }
        });

        btnQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spinnerValue = String.valueOf(scheduleSpinner.getSelectedItem());
//                Toast.makeText(getContext(),
//                        "" + String.valueOf(scheduleSpinner.getSelectedItem()),
//                        Toast.LENGTH_SHORT).show();

                addQueue(spinnerValue, userID);
            }
        });
        return v;
    }


    private void addQueue(final String value, final String userID) {

        //if it passes all the validations
        class AddQueue extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("time", value);
                params.put("user_id", userID);
                return requestHandler.sendPostRequest(URLs.URL_ADD_QUEUE, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getContext(), "Loading...", "Adding Queue..",true,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                try{
                    JSONObject obj = new JSONObject(s);
                    Toast.makeText(getContext(), "" + obj.getString("message")
                            + "\nTotal Queue : " + obj.getJSONObject("result").getString("count"),
                            Toast.LENGTH_SHORT).show();
//                    JSONArray resultArray = obj.getJSONArray("result");
//                    for(int i = 0; i < resultArray.length(); i++ ) {
//                        JSONObject o = resultArray.getJSONObject(i);
//                    }
                }catch (JSONException e){

                }
            }
        }
        AddQueue ru = new AddQueue();
        ru.execute();
    }

    private void getTime(){
        class GetTime extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }
                    return sb.toString().trim();
                }catch(Exception e){
                    return null;
                }
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getContext(),"Loading...","Please wait...",
                        false,true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                try {
                    JSONObject obj = new JSONObject(s);
                    JSONArray resultArray = obj.getJSONArray("result");
                    for(int i = 0; i < resultArray.length(); i++ ){
                        JSONObject o = resultArray.getJSONObject(i);
                        String time = o.getString("time");
                        TimeList.add(time);
                    }
                    scheduleSpinner.setAdapter(new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_spinner_dropdown_item, TimeList));
//                    Toast.makeText(getContext(), resultArray.toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        GetTime getImages = new GetTime();
        getImages.execute(URLs.URL_GET_TIME);
    }
}
