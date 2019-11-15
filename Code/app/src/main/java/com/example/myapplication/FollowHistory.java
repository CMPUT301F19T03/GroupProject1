package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Allows the user to see the moods of the people they are following
 * Issues:
 * Needs to be implemented at all
 */
public class FollowHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_history);
    }
    public void ReturnButton(View view) {
        finish();
    }
}
