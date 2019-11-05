package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
/**
 * This class is responsible for the Usermap activity
 */
public class Usermap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermap);
    }
    /**
     * This button returns the user to the Moodhistory view
     * @param view is the context for this view
     */
    public void ReturnButton(View view) {
        Intent intent = new Intent(this, MoodHistory.class);
        startActivity(intent);
    }

    /**
     * This button sends the user to the other map view which displays their followers
     * @param view is the context for this view
     */
    public void OthersButton(View view) {
        Intent intent = new Intent(this, OtherMap.class);
        startActivity(intent);
    }
}
