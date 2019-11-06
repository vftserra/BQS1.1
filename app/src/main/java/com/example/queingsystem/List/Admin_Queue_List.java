package com.example.queingsystem.List;

public class Admin_Queue_List {
    private  String queueID;
    private  String queueTime;
    private  String queueUser;
    private  String queueImg;

    public Admin_Queue_List( String queueTime, String queueUser, String queueID,  String queueImg){
        this.queueTime = queueTime;
        this.queueUser = queueUser;
        this.queueID = queueID;
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
