package com.example.queingsystem.List;

public class Driver_Queue_List {
    private  String queueID;
    private  String queueTime;
    private  String queueUser;
    private  String queueImg;


    public Driver_Queue_List(String queueID, String queueTime, String queueUser, String queueImg){
        this.queueID = queueID;
        this.queueTime = queueTime;
        this.queueUser = queueUser;
        this.queueImg = queueImg;
    }

    public String getQueueImg() {
        return queueImg;
    }

    public String getQueueID() {
        return queueID;
    }

    public String getQueueTime() {
        return queueTime;
    }

    public String getQueueUser() {
        return queueUser;
    }
}
