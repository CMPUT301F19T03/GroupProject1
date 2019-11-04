package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * This class is responsible for the mood history activity
 */
public class MoodHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);
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
