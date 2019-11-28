package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;

/**
 * Allows the user to see the moods of the people they are following
 * Issues:
 * Needs to be implemented at all
 */
public class FollowHistory extends AppCompatActivity {
    ArrayList<String> all_follows;
    FirebaseFirestore db;
    String TAG = "MY TAG";
    Participant user;
    Participant user1;
    ListView follow_activity;

    ArrayAdapter<Mood> follow_moodAdapter;
    ArrayList<Mood> follow_moodList;

    private ArrayList<Mood> followCustomList;

    private CollectionReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_history);
        follow_activity = findViewById(R.id.folloactivity_list);
        Intent intent = getIntent();
        user = (Participant) intent.getSerializableExtra("User");
        db = FirebaseFirestore.getInstance();
        users = db.collection("Users");
        follow_moodList = new ArrayList<>();
        follow_moodAdapter = new FollowCustomList(this,follow_moodList,user);
        follow_activity.setAdapter(follow_moodAdapter);


        all_follows = user.getFollowing();
        Log.d(TAG, ""+all_follows.size() );
        for (int i = 0; i < all_follows.size(); i++) {
            Log.d(TAG, ""+all_follows.get(i) );
            Log.d(TAG, "inside for loop");
            String name1 =  all_follows.get(i);
            Log.d(TAG, "inside for loop1");
            users.whereEqualTo("Username", name1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot queryDocumentSnapshots = task.getResult();
                                    Log.d(TAG,"FOUND");
                                    try{
                                        user1 = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                                        Mood mood1 = user1.getMoodHistory().get(0);
                                        mood1.setUser(user1.getName());
                                        follow_moodList.add(mood1);
                                        follow_moodAdapter.notifyDataSetChanged();
                    }catch(Exception e){
                                        Log.d(TAG,"No MoodHistory done");;
                                }
                            }
                            Log.d(TAG,"Exiting loop1");
                            }
                    });
            Log.d(TAG,"Exiting loop");

    }

    }

    public void ReturnButton(View view) {
        finish();
    }

}

