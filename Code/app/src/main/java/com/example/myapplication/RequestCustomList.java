package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static android.content.ContentValues.TAG;
import static android.content.Intent.getIntent;
import static com.example.myapplication.MoodHistory.moodArrayList;

public class RequestCustomList extends ArrayAdapter<Participant> {
    private ArrayList<Participant> requests;
    ArrayAdapter<String> requestArrayAdapter;
    private Context context;
    private CollectionReference users;
    Participant user;
    Activity requestActivity;
    FirebaseFirestore db;
    private RequestCustomList list;




    public RequestCustomList(Context context, ArrayList<Participant> requests, Participant user) {
        super(context, 0, requests);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        users = db.collection("Users");
        this.requests = requests;
        this.context = context;
        this.user = user;
        this.list = this;

    }




    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;
        //Participant request = requests.get(position);

        //final TextView requestName = view.findViewById(R.id.request_text);



        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.request_content, parent, false);
        }

        Participant request = requests.get(position);

        //Participant name = getItem(position);
        //requestName.setText(name.getName());

        final TextView requestName = view.findViewById(R.id.request_text);

        requestName.setText(request.getName());

        Button acceptButton = view.findViewById(R.id.requestAccept);
        Button rejectButton = view.findViewById(R.id.requestReject);






        rejectButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                requests.remove(position);
                                                list.notifyDataSetChanged();
                                                final HashMap<String, Object> userUpdate = new HashMap<>();
                                                userUpdate.put("Participant", user);
                                                users.whereEqualTo("Username", user.getName())
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                                                                    Participant updated = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                                                                    Log.d("myTag", "Deleting from requests: " + updated.getRequests());
                                                                    users.document(queryDocumentSnapshots.getDocuments().get(0).getId())
                                                                            .update(userUpdate)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {

                                                                                    Log.d("myTag", "deleted successfully");
                                                                                }
                                                                            });

                                                                }


                                                            }
                                                        });
                                            }
                                        });
        acceptButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final String request_name = requestName.getText().toString();
                list.notifyDataSetChanged();

                HashMap<String, String> data = new HashMap<>();
                data.put("followers", request_name);

                users.document(request_name)
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "follower is added to firestore");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Error: "+ e.getMessage());
                            }
                        });





            }

        });


    return view;}

}

