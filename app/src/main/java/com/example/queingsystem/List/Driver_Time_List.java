package com.example.queingsystem.List;

public class Driver_Time_List {
    private  String timeID;
    private  String time;

    public Driver_Time_List(String timeID, String time){
        this.timeID = timeID;
        this.time = time;
    }

    public String getTimeID() {
        return timeID;
    }

    public String getTime() {
        return time;
    }
}
