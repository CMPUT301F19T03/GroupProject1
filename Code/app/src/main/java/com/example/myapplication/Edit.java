package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This class is responsible for the Edit activity
 */
public class Edit extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    Calendar cal;
    TextView timeText;
    TextView dateText;
    ToggleButton locationToggle;
    String dateString;
    String timeString;
    Resources res;
    LatLng userLocation;
    Integer emote;
    String reason;
    String social;
    Spinner edit_situation;

    EditText ReasonText;

    /**
     * This is run when the Acitivity is created it sets initial values and finds layout objects
     * @param savedInstanceState this is the values saved in previous instances of the class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        // Get the mood array from moodHistory
        final Intent intent = getIntent();
        final ArrayList<Mood> moodList = (ArrayList<Mood>) intent.getSerializableExtra("moodList");
        //Get the position of the item to be changed
        final int pos = intent.getIntExtra("pos",0);
        // Get the mood that is being edited
        final Mood editMood = moodList.get(pos);

        // Initialize with values from editMood
        cal = Calendar.getInstance();
        cal.setTime(editMood.getDatetime());
        emote = editMood.getEmoteIcon();
        reason = editMood.getReason();
        social = editMood.getSocialSituation();

        // Set up the spinner for th Social Situation
        edit_situation = findViewById(R.id.spinner2);
        ArrayAdapter<String> myadapter = new ArrayAdapter<String>(Edit.this,
                android.R.layout.simple_expandable_list_item_1,getResources().getStringArray(R.array.SocialSituations));
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_situation.setAdapter(myadapter);

        // Find items related to location
        locationToggle = findViewById(R.id.editLocationToggle);
        final Button locationButton = findViewById(R.id.EditLocation);
        //Initialize visibility of 'Edit location' button
        if (editMood.getLatitude()!=null) {
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
        if (!social.isEmpty()){
            int spinnerpos = myadapter.getPosition(social);
            edit_situation.setSelection(spinnerpos);
        }
        // Set up date and time pickers and Strings for displaying
        timeText = findViewById(R.id.timeView);
        dateText = findViewById(R.id.dateView);
        res = getResources();
        timeString = String.format(res.getString(R.string.TimeString),cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE));
        timeText.setText(timeString);
        dateString = String.format(res.getString(R.string.DateString),cal.get(Calendar.YEAR),(cal.get(Calendar.MONTH)+1),cal.get(Calendar.DAY_OF_MONTH));
        dateText.setText(dateString);

        // Set listener for starting time Picker
        Button timePick = findViewById(R.id.timeButton);
        timePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment(cal);
                timePicker.show(getSupportFragmentManager(),"time Picker");
            }
        });
        // Set listener for starting date Picker
        final Button datePick = findViewById(R.id.dateButton);
        datePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment(cal);
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        // If they click on 'Edit location' send them to EditMapActivity
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditMapIntent = new Intent(Edit.this,EditMapActivity.class);
                EditMapIntent.putExtra("Lat",userLocation.latitude);
                EditMapIntent.putExtra("long",userLocation.longitude);
                startActivityForResult(EditMapIntent,1);
            }
        });
        // If they change from no location to yes send them to AddMapActivity if they do not already have a location
        // If they change to no location hide the 'Edit location' button
        locationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The location is enabled
                    locationButton.setVisibility(View.VISIBLE);
                    if (userLocation==null) {
                        Intent mapIntent = new Intent(Edit.this, AddMapActivity.class);
                        startActivityForResult(mapIntent, 1);
                    }
                } else {
                    // The location is disabled
                    locationButton.setVisibility(View.GONE);
                }
            }
        });

        // Set the behaviour for when the user tries to submit a Edited mood
        final Button confirm = findViewById(R.id.ConfirmEdit);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emote!=-1) {
                    Date date = cal.getTime();
                    String reason = ReasonText.getText().toString();
                    String social = edit_situation.getSelectedItem().toString();
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
                    editMood.setEmoteIcon(emote);
                    Intent data = new Intent();
                    data.putExtra("Addmood", moodList);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
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
     * This detects the return from the Location selecting activity (editMapActivity)
     * or puts the user's chosen location into an object for the end Mood object
     * @param requestCode this is the request code for the activity when it was created
     * @param resultCode this is the return value from the activity to tell if the user cancelled
     * @param data holds the LatLng object being returned from the editMapActivity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1) {
            if (resultCode==RESULT_OK) {
                userLocation = data.getExtras().getParcelable("location");
            } else if (resultCode==RESULT_CANCELED) {
                locationToggle.setChecked(false);
            }
        }
    }

    /**
     * If the user clicks on the great emote button set the emote to the great resource
     * @param view this is the view that was clicked
     */
    public void selectGreat(View view){
        emote = R.drawable.great;
    }
    /**
     * If the user clicks on the good emote button set the emote to the good resource
     * @param view this is the view that was clicked
     */
    public void selectGood(View view){
        emote = R.drawable.good;
    }
    /**
     * If the user clicks on the neutral emote button set the emote to the neutral resource
     * @param view this is the view that was clicked
     */
    public void selectNeutral(View view){
        emote = R.drawable.neutral;
    }
    /**
     * If the user clicks on the bad emote button set the emote to the bad resource
     * @param view this is the view that was clicked
     */
    public void selectBad(View view){
        emote = R.drawable.bad;
    }
    /**
     * If the user clicks on the worst emote button set the emote to the worst resource
     * @param view this is the view that was clicked
     */
    public void selectWorst(View view){
        emote = R.drawable.worst;
    }
}
