package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * This class is responsible for holding the map for Add
 * It draws the map and puts a marker on it where the user specifies
 * Portions of this page are modifications based on work created and shared by Google and used according to terms described in the Creative Commons 4.0 Attribution License.
 * https://developers.google.com/maps/documentation/android-sdk/start
 */
public class AddMapActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private LatLng latLng;
    private Marker marker;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private FusedLocationProviderClient fusedLocationClient;

    /**
     * This is called when the activity is created. It sets up initial values and then starts the map fragment
     * @param savedInstanceState this is values saved by previous instances of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_map);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Start the map fragment and go to onMapReady when it is done
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }

    /**
     * This is called when the mapFrag is finished constructing from onCreate
     * It sets the onClickListeners and the mapUI settings
     * @param googleMap this is the map that was created
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationPermission();
        // If we have been given permissions to access Location proceed
        if (mLocationPermissionGranted) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            mMap.setMyLocationEnabled(true);
                            if (location!=null) {
                                // If we can find a location for the user go to it
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),14.0f));
                            } else {
                                // Otherwise go to the University Campus
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(53.5232, -113.5263), 14.0f));
                            }
                            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            mMap.getUiSettings().setMapToolbarEnabled(false);
                            mMap.getUiSettings().setZoomControlsEnabled(true);
                            // When the user clicks on the map save that location and put a marker there
                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng point) {
                                    //save current location
                                    latLng = point;

                                    //remove previously placed Marker
                                    if (marker != null) {
                                        marker.remove();
                                    }

                                    //place marker where user just clicked
                                    marker = mMap.addMarker(new MarkerOptions().position(point)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                                }
                            });
                        }
                    });

        }
    }

    /**
     * If we have already been given permissions set the bool to true
     * Otherwise request permissions from the user
     */
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * when the user decides if the app can access their current location
     * either set the bool to true or exit the activity
     * @param requestCode the request code sent in getLocationPermission
     * @param permissions which permissions were requested
     * @param grantResults array for which permissions were granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                } else {
                    Toast.makeText(this,"App must have permissions for your location if you want to use the map",Toast.LENGTH_LONG).show();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        }
    }

    /**
     * If the user clicks submit and they have a currently selected location, return them to the
     * calling activity with the location
     * @param view this is the view that was clicked
     */
    public void SubmitLocation(View view) {
        if (latLng!=null) {
            Intent data = new Intent();
            data.putExtra("location",latLng);
            setResult(RESULT_OK,data);
            finish();
        }
    }

    /**
     * If the user clicks on cancel end the activity and tell the calling activity they cancelled
     * @param view this is the view that was clicked
     */
    public void CancelLocation(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
