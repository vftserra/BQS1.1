package com.example.queingsystem.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.queingsystem.Adapter.Admin_Time;
import com.example.queingsystem.Adapter.Driver_Time;
import com.example.queingsystem.List.Admin_Time_List;
import com.example.queingsystem.List.Driver_Time_List;
import com.example.queingsystem.R;
import com.example.queingsystem.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Driver_Time_Fragment extends Fragment {

    private RecyclerView RecyclerViewDriverTime;
    private Driver_Time DriverTime;
    private ArrayList<Driver_Time_List> DriverTimeList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycler_admin_time, container, false);

        RecyclerViewDriverTime = v.findViewById(R.id.RecyclerViewDriverQueue);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerViewDriverTime.setLayoutManager(layoutManager);
        RecyclerViewDriverTime.setItemAnimator(new DefaultItemAnimator());
        DriverTimeList = new ArrayList<>();

        getAdminTime();
        return v;
    }

    private void getAdminTime(){
        class GetAdminTime extends AsyncTask<String,Void,String> {
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
                loading = ProgressDialog.show(getContext(),"Loading Schedule...","Please wait...",
                        false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject obj = new JSONObject(s);
                    JSONArray resultArray = obj.getJSONArray("result");
                    for(int i = 0; i < resultArray.length(); i++ ){
                        JSONObject o = resultArray.getJSONObject(i);
                        Driver_Time_List driver_time_list = new Driver_Time_List(
                                o.getString("id"),
                                o.getString("time")
                        );
                        DriverTimeList.add(driver_time_list);
                    }

                    DriverTime = new Driver_Time(DriverTimeList, getContext());
                    RecyclerViewDriverTime.setAdapter(DriverTime);
                    loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        GetAdminTime getImages = new GetAdminTime();
        getImages.execute(URLs.URL_GET_TIME);
    }
}
