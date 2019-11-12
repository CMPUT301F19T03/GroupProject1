package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class is dedicated to acting as the custom adapter to our list used for the Mood History
 * All of it is based on the code provided by Lab 3
 */
public class CustomList extends ArrayAdapter<Mood> {
    private ArrayList<Mood> moods;
    private Context context;
    private CollectionReference users;
    Participant user;
    private CustomList list;

    public CustomList(Context context,ArrayList<Mood> moods,Participant user){
        super(context,0,moods);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        users = db.collection("Users");
        this.moods = moods;
        this.context = context;
        this.user = user;
        this.list = this;
    }

    /**
     * This retrieves the view for the customlist to function
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup
            parent) {
        //Sort the array whenever it needs to be drawn
        Collections.sort(moods, new MoodComparator());

        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }
        final Mood mood = moods.get(position);
        ImageView emoticon = view.findViewById(R.id.emoticon_image);
        TextView time = view.findViewById(R.id.time_text);
        TextView date = view.findViewById(R.id.date_text);
        emoticon.setImageResource(mood.getEmoteIcon());
        time.setText(mood.getStringTime());
        date.setText(mood.getStringDate());
        ImageButton editButton = view.findViewById(R.id.ListEdit);
        ImageButton deleteButton = view.findViewById(R.id.ListDelete);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewMood.class);
                intent.putExtra("Mood",moods.get(position));
                context.startActivity(intent);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Edit.class);
                intent.putExtra("moodList", MoodHistory.moodArrayList);
                int pos = MoodHistory.moodArrayList.indexOf(mood);
                intent.putExtra("pos", pos);
                ((Activity)context).startActivityForResult(intent,1);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moods.remove(position);
                list.notifyDataSetChanged();
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
                                    Log.d("myTag","Deleting from user: "+updated.getName());
                                    users.document(queryDocumentSnapshots.getDocuments().get(0).getId())
                                            .update(userUpdate)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    Log.d("myTag","deleted successfully");
                                                }
                                            });

                                }
                            }
                        });
            }
        });
        return view;
    }
}
