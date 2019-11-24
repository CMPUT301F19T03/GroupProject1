package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
 * This class is responsible for holding the map for Edit
 * It draws the map and puts a marker on it where the user specifies
 * Portions of this page are modifications based on work created and shared by Google and used according to terms described in the Creative Commons 4.0 Attribution License.
 * https://developers.google.com/maps/documentation/android-sdk/start
 */
public class EditMapActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private LatLng latLng;
    private Marker marker;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    /**
     * This is called when the activity is created. It sets up initial values and then starts the map fragment
     * @param savedInstanceState this is values saved by previous instances of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_map);
        // Get the location for the mood that is to be edited
        Intent intent = getIntent();
        latLng = new LatLng(intent.getDoubleExtra("Lat",0),intent.getDoubleExtra("long",0));
        // Start the map fragment and go to onMapReady when it is done
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Editmap);
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
        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            // Put the map over the user's position
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            // Put a marker on the position the user specified
            marker = mMap.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
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
    }
    /**
     * If we have already been given permissions set the bool to true
     * Otherwise request permissions from the user
     */
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
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
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
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
