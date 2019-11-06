package com.example.queingsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.queingsystem.List.Admin_Queue_List;
import com.example.queingsystem.List.Driver_Queue_List;
import com.example.queingsystem.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Admin_Queue extends RecyclerView.Adapter<Admin_Queue.MyViewHolder> {

    private List<Admin_Queue_List> adminQueueList;
    private Context context;
    public Admin_Queue(List<Admin_Queue_List> ListQueue, Context context) {
        this.adminQueueList = ListQueue;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView time,user,id;
        ImageView imgView;

        public MyViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.queueTime);
            user = itemView.findViewById(R.id.queueUser);
            id = itemView.findViewById(R.id.queueID);
            imgView = itemView.findViewById(R.id.imageEvent);

        }
    }

    @NonNull
    @Override
    public Admin_Queue.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_admin_queue, parent, false);
        // set the view's size, margins, paddings and layout parameters
        Admin_Queue.MyViewHolder vh = new Admin_Queue.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Admin_Queue.MyViewHolder holder, int position) {
        final Admin_Queue_List queueList = adminQueueList.get(position);
        holder.time.setText(queueList.getQueueTime());
        holder.user.setText(queueList.getQueueUser());
        holder.id.setText(queueList.getQueueID());
        Glide.with(context).load(queueList.getQueueImg()).into(holder.imgView);

    }

    @Override
    public int getItemCount() {
        return adminQueueList.size();
    }
}
