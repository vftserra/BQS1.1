package com.example.queingsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.queingsystem.Activity.Login;

public class SharedPrefManager {
    //the constants
    private static final String SHARED_PREF_NAME = "Bodega";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_FIRSTNAME = "keyfirstname";
    private static final String KEY_IMAGE = "keyimage";
    private static final String KEY_ROLE = "keyrole";
    private static final String KEY_ID = "keyid";
    private static final String KEY_EVENT_ID = "keyeventid";
    private static final String KEY_PASSWORD = "keypassword";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(Model_User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_FIRSTNAME, user.getFirstname());
        editor.putString(KEY_IMAGE, user.getImage());
        editor.putString(KEY_ROLE, user.getRole());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.commit();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME, null);
        if(username != null){
            return true;
        }else{
            return false;
        }
    }

    //this method will give the logged in user
    public Model_User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Model_User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_FIRSTNAME, null),
                sharedPreferences.getString(KEY_IMAGE, null),
                sharedPreferences.getString(KEY_ROLE, null),
                sharedPreferences.getString(KEY_PASSWORD, null)

        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, Login.class));
    }


}
