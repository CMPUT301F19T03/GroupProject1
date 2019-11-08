package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
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
    Spinner add_situation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        final Intent intent = getIntent();
        final ArrayList<Mood> moodList = (ArrayList<Mood>) intent.getSerializableExtra("moodList");
        timeText = findViewById(R.id.timeView);
        dateText = findViewById(R.id.dateView);
        cal = Calendar.getInstance();
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        res = getResources();
        timeString = String.format(res.getString(R.string.TimeString),cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE));
        timeText.setText(timeString);
        dateString = String.format(res.getString(R.string.DateString),cal.get(Calendar.YEAR),(cal.get(Calendar.MONTH)+1),cal.get(Calendar.DAY_OF_MONTH));
        dateText.setText(dateString);
        add_situation = findViewById(R.id.spinner1);
        ArrayAdapter<String> myadapter = new ArrayAdapter<String>(Add.this,
                android.R.layout.simple_expandable_list_item_1,getResources().getStringArray(R.array.SocialSituation));
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_situation.setAdapter(myadapter);

        Button timePick = findViewById(R.id.timeButton);
        timePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time Picker");
            }
        });

        final Button datePick = findViewById(R.id.dateButton);
        datePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
        final EditText ReasonText = findViewById(R.id.addReasonText);
        //final EditText SocialText = findViewById(R.id.addSocialText);
        final int emote = R.drawable.great;

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
                Date date = cal.getTime();
                Mood mood;
                String reason = ReasonText.getText().toString();
                String social = add_situation.getSelectedItem().toString();
                if (locationToggle.isChecked()) {
                    mood = new Mood(date,userLocation.latitude,userLocation.longitude,reason,social,emote);
                } else {
                    mood = new Mood(date, reason, social, emote);
                }
                moodList.add(mood);
                Intent data = new Intent();
                data.putExtra("Addmood",moodList);
                setResult(RESULT_OK,data);
                finish();
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
            if (resultCode==RESULT_CANCELED) {
                locationToggle.setChecked(false);
            } else if (resultCode==RESULT_OK) {
                userLocation = data.getExtras().getParcelable("location");
                Log.d("myTag","getlat: "+userLocation.latitude);
            }
        }
    }
}
