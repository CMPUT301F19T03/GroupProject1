package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
 * This class is responsible for the Requests activity
 */
public class Requests extends MyAppBase {
    Participant user;
    ArrayList<String> all_requests;
    ListView requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        Intent intent = getIntent();
        user = (Participant) intent.getSerializableExtra("User");
        requests = findViewById(R.id.request_list);
        final Button accept = findViewById(R.id.Accept);
        final Button reject = findViewById(R.id.Reject);
        all_requests = user.getRequests();

        final ArrayAdapter <String> requestsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, all_requests);
        requests.setAdapter(requestsAdapter);
        requests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        all_requests.remove(position);
                        requestsAdapter.notifyDataSetChanged();
                        String name = user.getName();
                        users.whereEqualTo("Username", name)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                                    Participant user1 = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                                    user1.setRequests(all_requests);
                                    uploadUser(user1);
                                }
                            }
                        });
                    }
                });
                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String name2 = all_requests.get(position);
                        all_requests.remove(position);
                        requestsAdapter.notifyDataSetChanged();
                        user.setRequests(all_requests);
                        uploadUser(user);

                        users.whereEqualTo("Username", name2)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                                        Participant user2 = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                                        user2.addFollowing(user.getName());
                                        uploadUser(user2);
                                    }
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void setUser(Participant user) {
        this.user = user;
    }

    /**
     * This button returns the user to the Moodhistory view
     *
     * @param view is the context for this view
     */
    public void ReturnButton(View view) {
        finish();
    }
}
