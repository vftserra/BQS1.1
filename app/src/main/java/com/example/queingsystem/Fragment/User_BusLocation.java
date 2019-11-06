package com.example.queingsystem.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.queingsystem.R;
import com.example.queingsystem.URLs;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class User_BusLocation extends Fragment {
    Button btnLocate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.user_bus_location, container, false);
        btnLocate = v.findViewById(R.id.btnLocate);
        getLocation();
        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
        return v;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private void getLocation() {
        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment

        class GetLocation extends AsyncTask<String,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getContext(),"Loading...","Locating please wait...",true,true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                try {
                    //converting response to json object
                    //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    JSONObject obj = new JSONObject(s);
                    JSONArray resultArray = obj.getJSONArray("result");
                    for(int i = 0; i < resultArray.length(); i++ ){
                        JSONObject o = resultArray.getJSONObject(i);
                        final String latitude = o.getString("latitude");
                        final String longitude = o.getString("longitude");


                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap mMap) {
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                            mMap.clear(); //clear old markers

//                            mMap.moveCamera(CameraUpdateFactory.newLatLng(cur_Latlng));
//                            mMap.animateCamera(CameraUpdateFactory.zoomTo(4));

                            CameraPosition googlePlex = CameraPosition.builder()
                                    .target(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)))
                                    .zoom(10)
                                    .bearing(0)
                                    .tilt(45)
                                    .build();

                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)))
                                    .title("Your Location")
                                    .icon(bitmapDescriptorFromVector(getContext(),R.drawable.spider)));

//                            mMap.addMarker(new MarkerOptions()
//                                    .position(new LatLng(37.4629101,-122.2449094))
//                                    .title("Iron Man")
//                                    .snippet("His Talent : Plenty of money"));
//
//                            mMap.addMarker(new MarkerOptions()
//                                    .position(new LatLng(37.3092293,-122.1136845))
//                                    .title("Captain America"));
                        }
                    });

                    }
                    loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }
                    return sb.toString().trim();
                }catch(Exception e){
                    return null;
                }
            }
        }

        GetLocation gu = new GetLocation();
        gu.execute(URLs.URL_GET_LOCATION);
    }

}
