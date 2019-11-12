package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * This class is responsible for holding the map for View
 * It draws the map and puts a marker on it according to the mood the user chose
 * Portions of this page are modifications based on work created and shared by Google and used according to terms described in the Creative Commons 4.0 Attribution License.
 * https://developers.google.com/maps/documentation/android-sdk/start
 */
public class ViewMapActivity extends AppCompatActivity implements OnMapReadyCallback{

    private LatLng latLng;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);
        Intent intent = getIntent();
        latLng = new LatLng(intent.getDoubleExtra("Lat",0),intent.getDoubleExtra("Long",0));

        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Viewmap);
        mapFrag.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        getLocationPermission();
        if (mLocationPermissionGranted) {
            googleMap.setMyLocationEnabled(true);
            googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(latLng , 16.0f) );

            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            googleMap.getUiSettings().setMapToolbarEnabled(false);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    public void BackButton(View view) {
        finish();
    }
}
