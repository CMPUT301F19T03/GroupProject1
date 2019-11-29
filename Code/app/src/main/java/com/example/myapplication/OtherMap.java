package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This class is responsible for the Othermap activity
 */

public class OtherMap extends AppCompatActivity implements OnMapReadyCallback {
    Participant user;

    CollectionReference users;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_map);
        user = (Participant)getIntent().getSerializableExtra("user");
        users = FirebaseFirestore.getInstance().collection("Users");

        // Start the map fragment and go to onMapReady when it is done
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.OtherMap);
        mapFrag.getMapAsync(this);
    }

    /**
     * This is called when the mapFrag is finished constructing from onCreate
     * It sets the onClickListeners and the mapUI settings
     * @param googleMap this is the map that was created
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        getLocationPermission();
        // If we have been given permissions to access Location proceed
        if (mLocationPermissionGranted) {
            // Otherwise go to the University Campus
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(53.5232, -113.5263), 14.0f));
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            //place markers on map

            ArrayList<String> all_follows = user.getFollowing();
            for (int i = 0; i < all_follows.size(); i++) {
                String name1 =  all_follows.get(i);
                users.whereEqualTo("Username", name1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                                    Participant user1 = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                                    Mood mood1 = user1.getMoodHistory().get(0);
                                    if (mood1.getLatitude()!=null) {
                                        LatLng point = new LatLng(mood1.getLatitude(),mood1.getLongitude());
                                        int emoteIcon = getResources().getIdentifier(mood1.getEmoticon(),"drawable",getPackageName());
                                        googleMap.addMarker(new MarkerOptions().position(point).title(user1.getName())
                                        .icon(BitmapDescriptorFactory.fromBitmap(resizeIcon(emoteIcon))));
                                    }
                                }
                            }
                        });
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
        setResult(RESULT_OK);
        finish();
    }

    /**
     * This button brings the user to their own map where they can see their mood history locations
     * @param view is the view context for this class
     */
    public void UserButton(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
