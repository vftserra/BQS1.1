package com.example.queingsystem.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.queingsystem.R;
import com.example.queingsystem.RequestHandler;
import com.example.queingsystem.URLs;
//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Admin_Home extends Fragment {
    CardView timeCard, userCard, deleteCard;
//    private IntentIntegrator qrScan;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.admin_home, container, false);

        timeCard = v.findViewById(R.id.timeCard);
        deleteCard = v.findViewById(R.id.deleteCard);
        userCard = v.findViewById(R.id.userCard);

        timeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] items = {"View Schedules","Add Schedule"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

                dialog.setTitle("Choose an action.");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            //View
                            Admin_ViewTime viewTime = new Admin_ViewTime();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, viewTime).commit();
                        }
                        if(which == 1) {
                            //Add
                            Admin_AddTime addTime = new Admin_AddTime();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, addTime).commit();
                        }
                    }
                });
                dialog.show();
                return ;
            }
        });

        deleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] items = {"Delete Queues"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

                dialog.setTitle("Are you sure to delete?.");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            deleteQueues();
                        }

                    }
                });
                dialog.show();
                return ;
            }
        });

        userCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Driver_Time_Fragment viewTime = new Driver_Time_Fragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, viewTime).commit();
            }
        });
        return v;
    }


    private void deleteQueues(){
        //if it passes all the validations
        class DeleteQueues extends AsyncTask<Void, Void, String> {
            ProgressDialog progressDialog;
            @Override

            protected String doInBackground(Void... voids){
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //returing the response
                return requestHandler.sendGetRequest(URLs.URL_DELETE_QUEUES);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressDialog = ProgressDialog.show(getContext(), "Loading...", "Deleting Queues..",
                        true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getContext(), "Queues Deleted !!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
        DeleteQueues ru = new DeleteQueues();
        ru.execute();
    }




}
