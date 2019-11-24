package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Allows the user to see the moods of the people they are following
 * Issues:
 * Needs to be implemented at all
 */
public class FollowHistory extends AppCompatActivity {
    Participant user;
    ArrayList <String> following;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = (Participant) intent.getSerializableExtra("User");
        setContentView(R.layout.activity_follow_history);
        TextView viewFollowers = findViewById(R.id.viewfollow);
        following = user.getFollowing();
        viewFollowers.setText(following.get(0));
    }
    public void ReturnButton(View view) {
        finish();
    }
}
