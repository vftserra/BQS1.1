package com.example.queingsystem.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.queingsystem.Adapter.Admin_Queue;
import com.example.queingsystem.Adapter.Driver_Queue;
import com.example.queingsystem.List.Admin_Queue_List;
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

public class Admin_Queue_Fragment extends Fragment {
    private RecyclerView RecyclerViewAdminQueue;
    private Admin_Queue AdminQueue;
    private ArrayList<Admin_Queue_List> AdminQueueList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycler_admin_queue, container, false);

        Bundle b = getArguments();
        final String timeID = b.getString("timeID");

        RecyclerViewAdminQueue = v.findViewById(R.id.RecyclerViewAdminQueue);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerViewAdminQueue.setLayoutManager(layoutManager);
        RecyclerViewAdminQueue.setItemAnimator(new DefaultItemAnimator());
        AdminQueueList = new ArrayList<>();

        getAdminQueue(timeID);

        return v;
    }

    private void getAdminQueue(String timeID){
        class GetAdminQueue extends AsyncTask<String,Void,String> {
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
                loading = ProgressDialog.show(getContext(),"Loading Users...","Please wait...",
                        false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

//                Toast.makeText(getContext(), "ADMIN "+ s, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject obj = new JSONObject(s);
                    JSONArray resultArray = obj.getJSONArray("result");
                    for(int i = 0; i < resultArray.length(); i++ ){
                        JSONObject o = resultArray.getJSONObject(i);
                        Admin_Queue_List driver_queue_list = new Admin_Queue_List(
                                o.getString("time"),
                                o.getString("user"),
                                String.valueOf(i+1),
                                o.getString("image")
                        );
                        AdminQueueList.add(driver_queue_list);
                    }

                    AdminQueue = new Admin_Queue(AdminQueueList, getContext());
                    RecyclerViewAdminQueue.setAdapter(AdminQueue);
                    loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        GetAdminQueue getImages = new GetAdminQueue();
        getImages.execute(URLs.URL_GET_ADMIN_QUEUES+timeID);
    }
}
