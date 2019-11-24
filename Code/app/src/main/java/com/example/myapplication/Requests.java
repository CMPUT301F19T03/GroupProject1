package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This class is responsible for the Requests activity
 */
public class Requests extends AppCompatActivity {
    Participant user;
    ArrayList<String> following;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        Intent intent = getIntent();
        user = (Participant) intent.getSerializableExtra("User");
        TextView request = findViewById(R.id.sent);
        following = user.getFollowing();
        request.setText(following.get(0));

    }
    /**
     * This button returns the user to the Moodhistory view
     * @param view is the context for this view
     */
    public void ReturnButton(View view) {
        finish();
    }
}
