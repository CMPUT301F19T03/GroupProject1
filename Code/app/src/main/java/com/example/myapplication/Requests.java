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
import android.widget.TextView;

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
public class Requests extends AppCompatActivity {
    Participant user;
    ArrayList<String> all_requests;
    ListView requests;
    String TAG = "MY TAG";
    FirebaseFirestore db;
    CollectionReference users;
    Participant user1;
    Participant user2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        Intent intent = getIntent();
        user = (Participant) intent.getSerializableExtra("User");
        requests = findViewById(R.id.request_list);
        db = FirebaseFirestore.getInstance();
        users = db.collection("Users");
        users.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d(TAG, "Something changed");
            }
        });
        final Button accept = findViewById(R.id.Accept);
        final Button reject = findViewById(R.id.Reject);
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
        all_requests = user.getRequests();

        final ArrayAdapter requestsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, all_requests);
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
                                    user1 = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                                    user1.setRequests(all_requests);
                                    final HashMap<String, Object> userUpdate = new HashMap<>();
                                    userUpdate.put("Participant", user1);
                                    users.whereEqualTo("Username", user1.getName())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                                                        Participant updated = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                                                        Log.d(TAG, "Updating user: " + updated.getName());
                                                        users.document(queryDocumentSnapshots.getDocuments().get(0).getId())
                                                                .update(userUpdate)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

                                                                        Log.d(TAG, "Updated user");
                                                                    }
                                                                });
                                                    }
                                                }
                                            });


                                }
                            }
                        });
                    }
                });
                Log.d(TAG, "NEAR ACCEPT");
                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = user.getName();
                        final String name2 = all_requests.get(position);
                        Log.d(TAG, "INSIDE ACCEPT");
                        all_requests.remove(position);
                        Log.d(TAG, "NOOOO ACCEPT");
                        requestsAdapter.notifyDataSetChanged();

                        users.whereEqualTo("Username", name)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                                    user1 = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                                    user1.setRequests(all_requests);
                                    final HashMap<String, Object> userUpdate = new HashMap<>();
                                    userUpdate.put("Participant", user1);
                                    users.whereEqualTo("Username", user1.getName())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                                                        Participant updated = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                                                        Log.d(TAG, "Updating user: " + updated.getName());
                                                        users.document(queryDocumentSnapshots.getDocuments().get(0).getId())
                                                                .update(userUpdate)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

                                                                        Log.d(TAG, "Updated user");
                                                                    }
                                                                });
                                                    }
                                                }
                                            });


                                }
                            }
                        });
                        Log.d(TAG, "HURRAY");
                        users.whereEqualTo("Username", name2)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                                    user2 = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                                    user2.addFollowing(user.getName());
                                    final HashMap<String, Object> userUpdate = new HashMap<>();
                                    userUpdate.put("Participant", user2);
                                    users.whereEqualTo("Username", user2.getName())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                                                        Participant updated = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                                                        Log.d(TAG, "Updating user: " + updated.getName());
                                                        users.document(queryDocumentSnapshots.getDocuments().get(0).getId())
                                                                .update(userUpdate)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

                                                                        Log.d(TAG, "Updated user");
                                                                    }
                                                                });
                                                    }
                                                }
                                            });


                                }
                            }
                        });
                    }
                });
            }
        });



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
