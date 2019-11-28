package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class is responsible for the View Mood activity
 */

public class ViewMood extends AppCompatActivity {
    Mood currentMood;
    TextView date;
    TextView time;
    TextView reason;
    TextView social;
    ImageView emote;
    ImageView picture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mood);
        Intent intent = getIntent();
        currentMood = (Mood) intent.getSerializableExtra("Mood");

        date = findViewById(R.id.dateView);
        date.setText(currentMood.getStringDate());
        time = findViewById(R.id.timeView);
        time.setText(currentMood.getStringTime());
        reason = findViewById(R.id.reasonView);
        reason.setText(currentMood.getReason());
        social = findViewById(R.id.socialView);
        social.setText(currentMood.getSocialSituation());
        emote = findViewById(R.id.emoticonView);
        int id = getResources().getIdentifier(currentMood.getEmoticon(), "drawable",getPackageName());
        emote.setImageResource(id);
        picture = findViewById(R.id.imageView2);

    }
    /**
     * This button returns the user to the Moodhistory view
     * @param view is the context for this view
     */
    public void ReturnButton(View view){
        finish();
    }

    public void LocationButton(View view) {
        if (currentMood.getLatitude()!=null) {
            Intent intent = new Intent(this, ViewMapActivity.class);
            intent.putExtra("Lat", currentMood.getLatitude());
            intent.putExtra("Long", currentMood.getLongitude());
            startActivity(intent);
        } else {
            Toast.makeText(this,"No Location for this mood",Toast.LENGTH_SHORT).show();
        }
    }
}
