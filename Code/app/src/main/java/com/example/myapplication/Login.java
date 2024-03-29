package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

/**
 * 
 */
public class Login extends AppCompatActivity {

    String TAG = "myTag";
    CollectionReference users;
    Participant user;
    Activity main = this;

    /**
     * create the view and get a reference to the database
     * @param savedInstanceState values saved by previous instances of this class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        users = FirebaseFirestore.getInstance().collection("Users");
    }

    /**
     * if the user has entreed a name login with that name
     * @param view the button that was clicked
     */
    public void loginButton(View view) {
        String name = ((EditText) findViewById(R.id.userText)).getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this,"Username can't be empty",Toast.LENGTH_SHORT).show();
        } else {
            LoginFunc(name);
        }

    }

    @Override
    public void onBackPressed() {
        // Don't allow user to press back
    }

    /**
     * creates the user profile from the firebase and logs them in
     * @param name the name to log in with
     */
    public void LoginFunc(String name) {
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
                                data.put("Participant", user);
                                data.put("Username", Rname);
                                users
                                        .add(data)
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG,"Adding new User Failed: "+e);
                                            }
                                        })
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(TAG, "New user added Successfully");
                                                user.setUID(documentReference.getId());

                                            }
                                        });
                            } else {
                                Log.d("myTag","found existing user");
                                user = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                                user.setUID(queryDocumentSnapshots.getDocuments().get(0).getId());
                            }
                            EditText nameText = findViewById(R.id.userText);
                            nameText.setText("");
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
