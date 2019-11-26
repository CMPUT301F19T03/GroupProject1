package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestActivity extends AppCompatActivity {
    String TAG = "myTag";
    ListView requestList;
    ArrayAdapter<String> requestArrayAdapter;
    ArrayList<String> requestArrayList;
    Participant user;
    FirebaseFirestore db;
    CollectionReference users;
    Activity requestActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        requestList = findViewById(R.id.request_list);
        requestActivity = this;
        db = FirebaseFirestore.getInstance();
        users = db.collection("Users");

        users.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                      @Override
                                      public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                          Log.d(TAG, "Something changed");
                                          for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                              if (doc.get("Username") == user.getName()) {
                                                  user = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                                              }
                                          }
                                      }
                                  }
        );
        Intent intent = getIntent();
        user = (Participant) intent.getSerializableExtra("User");
        requestArrayList = user.getRequests();

        //requestArrayAdapter= new RequestCustomList(this, requestArrayList, user);




        }







    }




