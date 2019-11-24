package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is dedicated to the activity_mood_history view and will handle that view's needs
 */
public class MoodHistory extends AppCompatActivity {
    String TAG = "myTag";
    ListView moodHistory;
    ArrayAdapter<Mood> moodArrayAdapter;
    static ArrayList<Mood> moodArrayList;
    ArrayAdapter<Mood> filterAdapter;
    ArrayList<Mood> filterList;
    Participant user;
    FirebaseFirestore db;
    CollectionReference users;
    Activity historyActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);
        historyActivity = this;
        db = FirebaseFirestore.getInstance();
        users = db.collection("Users");
        users.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d(TAG, "Something changed");
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("Username")==user.getName()) {
                        user = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                    }
                }
            }
        });
        Intent intent = getIntent();
        user = (Participant) intent.getSerializableExtra("User");
        moodArrayList = user.getMoodHistory();
        moodArrayAdapter = new CustomList(this,moodArrayList,user);

        moodHistory = findViewById(R.id.mood_history);
        moodHistory.setAdapter(moodArrayAdapter);

    }

    /**
     * this button sends the user to the add activity
     * @param view is the view context for this class
     */
    public void addButton(View view) {
        Intent intent = new Intent(this, Add.class);
        intent.putExtra("moodList",moodArrayList);
        startActivityForResult(intent,1);
    }
    /**
     * this button sends the user to the usermap activity
     * @param view is the view context for this class
     */
    public void mapButton(View view) {
        Intent intent = new Intent(this, Usermap.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }
    /**
     * this button sends the user to the Followed mood history activity
     * @param view is the view context for this class
     */
    public void viewFollowButton(View view) {
        Intent intent = new Intent(this, FollowHistory.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }
    /**
     * this button sends the user to the requests activity
     * @param view is the view context for this class
     */
    public void requestButton(View view) {
        Intent intent = new Intent(this, Requests.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }

    public  void clearFilterButton(View view) {
        moodHistory.setAdapter(moodArrayAdapter);
    }

    public void filterButton(View view) {
        HorizontalScrollView filterScroll = findViewById(R.id.FilterScroll);
        if (filterScroll.getVisibility()==View.GONE) {
            filterScroll.setVisibility(View.VISIBLE);
        } else {
            filterScroll.setVisibility(View.GONE);
        }
    }
    public void greatFilterButton(View view) {
        filterList = new ArrayList<>();
        for (Mood mood : moodArrayList) {
            if (mood.getEmoticon().equals("great")) {
                filterList.add(mood);
            }
        }
        filterAdapter = new CustomList(this,filterList,user);
        moodHistory.setAdapter(filterAdapter);
    }

    public void goodFilterButton(View view) {
        filterList = new ArrayList<>();
        for (Mood mood : moodArrayList) {
            if (mood.getEmoticon().equals("good")) {
                filterList.add(mood);
            }
        }
        filterAdapter = new CustomList(this,filterList,user);
        moodHistory.setAdapter(filterAdapter);
    }
    public void neutralFilterButton(View view) {
        filterList = new ArrayList<>();
        for (Mood mood : moodArrayList) {
            if (mood.getEmoticon().equals("neutral")) {
                filterList.add(mood);
            }
        }
        filterAdapter = new CustomList(this,filterList,user);
        moodHistory.setAdapter(filterAdapter);
    }
    public void badFilterButton(View view) {
        filterList = new ArrayList<>();
        for (Mood mood : moodArrayList) {
            if (mood.getEmoticon().equals("bad")) {
                filterList.add(mood);
            }
        }
        filterAdapter = new CustomList(this,filterList,user);
        moodHistory.setAdapter(filterAdapter);
    }
    public void worstFilterButton(View view) {
        filterList = new ArrayList<>();
        for (Mood mood : moodArrayList) {
            if (mood.getEmoticon().equals("worst")) {
                filterList.add(mood);
            }
        }
        filterAdapter = new CustomList(this,filterList,user);
        moodHistory.setAdapter(filterAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1) {
            if (resultCode==RESULT_OK) {
                Log.d(TAG,"Return from add");
                moodArrayList = (ArrayList<Mood>) data.getSerializableExtra("Addmood");
                moodArrayAdapter = new CustomList(this,moodArrayList,user);
                moodHistory.setAdapter(moodArrayAdapter);
                user.setMoodHistory(moodArrayList);
                final HashMap<String, Object> userUpdate = new HashMap<>();
                userUpdate.put("Participant", user);
                users.whereEqualTo("Username",user.getName())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                                    Participant updated = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                                    Log.d(TAG,"Updating user: "+updated.getName());
                                    users.document(queryDocumentSnapshots.getDocuments().get(0).getId())
                                            .update(userUpdate)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    Log.d(TAG,"Updated user");
                                                }
                                            });

                                }
                            }
                        });

            }
        }
    }
}
