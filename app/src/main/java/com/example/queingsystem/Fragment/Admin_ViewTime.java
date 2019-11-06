package com.example.queingsystem.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.queingsystem.Adapter.Admin_Time;
import com.example.queingsystem.Adapter.Driver_Queue;
import com.example.queingsystem.List.Admin_Time_List;
import com.example.queingsystem.List.Driver_Queue_List;
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

public class Admin_ViewTime extends Fragment {

    private RecyclerView RecyclerViewAdminTime;
    private Admin_Time AdminTime;
    private ArrayList<Admin_Time_List> AdminTimeList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycler_admin_time, container, false);

        RecyclerViewAdminTime = v.findViewById(R.id.RecyclerViewDriverQueue);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerViewAdminTime.setLayoutManager(layoutManager);
        RecyclerViewAdminTime.setItemAnimator(new DefaultItemAnimator());
        AdminTimeList = new ArrayList<>();

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
                        Admin_Time_List admin_time_list = new Admin_Time_List(
                                o.getString("id"),
                                o.getString("time")
                        );
                        AdminTimeList.add(admin_time_list);
                    }

                    AdminTime = new Admin_Time(AdminTimeList, getContext());
                    RecyclerViewAdminTime.setAdapter(AdminTime);
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
