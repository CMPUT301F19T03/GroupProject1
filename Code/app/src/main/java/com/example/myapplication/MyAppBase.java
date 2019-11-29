package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

import java.util.HashMap;

public abstract class MyAppBase extends AppCompatActivity {
    CollectionReference users;
    String TAG = "myTag";
    Participant user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = (Participant)intent.getSerializableExtra("User");
        users = FirebaseFirestore.getInstance().collection("Users");
        users.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d("myTag", "Something changed");
                if (queryDocumentSnapshots != null) {
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getDocument().get("Username").equals(user.getName())) {
                            user = doc.getDocument().get("Participant", Participant.class);
                            user.setUID(doc.getDocument().getId());
                            setUser(user);
                        }
                    }
                }
            }
        });
    }

    public abstract void setUser(Participant user);
    public void uploadUser(Participant user) {
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
