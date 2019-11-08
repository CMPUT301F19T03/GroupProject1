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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
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

    EditText ReasonText;
    EditText SocialText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        final Intent intent = getIntent();
        final ArrayList<Mood> moodList = (ArrayList<Mood>) intent.getSerializableExtra("moodList");
        final int pos = intent.getIntExtra("pos",0);

        final Mood editMood = moodList.get(pos);
        cal = Calendar.getInstance();
        cal.setTime(editMood.getDatetime());
        emote = editMood.getEmoteIcon();
        reason = editMood.getReason();
        social = editMood.getSocialSituation();
        locationToggle = findViewById(R.id.editLocationToggle);
        final Button locationButton = findViewById(R.id.EditLocation);

        if (editMood.getLatitude()!=null) {
            userLocation = new LatLng(editMood.getLatitude(), editMood.getLongitude());
            locationToggle.setChecked(true);
        } else {
            locationToggle.setChecked(false);
            locationButton.setVisibility(View.GONE);

        }

        ReasonText = findViewById(R.id.addReasonText);
        ReasonText.setText(reason);
        SocialText = findViewById(R.id.addSocialText);
        SocialText.setText(social);

        timeText = findViewById(R.id.timeView);
        dateText = findViewById(R.id.dateView);
        res = getResources();
        timeString = String.format(res.getString(R.string.TimeString),cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE));
        timeText.setText(timeString);
        dateString = String.format(res.getString(R.string.DateString),cal.get(Calendar.YEAR),(cal.get(Calendar.MONTH)+1),cal.get(Calendar.DAY_OF_MONTH));
        dateText.setText(dateString);

        Button timePick = findViewById(R.id.timeButton);
        timePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment(cal);
                timePicker.show(getSupportFragmentManager(),"time Picker");
            }
        });

        final Button datePick = findViewById(R.id.dateButton);
        datePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment(cal);
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditMapIntent = new Intent(Edit.this,EditMapActivity.class);
                EditMapIntent.putExtra("Lat",userLocation.latitude);
                EditMapIntent.putExtra("long",userLocation.longitude);
                startActivityForResult(EditMapIntent,1);
            }
        });
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
//                    userLocation = null;//Very unsure
                }
            }
        });

        final Button confirm = findViewById(R.id.ConfirmEdit);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emote!=-1) {
                    Date date = cal.getTime();
                    String reason = ReasonText.getText().toString();
                    String social = SocialText.getText().toString();
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

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        cal.set(Calendar.MINUTE,minute);
        cal.set(Calendar.HOUR_OF_DAY,hour);
        timeString = String.format(res.getString(R.string.TimeString),cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE));
        timeText.setText(timeString);

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.DAY_OF_MONTH,day);
        dateString = String.format(res.getString(R.string.DateString),cal.get(Calendar.YEAR),(cal.get(Calendar.MONTH)+1),cal.get(Calendar.DAY_OF_MONTH));
        dateText.setText(dateString);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1) {
            if (resultCode==RESULT_OK) {
                userLocation = data.getExtras().getParcelable("location");
                Log.d("myTag","getlat: "+userLocation.latitude);
            }
        }
    }

    //The following methods are used to remember which Emotion is selected.
    public void selectGreat(View view){        emote = R.drawable.great; }
    public void selectGood(View view){        emote = R.drawable.good; }
    public void selectNeutral(View view){        emote = R.drawable.neutral; }
    public void selectBad(View view){        emote = R.drawable.bad; }
    public void selectWorst(View view){        emote = R.drawable.worst; }
}
