package com.example.queingsystem.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.queingsystem.Adapter.Driver_Queue;
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

public class Driver_Queue_Fragment extends Fragment {

    private RecyclerView RecyclerViewDriverQueue;
    private Driver_Queue DriverQueue;
    private ArrayList<Driver_Queue_List> DriverQueueList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.recycler_driver_queue, container, false);

        RecyclerViewDriverQueue = view.findViewById(R.id.RecyclerViewDriverQueue);
//        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerViewDriverQueue.setLayoutManager(layoutManager);
        RecyclerViewDriverQueue.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        DriverQueueList = new ArrayList<>();

        getDriverQueues();
        return view;
    }

    private void getDriverQueues(){
        class GetDriverQueues extends AsyncTask<String,Void,String> {
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
                loading.dismiss();

//                Toast.makeText(getContext(), "DRIVER", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject obj = new JSONObject(s);
                    JSONArray resultArray = obj.getJSONArray("result");
                    for(int i = 0; i < resultArray.length(); i++ ){
                        JSONObject o = resultArray.getJSONObject(i);
                        Driver_Queue_List driver_queue_list = new Driver_Queue_List(
                                String.valueOf(i+1),
                                o.getString("time"),
                                o.getString("user"),
                                o.getString("image")
                        );
                        DriverQueueList.add(driver_queue_list);
                    }

                    DriverQueue = new Driver_Queue(DriverQueueList, getContext());
                    RecyclerViewDriverQueue.setAdapter(DriverQueue);
                    loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        GetDriverQueues getImages = new GetDriverQueues();
        getImages.execute(URLs.URL_GET_DRIVER_QUEUES);
    }

}
