package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

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
    EditText ReasonText;
    ToggleButton locationToggle;
    private ImageView moodEmote;
    String dateString;
    String timeString;
    Resources res;
    LatLng userLocation = null;
    Integer emote;
    Spinner add_situation;
    Activity context;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    View.OnTouchListener gestureListener;
    private int index;
    private String[] subjectArray;

    private GestureDetector mDetector;

    /**
     * This is run when the activity is created. It sets up listeners and initial values
     * @param savedInstanceState this is the values saved in previous instances of the class
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        context = this;
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
                //Get the date from the Calendar object that can be set from the Pickers
                Date date = cal.getTime();
                Mood mood;
                //Get the reason and social situation of the mood
                String reason = ReasonText.getText().toString();
                if (reason.split(" ").length>3) {
                    Toast.makeText(context,"Reason can only be 3 words",Toast.LENGTH_SHORT).show();
                    return;
                }
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
        });

        index = 2;
        subjectArray = getResources().getStringArray(R.array.emotes);
        moodEmote = findViewById(R.id.addEmote);
        int id = getResources().getIdentifier(subjectArray[index],"drawable",getPackageName());
        emote = id;
        moodEmote.setImageResource(id);
        gestureListener = new View.OnTouchListener() {
            float prevX;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        prevX = motionEvent.getX();
                        Log.d("myTag","prevX: "+prevX);
                        return true;
                    case MotionEvent.ACTION_UP:
                        float newX = motionEvent.getX();
                        Log.d("myTag","newX: "+newX);

                        if (Math.abs(newX - prevX) > SWIPE_MIN_DISTANCE) {
                            if (newX > prevX) {
                                Log.d("myTag", "swipe right");
                                index = index - 1;
                                if (index<0) {
                                    index=0;
                                }

                                int id = getResources().getIdentifier(subjectArray[index], "drawable", getPackageName());
                                emote = id;
                                moodEmote.setImageResource(id);
                                return true;
                            } else {
                                Log.d("myTag", "swipe left");
                                index = index + 1;
                                if (index>=subjectArray.length) {
                                    index = subjectArray.length-1;
                                }

                                int id = getResources().getIdentifier(subjectArray[index],"drawable",getPackageName());
                                emote = id;
                                moodEmote.setImageResource(id);
                                return true;
                            }
                        }
                    default:
                        return false;
                }
            }
        };

        mDetector = new GestureDetector(moodEmote.getContext(),new MGestureDetector());
        gestureListener = new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mDetector.onTouchEvent(event);
            }
        };
        moodEmote.setOnTouchListener(gestureListener);

        Toast.makeText(this,"Swipe emote to select other moods",Toast.LENGTH_LONG).show();
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
        }
    }

    class MGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("myTag","down");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("myTag","Fling");
            try{
                if(Math.abs(e1.getY()-e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
                    Log.d("myTag", "Move Next");
                    index = index + 1;
                    if (index>=subjectArray.length) {
                        index = subjectArray.length-1;
                    }

                    int id = getResources().getIdentifier(subjectArray[index],"drawable",getPackageName());
                    emote = id;
                    moodEmote.setImageResource(id);
                    return true;
                } else if(e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
                    Log.d("myTag", "Move Previous");
                    index = index - 1;
                    if (index<0) {
                        index=0;
                    }

                    int id = getResources().getIdentifier(subjectArray[index], "drawable", getPackageName());
                    emote = id;
                    moodEmote.setImageResource(id);
                    return true;
                }
                return false;
            }
            catch(Exception e){
                return false;
            }
        }

    }
}

