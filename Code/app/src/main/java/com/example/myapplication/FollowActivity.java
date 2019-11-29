package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class FollowActivity extends AppCompatActivity {
    String TAG = "MY TAG";
    FirebaseFirestore db;
    CollectionReference users;
    Participant user;
    Participant user1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        final EditText Username =  findViewById(R.id.Username);
        final Button add = findViewById(R.id.Add_button);
        final TextView error = findViewById(R.id.ErrorM);
        db = FirebaseFirestore.getInstance();
        users = db.collection("Users");
        users.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d(TAG, "Something changed");
                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        if (doc.get("Username").equals(user1.getName())) {
                            user1 = doc.get("Participant", Participant.class);
                        }
                    }
                }
            }
        });
        Intent intent = getIntent();
        user1 = (Participant) intent.getSerializableExtra("User");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = Username.getText().toString();
                if (user1.getName().equals(name)) {
                    error.setText("You can't follow yourself");
                    return;
                }
                users.whereEqualTo("Username", name)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            QuerySnapshot queryDocumentSnapshots = task.getResult();
                            if (queryDocumentSnapshots.isEmpty()) {
                                error.setText("UserName not Found");
                            } else if (user1.getFollowing().contains(name)) {
                                error.setText(String.format("You are already following %s", name));
                            } else{
                                user = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                                if (user.getRequests().contains(user1.getName())) {
                                    error.setText(String.format("%s already has a request from you", user.getName()));
                                    return;
                                }
                                user.addRequest(user1.getName());
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
                                finish();
                            }
                        }
                    }
                });
            }
        });
    }
}
