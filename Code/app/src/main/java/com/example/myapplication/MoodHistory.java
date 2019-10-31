package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MoodHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);
    }


    public void addButton(View view) {
        Intent intent = new Intent(this, Add.class);
        startActivity(intent);
    }

    public void editButton(View view) {
        Intent intent = new Intent(this, Edit.class);
        startActivity(intent);
    }
    public void mapButton(View view) {
        Intent intent = new Intent(this, Usermap.class);
        startActivity(intent);
    }

    public void viewButton(View view) {
        Intent intent = new Intent(this, com.example.myapplication.View.class);
        startActivity(intent);
    }

    public void viewFollowButton(View view) {
        Intent intent = new Intent(this, FollowHistory.class);
        startActivity(intent);
    }
    public void requestButton(View view) {
        Intent intent = new Intent(this, Requests.class);
        startActivity(intent);
    }
}
