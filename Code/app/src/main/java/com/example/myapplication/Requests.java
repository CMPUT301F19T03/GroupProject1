package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

/**
 * This class is responsible for the Requests activity
 */
public class Requests extends AppCompatActivity {
    String TAG = "MY TAG";
    FirebaseFirestore db;
    CollectionReference users;
    Participant user;
    Participant user1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        db = FirebaseFirestore.getInstance();
        users = db.collection("Users");
        users.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d(TAG, "Something changed");
            }
        });
        Intent intent = getIntent();
        user1 = (Participant) intent.getSerializableExtra("User");
        final EditText username = findViewById(R.id.UserName);
        final TextView Error = findViewById(R.id.ErrorM);
        Button done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                //intent.putExtra("UserName", s);
                //setResult(1,intent);
                users.whereEqualTo("Username", name)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            QuerySnapshot queryDocumentSnapshots = task.getResult();
                            if (queryDocumentSnapshots.isEmpty()) {
                                  Error.setText("UserName not Found");
                            }
                        else
                            {
                                user = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                                user.addFollowing("Request sent from " + user1.getName());
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
                                                            });}
                                            }
                                        });


                                finish();

                        }
                        }
                    }
                });
                //finish();
            }
        });
    }
    /**
     * This button returns the user to the Moodhistory view
     * @param view is the context for this view
     */
    public void ReturnButton(View view) {
        finish();
    }
}
