package com.example.queingsystem.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.queingsystem.Activity.Register;
import com.example.queingsystem.Activity.User_Handler;
import com.example.queingsystem.Model_User;
import com.example.queingsystem.R;
import com.example.queingsystem.RequestHandler;
import com.example.queingsystem.SharedPrefManager;
import com.example.queingsystem.URLs;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


public class Driver_Home extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    CardView cardLocation;
    TextView txtLocation;

    private static final String TAG = "MainActivity";
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;

    private LocationRequest mLocationRequest = new LocationRequest();
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 0;  /* 10 secs */
    private long FASTEST_INTERVAL = 1000; /* 2 sec */

    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.driver_home, container, false);
        cardLocation = v.findViewById(R.id.sendLocation);
        txtLocation = v.findViewById(R.id.txtLocation);
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
//                .setSmallestDisplacement(10)
                .setFastestInterval(FASTEST_INTERVAL);

        mLocationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        checkLocation();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
//        buildLocationCallBack();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        cardLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(getContext(), "Loading...",
                        "Sending Location..",true,true);

                Log.i(TAG, "card location");
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,locationCallback, Looper.myLooper());
//                startLocationUpdates();
//                checkLocation();
//                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//                LocationListener locationListener = new Driver_Home.MyLocationListener();
//
//                if ((ActivityCompat.checkSelfPermission(getContext(),
//                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
//                        ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
//                    Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();   // Toast is showing
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
//
//                }
//                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    // Permission is not granted
//                }
//                final double latitude = location.getLatitude();
//                final double longitude = location.getLongitude();
//                txtLocation.setText("Longitude"+longitude+ "Latitude "+ latitude);
            }



        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            return;
            }
        }
        Log.i(TAG, "Connected");
        buildLocationCallBack();

//        startLocationUpdates();
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        Log.d(TAG, "LastLocation: " + (mLocation == null ? "NO LastLocation" : mLocation.toString()));
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//        mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

//        if(mLocation == null){
//            Log.i(TAG, "Null location");
//            startLocationUpdates();
//        }
//        if (mLocation != null) {
////            startLocationUpdates();
////            txtLocation.setText(String.valueOf(mLocation.getLatitude()));
//            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
//            Toast.makeText(getContext(), "Location Detected " + String.valueOf(mLocation), Toast.LENGTH_SHORT).show();
//            Log.i(TAG, "" + String.valueOf(mLocation));
//        }else{
//            startLocationUpdates();
//        }
// else {
//            Toast.makeText(getContext(), "Location not Detected", Toast.LENGTH_SHORT).show();
//        }
    }

    private void buildLocationCallBack() {
        Log.i(TAG, "buildLocationCallBack");
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for(Location location:locationResult.getLocations()){
                    txtLocation.setText(String.valueOf(location.getLatitude()));
                    Log.i(TAG, "Location CallBack == " + String.valueOf(location.getLatitude()) + "/" + String.valueOf(location.getLongitude()));
                    addLocation(location.getLatitude(), location.getLongitude());
                }
            }
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                Log.i(TAG, " " + locationAvailability);
            }
        };
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
        Toast.makeText(getContext(), "Connection Failed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "Location Change" );

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
//        try {
//            String cityName = currentLocation(location.getLatitude(), location.getLongitude());
//            txtLocation.setText(cityName);
//        }catch (Exception e){
//            e.printStackTrace();
//            Log.i(TAG, "Location Change ERROR" );
//
//        }

        txtLocation.setText(String.valueOf(location.getLongitude() ));
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "Start" );

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "Stop" );
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    protected void startLocationUpdates() {
        // Create the location request
        Log.i(TAG, "startLocationUpdates");


        // Request location updates
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{
//                                Manifest.permission.ACCESS_COARSE_LOCATION,
//                                Manifest.permission.ACCESS_FINE_LOCATION},
//                        1);
                return;
            }
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

//        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d(TAG, "LastLocation: " + (mLocation == null ? "NO LastLocation" : mLocation.toString()));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
//                    startLocationUpdates();
//                    Log.d(TAG, "LastLocation: " + (mLocation == null ? "NO LastLocation" : mLocation.toString()));
//                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

//                    try{
//                        LocationServices.FusedLocationApi.requestLocationUpdates(
//                                mGoogleApiClient, mLocationRequest, this);
//                    } catch (SecurityException e) {
//                        Log.d(TAG, "LastLocation: " + e.toString());
//
////                        Toast.makeText(getContext(), "SecurityException:\n" + e.toString(),
////                                Toast.LENGTH_LONG).show();
//                    }
//                    LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
//                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                    try{
//                        String city = currentLocation(location.getLatitude(), location.getLongitude());
//                        txtLocation.setText(city);
//                    }catch (Exception e){
//                        Toast.makeText(getContext(), "Not Found2", Toast.LENGTH_SHORT).show();
//                    }
                }else{
                    Toast.makeText(getContext(), "Permission Not Granted", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
//
    private String currentLocation(double lat, double lon){
    String cityName = "";

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> address;
        try {
            address = geocoder.getFromLocation(lat, lon, 10);
            if(address.size() > 0){
                for(Address adr : address){
                    if(adr.getLocality() != null && adr.getLocality().length() > 0){
                        cityName = adr.getLocality();
                        break;
                    }
                }
            }
            Log.d(TAG, "Location Found: " );

        } catch (IOException e) {
            Toast.makeText(getContext(), "Location Not Found 2", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Location Not Found 2: " + e.toString());

            e.printStackTrace();
        }
        return cityName;
    }

    private void addLocation(double lat, double lon) {
        final String latitude = String.valueOf(lat);
        final String longitude = String.valueOf(lon);

        //if it passes all the validations
        class AddLocation extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_ADD_LOCATION, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
//                progressDialog.dismiss();
                Toast.makeText(getContext(), "Location Sent!!", Toast.LENGTH_SHORT).show();
            }
        }
        AddLocation ru = new AddLocation();
        ru.execute();
    }

}
