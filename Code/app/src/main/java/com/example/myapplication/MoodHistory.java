package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
    int selected = -1;

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
                if (queryDocumentSnapshots.getDocuments().size()>0) {
                    user = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                }
            }
        });
        Intent intent = getIntent();
        user = (Participant) intent.getSerializableExtra("User");
        moodArrayList = user.getMoodHistory();
        moodArrayAdapter = new CustomList(this,moodArrayList);

        moodHistory = findViewById(R.id.mood_history);
        moodHistory.setAdapter(moodArrayAdapter);
        moodHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                selected = pos;
            }
        });

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
        if (selected!=-1) {
            Intent intent = new Intent(this, ViewMood.class);
            intent.putExtra("pos", selected);
            intent.putExtra("Mood",moodArrayList.get(selected));
            selected = -1;
            startActivity(intent);
        }
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
                moodArrayList = (ArrayList<Mood>) data.getSerializableExtra("Addmood");
                //How to sort the array list:
                Collections.sort(moodArrayList, new MoodComparator());
                moodArrayAdapter = new CustomList(this,moodArrayList);
                moodHistory.setAdapter(moodArrayAdapter);
                user.setMoodHistory(moodArrayList);
                final HashMap<String, Object> userUpdate = new HashMap<>();
                userUpdate.put("Participant", user);
                userUpdate.put("Username", user.getName());
                users.whereEqualTo("Username",user.getName())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                                    users.document(queryDocumentSnapshots.getDocuments().get(0).getId())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    users.add(userUpdate)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    Log.d(TAG,"Updated user");
                                                                }
                                                            });
                                                }
                                            });

                                }
                            }
                        });

            }
        }
    }
}
