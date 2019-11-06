package com.example.queingsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.queingsystem.List.Driver_Queue_List;
import com.example.queingsystem.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Driver_Queue extends RecyclerView.Adapter<Driver_Queue.MyViewHolder> {

    private List<Driver_Queue_List> queueLists;
    private Context context;
    public Driver_Queue(List<Driver_Queue_List> ListQueue, Context context) {
        this.queueLists = ListQueue;
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
    public Driver_Queue.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_driver_queue, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Driver_Queue.MyViewHolder holder, int position) {
        final Driver_Queue_List queueList = queueLists.get(position);
        holder.time.setText(queueList.getQueueTime());
        holder.user.setText(queueList.getQueueUser());
        holder.id.setText(queueList.getQueueID());
        Glide.with(context).load(queueList.getQueueImg() ).into(holder.imgView);

//        Toast.makeText(context, queueList.getQueueUser(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return queueLists.size();
    }


}
