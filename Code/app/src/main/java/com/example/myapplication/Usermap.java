package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * This class is responsible for the Usermap activity
 * Portions of this page are modifications based on work created and shared by Google and used according to terms described in the Creative Commons 4.0 Attribution License.
 * https://developers.google.com/maps/documentation/android-sdk/start
 */
public class Usermap extends AppCompatActivity implements OnMapReadyCallback {
    Participant user;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermap);
        user = (Participant)getIntent().getSerializableExtra("user");

        // Start the map fragment and go to onMapReady when it is done
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Usermap);
        mapFrag.getMapAsync(this);
    }

    /**
     * This is called when the mapFrag is finished constructing from onCreate
     * It sets the onClickListeners and the mapUI settings
     * @param googleMap this is the map that was created
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        getLocationPermission();
        // If we have been given permissions to access Location proceed
        if (mLocationPermissionGranted) {
            googleMap.setMyLocationEnabled(true);
            // Otherwise go to the University Campus
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(53.5232, -113.5263), 14.0f));
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            googleMap.getUiSettings().setMapToolbarEnabled(false);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            //place markers on map
            ArrayList<Mood> moods = user.getMoodHistory();
            for (Mood mood : moods) {
                if (mood.getLatitude()!=null) {
                    LatLng point = new LatLng(mood.getLatitude(), mood.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(point)
                            .icon(BitmapDescriptorFactory.fromBitmap(resizeIcon(mood.getEmoteIcon()))));
                }
            }
        }
    }

    /**
     * Resizes the icon from the resources folder into a size that can be drawn on the map
     * from https://stackoverflow.com/questions/14851641/change-marker-size-in-google-maps-api-v2
     * @param icon the icon to be resized
     * @return a Bitmap of the resized image
     */
    public Bitmap resizeIcon(int icon) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),icon);
        return Bitmap.createScaledBitmap(imageBitmap,100,100,false);
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
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            } else {
                Toast.makeText(this, "App must have permissions for your location if you want to use the map", Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }

    /**
     * This button returns the user to the Moodhistory view
     * @param view is the context for this view
     */
    public void ReturnButton(View view) {
        finish();
    }

    /**
     * This button sends the user to the other map view which displays their followers
     * @param view is the context for this view
     */
    public void OthersButton(View view) {
        Intent intent = new Intent(this, OtherMap.class);
        intent.putExtra("user",user);
        startActivityForResult(intent,1);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            finish();
        }
    }
}
