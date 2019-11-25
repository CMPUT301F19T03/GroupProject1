package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This class is responsible for the Add activity
 * which creates a new Mood object based on the user's choices
 *
 *  Issues:
 *  Does not have any images
 */

public class Add extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    Calendar cal;
    TextView timeText;
    TextView dateText;
    EditText ReasonText;
    ToggleButton locationToggle;
    ToggleButton photoToggle;
    String dateString;
    String timeString;
    Resources res;
    LatLng userLocation = null;
    Spinner add_situation;
    Activity context;

    ViewPager viewPager;

    ImageView temp;
    FirebaseStorage mStorage;
    Participant user;

    String reason;
    String social;
    Date datetime;
    String emoticon;

    private static final int PERMISSIONS_REQUEST_ACCESS_CAMERA = 2;
    private boolean mCameraPermissionGranted;

    /**
     * This is run when the activity is created. It sets up listeners and initial values
     * @param savedInstanceState this is the values saved in previous instances of the class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        context = this;
        mStorage = FirebaseStorage.getInstance();
        // Get the List of Moods from moodHistory
        final Intent intent = getIntent();
        user = (Participant) intent.getSerializableExtra("user");

        final ArrayList<Mood> moodList = user.getMoodHistory();
        // Get a reference to current time/date
        cal = Calendar.getInstance();
        // Get a reference to the resources
        res = getResources();
        // Find text boxes and fill them
        timeText = findViewById(R.id.timeView);
        dateText = findViewById(R.id.dateView);
        ReasonText = findViewById(R.id.addReasonText);
        timeString = String.format(res.getString(R.string.TimeString),cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE));
        timeText.setText(timeString);
        dateString = String.format(res.getString(R.string.DateString),cal.get(Calendar.YEAR),(cal.get(Calendar.MONTH)+1),cal.get(Calendar.DAY_OF_MONTH));
        dateText.setText(dateString);
        // Set up the spinner for social situation
        add_situation = findViewById(R.id.spinner1);
        final ArrayAdapter<String> myAdapter = new ArrayAdapter<>(Add.this,
                android.R.layout.simple_expandable_list_item_1,getResources().getStringArray(R.array.SocialSituations));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_situation.setAdapter(myAdapter);

        viewPager = findViewById(R.id.AddviewPager);
        ImagePagerAdapter adapter = new ImagePagerAdapter(context);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2);

        temp = findViewById(R.id.tempImageView);

        // Set up a button to start the time Picker
        Button timePick = findViewById(R.id.timeButton);
        timePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time Picker");
            }
        });
        // Set up a button to start the date picker
        final Button datePick = findViewById(R.id.dateButton);
        datePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
        final Button locationButton = findViewById(R.id.EditLocation);
        // If they click on 'Edit location' send them to EditMapActivity
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditMapIntent = new Intent(Add.this, EditMapActivity.class);
                EditMapIntent.putExtra("Lat", userLocation.latitude);
                EditMapIntent.putExtra("long", userLocation.longitude);
                startActivityForResult(EditMapIntent, 2);
            }
        });
        // When the user turns on choosing location send them to AddMapActivity to select a location
        // When the user turns off choosing location remove their location
        locationToggle = findViewById(R.id.locationToggle);
        locationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    locationButton.setVisibility(View.VISIBLE);
                    // The location is enabled
                    Intent mapIntent = new Intent(Add.this,AddMapActivity.class);
                    startActivityForResult(mapIntent,1);
                } else {
                    // The location is disabled
                    userLocation = null;
                    locationButton.setVisibility(View.GONE);
                }
            }
        });

        photoToggle = findViewById(R.id.photoToggle);
        photoToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
//                    Intent photoIntent = new Intent();
//                    photoIntent.setType("image/*");
//                    photoIntent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(photoIntent,"Select Picture"),3);
                    getCameraPermission();
                    if (mCameraPermissionGranted) {
                        startCameraIntent();
                    }
                } else {
                    temp.setImageBitmap(null);
                }
            }
        });

        final Button confirm = findViewById(R.id.ConfirmAdd);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the date from the Calendar object that can be set from the Pickers
                datetime = cal.getTime();
                //Get the reason and social situation of the mood
                reason = ReasonText.getText().toString();
                if (reason.split(" ").length>3) {
                    Toast.makeText(context,"Reason can only be 3 words",Toast.LENGTH_SHORT).show();
                    return;
                }
                social = add_situation.getSelectedItem().toString();
                // Get the name of the emoticon for future safe storage in the firebase
                int pos = viewPager.getCurrentItem();
                emoticon = res.getStringArray(R.array.emotes)[pos];
                if (photoToggle.isChecked()) {
                    encodeBitmapAndSave();
                } else {
                    createMood(null, datetime, reason, social, emoticon);
                }

            }
        });

        Toast.makeText(this,"Swipe emote to select other moods",Toast.LENGTH_SHORT).show();
    }

    public void createMood(UploadTask task,Date date,String reason,String social,String emoticon) {
        Mood mood;
        ArrayList<Mood> moodList = user.getMoodHistory();
        String image;
        if (task==null) {
            image = null;
        } else {
            image = user.getUID()+"/"+cal.getTime();
        }

        // If the user added a location include it in the Mood
        if (locationToggle.isChecked()) {
            mood = new Mood(date, userLocation.latitude, userLocation.longitude, reason, social, emoticon, image);
        } else {
            mood = new Mood(date, reason, social, emoticon, image);
        }
        //Add the mood to the list and send the list back to moodHistory to update the firebase
        moodList.add(mood);
        Intent data = new Intent();
        data.putExtra("Addmood", moodList);
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * This button returns the user to the Moodhistory view
     * @param view is the context for this view
     */
    public void ReturnButton(View view) {
        finish();
    }

    /**
     * This is an interface that detects when the user enters a time in the timepicker
     * This interface then sets that time into an object for the end Mood product
     * and sets a TextView to display their chosen time on screen
     * @param timePicker this is the TimePicker fragment the user is interacting with
     * @param hour this is the hour the user chose
     * @param minute this is the minute the user chose
     */
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        cal.set(Calendar.MINUTE,minute);
        cal.set(Calendar.HOUR_OF_DAY,hour);
        timeString = String.format(res.getString(R.string.TimeString),cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE));
        timeText.setText(timeString);

    }
    /**
     * This is an interface that detects when the user enters a date in the datepicker
     * This interface then sets that date into an object for the end Mood product
     * and sets a TextView to display their chosen date on screen
     * @param datePicker this is the DatePicker fragment the user is interacting with
     * @param year this is the year the user chose
     * @param month this is the month the user chose
     * @param day this is the day the user chose
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.DAY_OF_MONTH,day);
        dateString = String.format(res.getString(R.string.DateString),cal.get(Calendar.YEAR),(cal.get(Calendar.MONTH)+1),cal.get(Calendar.DAY_OF_MONTH));
        dateText.setText(dateString);
    }

    /**
     * This detects the return from the Location selecting activity (addMapActivity)
     * It unchecks the include location box if the user cancels
     * or puts the user's chosen location into an object for the end Mood object
     * @param requestCode this is the request code for the activity when it was created
     * @param resultCode this is the return value from the activity to tell if the user cancelled
     * @param data holds the LatLng object being returned from the addMapActivity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1) {
            if (resultCode==RESULT_CANCELED) {
                locationToggle.setChecked(false);
            } else if (resultCode==RESULT_OK) {
                userLocation = data.getExtras().getParcelable("location");
            }
        } else if (requestCode==2) {
            if (resultCode==RESULT_OK) {
                userLocation = data.getExtras().getParcelable("location");
            }
        } else if (requestCode==3) {
            if (resultCode==RESULT_OK) {
                Uri imageURI;
                if (data.getData()!=null) {
                    imageURI = data.getData();
                } else {
                    Log.d("myTag","Path: "+currentPhotoPath);
                    imageURI = Uri.parse("file://"+currentPhotoPath);
                }
                temp.setImageURI(imageURI);

            } else {
                photoToggle.setChecked(false);
            }
        }
    }

    public void encodeBitmapAndSave() {
        Bitmap bitmap = ((BitmapDrawable) temp.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] data = baos.toByteArray();
        StorageReference ref = mStorage.getReference().child(user.getUID()+"/"+cal.getTime());
        final UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("myTag","Upload Failed: "+e);
            }
        });
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("myTag", "Upload Succeeded");
                createMood(uploadTask,datetime,reason,social,emoticon);
            }
        });
    }

    private void getCameraPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            mCameraPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_ACCESS_CAMERA);
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
        mCameraPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mCameraPermissionGranted = true;
                    startCameraIntent();
                } else {
                    Toast.makeText(this,"App must have permissions for your camera if you want to use the camera",Toast.LENGTH_LONG).show();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        }
    }
    String currentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void startCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager())!=null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Log.e("myTag","Exception found while creating file: "+e);
            }
            if (photoFile!=null) {
                Uri photoURI = FileProvider.getUriForFile(context,"com.example.android.fileprovider",
                        photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, 3);
            }
        } else {
            Log.d("myTag","Could not resolve camera activity");
        }
    }

}

