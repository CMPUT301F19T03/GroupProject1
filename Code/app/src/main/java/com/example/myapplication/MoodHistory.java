package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * This class is dedicated to the activity_mood_history view and will handle that view's needs
 */
public class MoodHistory extends AppCompatActivity {
    String TAG = "myTag";
    ListView moodHistory;
    ArrayAdapter<Mood> moodArrayAdapter;
    ArrayList<Mood> moodArrayList;
    Participant user;
    FirebaseFirestore db;
    CollectionReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);
        db = FirebaseFirestore.getInstance();
        users = db.collection("Users");
        users.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d(TAG, "Something changed");
            }
        });
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
        startActivityForResult(intent,1);
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
//        Calendar c = Calendar.getInstance();
//        Mood mood = new Mood(c.getTime(),"Test1","Test2",R.drawable.bad);
        Mood mood = user.getMoodHistory().get(1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1) {
            if (resultCode==RESULT_OK) {
                Log.d(TAG,"Return from add");
                Mood addmood = (Mood) data.getSerializableExtra("Addmood");
                Log.d(TAG,addmood.getReason());
                user.addMood(addmood);
            }
        }
    }
}
