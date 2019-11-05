package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;

public class Login extends AppCompatActivity {

    String TAG = "myTag";
    FirebaseFirestore db;
    CollectionReference users;
    Participant user;
    Activity main = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        users = db.collection("Users");
        users.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d(TAG, "Something changed");
            }
        });
    }


    public void loginButton(View view) {
        String name = ((EditText) findViewById(R.id.userText)).getText().toString();
        Login(name);

    }

    public void Login(String name) {
        final String Rname = name;
        users.whereEqualTo("Username", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot queryDocumentSnapshots = task.getResult();
                            if (queryDocumentSnapshots.isEmpty()) {
                                HashMap<String, Object> data = new HashMap<>();
                                user = new Participant(Rname);
                                Date date = new Date();
                                Date d = new Date(date.getYear(),date.getMonth(),date.getDay());
                                Time time = new Time(16000);
                                Mood mood = new Mood(d,time,"Test1","Test2",R.drawable.bad);
                                user.addMood(mood);
                                data.put("Participant", user);
                                data.put("Username", Rname);
                                users
                                        .add(data)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(TAG, "New user added Successfully");
                                            }
                                        });
                            } else {
                                user = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                            }
                            Log.d(TAG, "User: " + user.getName());
                            Intent intent = new Intent(main, MoodHistory.class);
                            intent.putExtra("User", user);
                            startActivity(intent);
                        } else {
                            Log.d(TAG, "Retrieve username failed");
                        }
                    }
                });

    }
}
