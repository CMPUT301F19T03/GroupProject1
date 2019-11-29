package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    ArrayList<Mood> moodArrayList;
    CustomList filterAdapter;
    ArrayList<Mood> filterList;
    Participant user;
    FirebaseFirestore db;
    CollectionReference users;

    static Drawable buttonBackground;
    static int filterPressed;
    ImageButton greatFilter;
    ImageButton goodFilter;
    ImageButton neutralFilter;
    ImageButton badFilter;
    ImageButton worstFilter;

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
                if (queryDocumentSnapshots != null) {
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getDocument().get("Username").equals(user.getName())) {
                            user = doc.getDocument().get("Participant", Participant.class);
                            user.setUID(doc.getDocument().getId());
                            moodArrayList = user.getMoodHistory();
                            moodArrayAdapter.clear();
                            moodArrayAdapter.addAll(user.getMoodHistory());
                        }
                    }
                }
            }
        });
        Intent intent = getIntent();
        user = (Participant) intent.getSerializableExtra("User");
        moodArrayList = user.getMoodHistory();
        moodArrayAdapter = new CustomList(this,moodArrayList,user,null);

        moodHistory = findViewById(R.id.mood_history);
        moodHistory.setAdapter(moodArrayAdapter);

        greatFilter = findViewById(R.id.GreatFilterButton);
        goodFilter = findViewById(R.id.GoodFilterButton);
        neutralFilter = findViewById(R.id.NeutralFilterButton);
        badFilter = findViewById(R.id.BadFilterButton);
        worstFilter = findViewById(R.id.WorstFilterButton);
        buttonBackground = worstFilter.getBackground();
        filterPressed = getResources().getColor(android.R.color.darker_gray);

    }

    /**
     * this button sends the user to the add activity
     * @param view is the view context for this class
     */
    public void addButton(View view) {
        Intent intent = new Intent(this, Add.class);
        intent.putExtra("user",user);
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
    public void communityButton(View view) {
        Intent intent = new Intent(this, Community.class);
        intent.putExtra("User",user);
        startActivityForResult(intent,2);
    }
    /**
     * this button sends the user to the requests activity
     * @param view is the view context for this class
     */
    public void logOut(View view) {
        finish();
    }

    public  void clearFilterButton(View view) {
        unSetFilterColors();
        moodHistory.setAdapter(moodArrayAdapter);
        filterAdapter = null;
    }

    public void filterButton(View view) {
        HorizontalScrollView filterScroll = findViewById(R.id.FilterScroll);
        if (filterScroll.getVisibility()==View.GONE) {
            filterScroll.setVisibility(View.VISIBLE);
        } else {
            filterScroll.setVisibility(View.GONE);
        }
    }

    public void createFilter(View view) {
        unSetFilterColors();
        view.setBackgroundColor(filterPressed);
        ArrayList<Mood> filterList = new ArrayList<>();
        for (Mood mood : user.getMoodHistory()) {
            if (mood.getEmoticon().equals(view.getTag().toString())) {
                filterList.add(mood);
            }
        }
        filterAdapter = new CustomList(this,filterList,user,view.getTag().toString());
        moodHistory.setAdapter(filterAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        unSetFilterColors();
        recoverFilter();
        findViewById(R.id.FilterScroll).setVisibility(View.GONE);
        if (requestCode==1) {
            if (resultCode==RESULT_OK) {
                Log.d(TAG,"Return from add");
                moodArrayList = (ArrayList<Mood>) data.getSerializableExtra("Addmood");
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
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, "Update Failed: "+e);
                                                }
                                            })
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG,"Updated user Successfully");
                                                }
                                            });

                                }
                            }
                        });
            }
        }
    }
    public void unSetFilterColors() {
        int padding = getResources().getDimensionPixelSize(R.dimen.padding_large);
        greatFilter.setBackground(buttonBackground);
        greatFilter.setPadding(padding,padding,padding,padding);
        goodFilter.setBackground(buttonBackground);
        goodFilter.setPadding(padding,padding,padding,padding);
        neutralFilter.setBackground(buttonBackground);
        neutralFilter.setPadding(padding,padding,padding,padding);
        badFilter.setBackground(buttonBackground);
        badFilter.setPadding(padding,padding,padding,padding);
        worstFilter.setBackground(buttonBackground);
        worstFilter.setPadding(padding,padding,padding,padding);
    }

    public void recoverFilter() {
        if (filterAdapter!=null) {
            ImageButton filter = null;
            switch (filterAdapter.getEmote()) {
                case "great":
                    filter = findViewById(R.id.GreatFilterButton);
                    break;
                case "good":
                    filter = findViewById(R.id.GoodFilterButton);
                    break;
                case "neutral":
                    filter = findViewById(R.id.NeutralFilterButton);
                    break;
                case "bad":
                    filter = findViewById(R.id.BadFilterButton);
                    break;
                case "worst":
                    filter = findViewById(R.id.WorstFilterButton);
                    break;
            }
            createFilter(filter);
        } else {
            clearFilterButton(findViewById(R.id.ClearFilterButton));
        }
    }
}
