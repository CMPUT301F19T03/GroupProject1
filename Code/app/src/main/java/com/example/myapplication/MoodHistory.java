package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * This class is dedicated to the activity_mood_history view and will handle that view's needs
 */
public class MoodHistory extends AppCompatActivity {
    ListView moodHistory;
    ArrayAdapter<Mood> moodArrayAdapter;
    ArrayList<Mood> moodArrayList;
    Participant user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);
        Intent intent = getIntent();
        user = (Participant) intent.getSerializableExtra("User");
        //How to sort the array list:Collections.sort(moodArrayList, new MoodComparator());
    }

    /**
     * this button sends the user to the add activity
     * @param view is the view context for this class
     */
    public void addButton(View view) {
        Intent intent = new Intent(this, Add.class);
        startActivity(intent);
    }
    /**
     * this button sends the user to the edit activity
     * @param view is the view context for this class
     */
    public void editButton(View view) {
        Intent intent = new Intent(this, Edit.class);
        startActivity(intent);
    }
    /**
     * this button sends the user to the usermap activity
     * @param view is the view context for this class
     */
    public void mapButton(View view) {
        Intent intent = new Intent(this, Usermap.class);
        startActivity(intent);
    }
    /**
     * this button sends the user to the ViewMood activity
     * @param view is the view context for this class
     */
    public void viewButton(View view) {
        Intent intent = new Intent(this, ViewMood.class);
        Date date = new Date();
        Time time = new Time(16000);
        Mood mood = new Mood(date,time,"Test1","Test2");
        intent.putExtra("Mood",mood);
        startActivity(intent);
    }
    /**
     * this button sends the user to the Followed mood history activity
     * @param view is the view context for this class
     */
    public void viewFollowButton(View view) {
        Intent intent = new Intent(this, FollowHistory.class);
        startActivity(intent);
    }
    /**
     * this button sends the user to the requests activity
     * @param view is the view context for this class
     */
    public void requestButton(View view) {
        Intent intent = new Intent(this, Requests.class);
        startActivity(intent);
    }
}
