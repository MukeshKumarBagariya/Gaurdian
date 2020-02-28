package com.example.oldman;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private RequestQueue requestQueue;
    private Button emergency;
    public double lattitude;
    public double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        emergency = findViewById(R.id.emergencyButton);
        emergency.animate().translationYBy(1700);
        emergency.animate().translationXBy(850);
        requestQueue = Volley.newRequestQueue(this);
        String url = "https://warm-river-60977.herokuapp.com/getdata";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String server_data = response.toString();
                    JSONObject mapdata = new JSONObject(server_data);
                    lattitude = mapdata.getDouble("lat");
                    longitude = mapdata.getDouble("lon");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this,CallingActivity.class);
                startActivity(intent);
                Toast.makeText(MapsActivity.this,lattitude+"  -  "+longitude,Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng oldLocation = new LatLng(lattitude, longitude);
        mMap.addMarker(new MarkerOptions().position(oldLocation).title("You are here !!!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(oldLocation, 16));
        Toast.makeText(MapsActivity.this,lattitude+"  -  "+longitude,Toast.LENGTH_LONG).show();

    }


//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                updateCameraPassengerLocation(location);
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };
//
//        if (Build.VERSION.SDK_INT >= 23){
//
//            if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//
//                ActivityCompat.requestPermissions(MapsActivity.this,new String[]{
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                },1000);
//            } else {
//                try {
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
//                    Location currentPassengerLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                    updateCameraPassengerLocation(currentPassengerLoc);
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1000 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//            if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
//                Location currentPassengerLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                updateCameraPassengerLocation(currentPassengerLocation);
//            }
//
//        }
//    }
//    private void updateCameraPassengerLocation(Location location){
//        LatLng passengerLocation = new LatLng(location.getLatitude(), location.getLongitude());
//        mMap.clear();
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(passengerLocation, 18));
//        mMap.addMarker(new MarkerOptions().position(passengerLocation).title("You are here:"));
//    }
}
