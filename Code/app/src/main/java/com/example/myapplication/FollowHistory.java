package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FollowHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_history);
    }
    public void ReturnButton(View view) {
        Intent intent = new Intent(this, MoodHistory.class);
        startActivity(intent);
    }
}
