package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This class is responsible for the Edit activity
 */
public class Edit extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    Mood editMood;
    ArrayList<Mood> moodList;
    Calendar cal;
    TextView timeText;
    TextView dateText;
    EditText ReasonText;
    ToggleButton locationToggle;
    ToggleButton photoToggle;
    String dateString;
    String timeString;
    Resources res;
    LatLng userLocation;
    String reason;
    String social;
    Spinner edit_situation;
    Activity context;

    ViewPager viewpager;

    ImageView temp;
    FirebaseStorage mStorage;
    Participant user;
    String imagePath;
    boolean imageChanged;


    /**
     * This is run when the Acitivity is created it sets initial values and finds layout objects
     *
     * @param savedInstanceState this is the values saved in previous instances of the class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        imageChanged = false;
        context = this;
        mStorage = FirebaseStorage.getInstance();
        res = getResources();
        // Get the mood array from moodHistory
        final Intent intent = getIntent();
        user = (Participant) intent.getSerializableExtra("user");
        moodList = user.getMoodHistory();
        //Get the position of the item to be changed
        final int pos = intent.getIntExtra("pos", 0);
        // Get the mood that is being edited
        editMood = moodList.get(pos);

        // Initialize with values from editMood
        cal = Calendar.getInstance();
        cal.setTime(editMood.getDatetime());
        reason = editMood.getReason();
        social = editMood.getSocialSituation();

        // Set up the spinner for th Social Situation
        edit_situation = findViewById(R.id.spinner2);
        ArrayAdapter<String> myadapter = new ArrayAdapter<String>(Edit.this,
                android.R.layout.simple_expandable_list_item_1, getResources().getStringArray(R.array.SocialSituations));
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_situation.setAdapter(myadapter);

        viewpager = findViewById(R.id.EditviewPager);
        ImagePagerAdapter adapter = new ImagePagerAdapter(context);
        viewpager.setAdapter(adapter);
        int index = Arrays.asList(res.getStringArray(R.array.emotes)).indexOf(editMood.getEmoticon());
        viewpager.setCurrentItem(index);

        // Find items related to location
        locationToggle = findViewById(R.id.editLocationToggle);
        final Button locationButton = findViewById(R.id.EditLocation);
        //Initialize visibility of 'Edit location' button
        if (editMood.getLatitude() != null) {
            userLocation = new LatLng(editMood.getLatitude(), editMood.getLongitude());
            locationToggle.setChecked(true);
        } else {
            locationToggle.setChecked(false);
            locationButton.setVisibility(View.GONE);
        }
        // Set with value from editMood
        ReasonText = findViewById(R.id.addReasonText);
        ReasonText.setText(reason);
        // Set the spinner to the existing value
        if (!social.isEmpty()) {
            int spinnerpos = myadapter.getPosition(social);
            edit_situation.setSelection(spinnerpos);
        }
        // Set up date and time pickers and Strings for displaying
        timeText = findViewById(R.id.timeView);
        dateText = findViewById(R.id.dateView);
        res = getResources();
        timeString = String.format(res.getString(R.string.TimeString), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        timeText.setText(timeString);
        dateString = String.format(res.getString(R.string.DateString), cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH) + 1), cal.get(Calendar.DAY_OF_MONTH));
        dateText.setText(dateString);

        // Set listener for starting time Picker
        Button timePick = findViewById(R.id.timeButton);
        timePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment(cal);
                timePicker.show(getSupportFragmentManager(), "time Picker");
            }
        });
        // Set listener for starting date Picker
        final Button datePick = findViewById(R.id.dateButton);
        datePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment(cal);
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        // If they click on 'Edit location' send them to EditMapActivity
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditMapIntent = new Intent(Edit.this, EditMapActivity.class);
                EditMapIntent.putExtra("Lat", userLocation.latitude);
                EditMapIntent.putExtra("long", userLocation.longitude);
                startActivityForResult(EditMapIntent, 2);
            }
        });
        // If they change from no location to yes send them to AddMapActivity if they do not already have a location
        // If they change to no location hide the 'Edit location' button
        locationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The location is enabled
                    locationButton.setVisibility(View.VISIBLE);
                    if (userLocation == null) {
                        Intent mapIntent = new Intent(Edit.this, AddMapActivity.class);
                        startActivityForResult(mapIntent, 1);
                    }
                } else {
                    // The location is disabled
                    locationButton.setVisibility(View.GONE);
                }
            }
        });

        temp = findViewById(R.id.tempImageView);
        if (editMood.getPicture()!=null) {
            StorageReference imRef = mStorage.getReference().child(editMood.getPicture());
            imRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    temp.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("myTag","Failed to getBytes: "+e);
                }
            });
        }
        photoToggle = findViewById(R.id.photoToggle);
        photoToggle.setChecked(editMood.getPicture()!=null);
        photoToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                imageChanged = true;
                if (isChecked) {
                    Intent photoIntent = new Intent();
                    photoIntent.setType("image/*");
                    photoIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(photoIntent,"Select Picture"),3);
                } else {
                    temp.setImageBitmap(null);
                }
            }
        });

        // Set the behaviour for when the user tries to submit a Edited mood
        final Button confirm = findViewById(R.id.ConfirmEdit);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = cal.getTime();
                String reason = ReasonText.getText().toString();
                if (reason.split(" ").length > 3) {
                    Toast.makeText(context, "Reason can only be 3 words", Toast.LENGTH_SHORT).show();
                    return;
                }
                String social = edit_situation.getSelectedItem().toString();

                int pos = viewpager.getCurrentItem();
                String emote = res.getStringArray(R.array.emotes)[pos];
                if (locationToggle.isChecked()) {
                    //Include the location
                    editMood.setLatitude(userLocation.latitude);
                    editMood.setLongitude(userLocation.longitude);
                } else {
                    //Do not include location
                    editMood.setLongitude(null);
                    editMood.setLatitude(null);
                }
                editMood.setDatetime(date);
                editMood.setReason(reason);
                editMood.setSocialSituation(social);
                editMood.setEmoticon(emote);
                editImage();
            }
        });

        Toast.makeText(this,"Swipe emote to select other moods",Toast.LENGTH_SHORT).show();
    }

    /**
     * This button returns the user to the Moodhistory view
     *
     * @param view is the context for this view
     */
    public void ReturnButton(View view) {
        finish();
    }

    /**
     * This is an interface that detects when the user enters a time in the timepicker
     * This interface then sets that time into an object for the end Mood product
     * and sets a TextView to display their chosen time on screen
     *
     * @param timePicker this is the TimePicker fragment the user is interacting with
     * @param hour       this is the hour the user chose
     * @param minute     this is the minute the user chose
     */
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        timeString = String.format(res.getString(R.string.TimeString), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        timeText.setText(timeString);

    }

    /**
     * This is an interface that detects when the user enters a date in the datepicker
     * This interface then sets that date into an object for the end Mood product
     * and sets a TextView to display their chosen date on screen
     *
     * @param datePicker this is the DatePicker fragment the user is interacting with
     * @param year       this is the year the user chose
     * @param month      this is the month the user chose
     * @param day        this is the day the user chose
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        dateString = String.format(res.getString(R.string.DateString), cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH) + 1), cal.get(Calendar.DAY_OF_MONTH));
        dateText.setText(dateString);
    }

    /**
     * This detects the return from the Location selecting activity (editMapActivity)
     * or puts the user's chosen location into an object for the end Mood object
     *
     * @param requestCode this is the request code for the activity when it was created
     * @param resultCode  this is the return value from the activity to tell if the user cancelled
     * @param data        holds the LatLng object being returned from the editMapActivity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                userLocation = data.getExtras().getParcelable("location");
            } else if (resultCode == RESULT_CANCELED) {
                locationToggle.setChecked(false);
            }
        } else if (requestCode==2) {
            if (resultCode==RESULT_OK) {
                userLocation = data.getExtras().getParcelable("location");
            }
        } else if (requestCode==3) {
            if (resultCode == RESULT_OK) {
                Uri imageURI = data.getData();
                temp.setImageURI(imageURI);
            } else {
                photoToggle.setChecked(false);
            }
        }
    }

    public void editImage() {
        if (photoToggle.isChecked() && imageChanged) {
            Log.d("myTag","case 1");
            encodeBitmapAndSave();
        } else if (!photoToggle.isChecked() && imageChanged){
            Log.d("myTag","case 2");
            if (editMood.getPicture()!=null) {
                Log.d("myTag","case 2.1");
                mStorage.getReference().child(editMood.getPicture()).delete()
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("myTag", "Failed to delete old image: " + e);
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("myTag", "Removed old image successfully");
                                editMood.setPicture(null);
                                Intent data = new Intent();
                                data.putExtra("Addmood", moodList);
                                setResult(RESULT_OK, data);
                                finish();
                            }
                        });
            } else {
                Log.d("myTag","case 2.2");
                editMood.setPicture(null);
                Intent data = new Intent();
                data.putExtra("Addmood", moodList);
                setResult(RESULT_OK, data);
                finish();
            }
        } else {
            Log.d("myTag", "case 3");
            Intent data = new Intent();
            data.putExtra("Addmood", moodList);
            setResult(RESULT_OK, data);
            finish();
        }

    }
    public void encodeBitmapAndSave() {
        Bitmap bitmap = ((BitmapDrawable) temp.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        final byte[] data = baos.toByteArray();
        imagePath = user.getUID()+"/"+Calendar.getInstance().getTime();
        if (editMood.getPicture()!=null) {
            mStorage.getReference().child(editMood.getPicture()).delete()
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("myTag", "Failed to delete old image: " + e);
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("myTag", "Removed old image successfully");

                        }
                    });
        }
        StorageReference ref = mStorage.getReference().child(imagePath);
        UploadTask uploadTask = ref.putBytes(data);
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
            editMood.setPicture(imagePath);
            Intent data = new Intent();
            data.putExtra("Addmood", moodList);
            setResult(RESULT_OK, data);
            finish();
            }
        });

        }

}
