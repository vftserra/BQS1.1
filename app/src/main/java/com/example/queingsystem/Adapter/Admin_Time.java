package com.example.queingsystem.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.queingsystem.Fragment.Admin_Queue_Fragment;
import com.example.queingsystem.Fragment.Admin_ViewTime;
import com.example.queingsystem.List.Admin_Time_List;
import com.example.queingsystem.R;
import com.example.queingsystem.RequestHandler;
import com.example.queingsystem.URLs;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class Admin_Time extends RecyclerView.Adapter<Admin_Time.MyViewHolder> {

    private List<Admin_Time_List> timeLists;
    private Context context;
    public Admin_Time(List<Admin_Time_List> ListTime, Context context) {
        this.timeLists = ListTime;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView time, id;
        public MyViewHolder(final View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.queueTime);
            id = itemView.findViewById(R.id.queueID);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
        }
    }

    @NonNull
    @Override
    public Admin_Time.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_admin_time, parent, false);
        Admin_Time.MyViewHolder vh = new Admin_Time.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Admin_Time_List queueList = timeLists.get(position);
        holder.time.setText(queueList.getTime());
        holder.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] items = {"Update Schedule","Delete Schedule","View Queues"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                dialog.setTitle("Choose an action.");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            //Update
                            showDialogUpdate(queueList.getTimeID(),queueList.getTime());
                        }
                        if(which == 1) {
                            //Delete
                            showDialogDelete(queueList.getTimeID());
                        }
                        if(which == 2) {
                            //Delete
//                            showDialogDelete(queueList.getTimeID());
                            Bundle args = new Bundle();
                            args.putString("timeID", queueList.getTimeID());
                            Admin_Queue_Fragment fragmentReserve = new Admin_Queue_Fragment();
                            fragmentReserve.setArguments(args);
                            FragmentManager fragmentManager =  ((AppCompatActivity)context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, fragmentReserve).commit();
                        }
                    }
                });
                dialog.show();
                return ;
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeLists.size();
    }

    private void showDialogDelete(final String timeID) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(context);
        dialogDelete.setTitle("Warning!");
        dialogDelete.setMessage("Are you sure you want to delete?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    deleteEvent(timeID);
                }catch(Exception e){
                    Log.e("error",e.getMessage());
                }

            }
        });
        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void deleteEvent(final String timeID){
        //if it passes all the validations
        class DeleteEvent extends AsyncTask<Void, Void, String> {
            ProgressDialog progressDialog;
            @Override

            protected String doInBackground(Void... voids){
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //returing the response
                return requestHandler.sendGetRequest(URLs.URL_DELETE_TIME+timeID);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressDialog = ProgressDialog.show(context, "Loading...", "Deleting Schedule..",
                        true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(context, "Schedule Deleted !!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                Admin_ViewTime fragmentReserve = new Admin_ViewTime();
                FragmentManager fragmentManager =  ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragmentReserve).commit();

            }
        }
        DeleteEvent ru = new DeleteEvent();
        ru.execute();
    }

    private void showDialogUpdate(final String timeID, String time ){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_update_schedule);
        dialog.setTitle("Update Schedule");

        final EditText editTextTime = dialog.findViewById(R.id.editTextTime);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);

        editTextTime.setText(time);

        //SET WIDTH OF DIALOG
        int width = (int)(context.getResources().getDisplayMetrics().widthPixels * 0.95);
        //SET HEIGHT OF DIALOG
        int height = (int)(context.getResources().getDisplayMetrics().heightPixels * 0.4);
        dialog.getWindow().setLayout(width,height);
        dialog.show();

        editTextTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);

                        TimePickerDialog datePickerDialog = new TimePickerDialog(context,
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


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventTime = editTextTime.getText().toString().trim();
                updateTime(timeID, eventTime, dialog);
            }
        });
    }

    private void updateTime(final String timeID, final String time, final Dialog dialog){

        if (TextUtils.isEmpty(time)) {
            Toast.makeText(context, "Enter Time!", Toast.LENGTH_SHORT).show();
            return;
        }

        //if it passes all the validations
        class UpdateEvent extends AsyncTask<Void, Void, String> {
            ProgressDialog progressDialog;
            @Override

            protected String doInBackground(Void... voids){
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("id", timeID);
                params.put("time", time);
                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_UPDATE_TIME, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.dismiss();

                //displaying the progress bar while user registers on the server
                progressDialog = ProgressDialog.show(context, "Loading...", "Updating Time..",
                        true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(context, "Time Updated !!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Admin_ViewTime fragmentReserve = new Admin_ViewTime();
                FragmentManager fragmentManager =  ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragmentReserve).commit();
            }
        }
        //executing the async task
        UpdateEvent ru = new UpdateEvent();
        ru.execute();
    }
}
