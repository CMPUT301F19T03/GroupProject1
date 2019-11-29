package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Allows the user to see the moods of the people they are following
 * Issues:
 * Needs to be implemented at all
 */
public class Community extends MyAppBase {
    ArrayList<String> all_follows;
    Participant user;
    Participant user1;
    ListView follow_activity;

    ArrayAdapter<Mood> follow_moodAdapter;
    ArrayList<Mood> follow_moodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        follow_activity = findViewById(R.id.followactivity_list);
        Intent intent = getIntent();
        user = (Participant) intent.getSerializableExtra("User");

    }

    @Override
    public void setUser(Participant user) {
        this.user = user;
        buildList();
    }

    public void RequestButton(View view) {
        Intent requestIntent = new Intent(this,Requests.class);
        requestIntent.putExtra("User",user);
        startActivity(requestIntent);
    }

    public void FollowButton(View view) {
        Intent requestIntent = new Intent(this,FollowActivity.class);
        requestIntent.putExtra("User",user);
        startActivity(requestIntent);

    }

    public void ReturnButton(View view) {
        finish();
    }

    public void buildList() {
        follow_moodList = new ArrayList<>();
        follow_moodAdapter = new FollowCustomList(this,follow_moodList,user);
        follow_activity.setAdapter(follow_moodAdapter);
        all_follows = user.getFollowing();
        for (int i = 0; i < all_follows.size(); i++) {
            String name1 =  all_follows.get(i);
            users.whereEqualTo("Username", name1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot queryDocumentSnapshots = task.getResult();
                                if (queryDocumentSnapshots != null) {
                                    if (queryDocumentSnapshots.isEmpty()) {
                                        return;
                                    }
                                    user1 = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                                    if (user1.getMoodHistory().isEmpty()) {
                                        return;
                                    }
                                    Mood mood1 = user1.getMoodHistory().get(0);
                                    mood1.setUser(user1.getName());
                                    follow_moodList.add(mood1);
                                    follow_moodAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }

    }

}

