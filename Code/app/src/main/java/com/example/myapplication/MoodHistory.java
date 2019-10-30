package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class is dedicated to the activity_mood_history view and will handle that view's needs
 */
public class MoodHistory extends AppCompatActivity {
    ListView moodHistory;
    ArrayAdapter<Mood> moodArrayAdapter;
    ArrayList<Mood> moodArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);
        moodArrayList= new ArrayList<Mood>();
        // Test code to insure the list view is working

        moodHistory=findViewById(R.id.mood_history);
        Date date= new Date(2020,11,13);
        Time time= new Time(23,22,21);
        int image= R.drawable.neutral;
        Location l = new Location("Hell");
        State s = new State("black","white");
        Mood mood=new Mood(date,time,l,s,"hi","hi",image);
        moodArrayList.add(mood);
        moodArrayAdapter = new CustomList(this,moodArrayList);
        moodHistory.setAdapter(moodArrayAdapter);

    }
}
