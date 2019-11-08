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
    ToggleButton locationToggle;
    String dateString;
    String timeString;
    Resources res;
    LatLng userLocation = null;
    Integer emote;
    Spinner add_situation;

    /**
     * This is run when the activity is created. It sets up listeners and initial values
     * @param savedInstanceState this is the values saved in previous instances of the class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        // Get the List of Moods from moodHistory
        final Intent intent = getIntent();
        final ArrayList<Mood> moodList = (ArrayList<Mood>) intent.getSerializableExtra("moodList");
        // Get a reference to current time/date
        cal = Calendar.getInstance();
        // Get a reference to the resources
        res = getResources();
        // Find text boxes and fill them
        timeText = findViewById(R.id.timeView);
        dateText = findViewById(R.id.dateView);
        final EditText ReasonText = findViewById(R.id.addReasonText);
        timeString = String.format(res.getString(R.string.TimeString),cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE));
        timeText.setText(timeString);
        dateString = String.format(res.getString(R.string.DateString),cal.get(Calendar.YEAR),(cal.get(Calendar.MONTH)+1),cal.get(Calendar.DAY_OF_MONTH));
        dateText.setText(dateString);
        // Set initial value to ensure the user picks an emote
        emote = -1;
        // Set up the spinner for social situation
        add_situation = findViewById(R.id.spinner1);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Add.this,
                android.R.layout.simple_expandable_list_item_1,getResources().getStringArray(R.array.SocialSituations));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_situation.setAdapter(myAdapter);

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
        // When the user turns on choosing location send them to AddMapActivity to select a location
        // When the user turns off choosing location remove their location
        locationToggle = findViewById(R.id.locationToggle);
        locationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The location is enabled
                    Intent mapIntent = new Intent(Add.this,AddMapActivity.class);
                    startActivityForResult(mapIntent,1);
                } else {
                    // The location is disabled
                    userLocation = null;
                }
            }
        });

        final Button confirm = findViewById(R.id.ConfirmAdd);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If an emote button has not been clicked do not do anything
                if (emote!=-1) {
                    //Get the date from the Calendar object that can be set from the Pickers
                    Date date = cal.getTime();
                    Mood mood;
                    //Get the reason and social situation of the mood
                    String reason = ReasonText.getText().toString();
                    String social = add_situation.getSelectedItem().toString();
                    // Get the name of the emoticon for future safe storage in the firebase
                    String emoticon = res.getResourceEntryName(emote);
                    // If the user added a location include it in the Mood
                    if (locationToggle.isChecked()) {
                        mood = new Mood(date, userLocation.latitude, userLocation.longitude, reason, social, emoticon);
                    } else {
                        mood = new Mood(date, reason, social, emoticon);
                    }
                    //Add the mood to the list and send the list back to moodHistory to update the firebase
                    moodList.add(mood);
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
                Log.d("myTag","getlat: "+userLocation.latitude);
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
