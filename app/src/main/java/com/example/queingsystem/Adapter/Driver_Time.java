package com.example.queingsystem.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.queingsystem.Fragment.Admin_Queue_Fragment;
import com.example.queingsystem.List.Driver_Time_List;
import com.example.queingsystem.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class Driver_Time extends RecyclerView.Adapter<Driver_Time.MyViewHolder> {

    private List<Driver_Time_List> timeLists;
    private Context context;
    public Driver_Time(List<Driver_Time_List> ListTime, Context context) {
        this.timeLists = ListTime;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView time, id;
        public MyViewHolder(final View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.queueTime);
            id = itemView.findViewById(R.id.queueID);
        }
    }

    @NonNull
    @Override
    public Driver_Time.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_admin_time, parent, false);
        Driver_Time.MyViewHolder vh = new Driver_Time.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Driver_Time.MyViewHolder holder, int position) {
        final Driver_Time_List queueList = timeLists.get(position);
        holder.time.setText(queueList.getTime());

        holder.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] items = {"View Queues"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                dialog.setTitle("Choose an action.");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(which == 0) {
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
}
