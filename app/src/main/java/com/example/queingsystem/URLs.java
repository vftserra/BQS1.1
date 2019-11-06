package com.example.queingsystem;

public class URLs {

    private static final String ROOT_URL = "http://bodegafoodhub.com/queing/Api/Api.php?apicall=";

    public static final String URL_REGISTER = ROOT_URL + "register";
    public static final String URL_LOGIN = ROOT_URL + "checkUserLogin";
    public static final String URL_ADD_LOCATION = ROOT_URL + "addLocation";
    public static final String URL_GET_LOCATION = ROOT_URL + "getLocation";
    public static final String URL_ADD_QUEUE = ROOT_URL + "addQueue";
    public static final String URL_GET_DRIVER_QUEUES = ROOT_URL + "getDriverQueues";
    public static final String URL_ADD_TIME = ROOT_URL + "addTime";
    public static final String URL_GET_TIME = ROOT_URL + "getTime";
    public static final String URL_UPDATE_TIME = ROOT_URL + "updateTime";
    public static final String URL_DELETE_TIME = ROOT_URL + "deleteTime&id=";
    public static final String URL_GET_ADMIN_QUEUES = ROOT_URL + "getAdminQueue&id=";
    public static final String URL_DELETE_QUEUES = ROOT_URL + "deleteQueues";
    public static final String URL_VALIDATE_QR = ROOT_URL + "checkQR";
    public static final String URL_GET_EVENTMODULE = ROOT_URL + "getEventModule&id=";

}
