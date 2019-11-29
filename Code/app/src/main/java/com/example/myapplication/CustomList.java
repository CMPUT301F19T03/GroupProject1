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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    private String filter;

    /**
     *
     * @param context this is the Activity that created teh CustomList
     * @param moods This is the array to build the CustomList from
     * @param user This is the user that owns the array
     * @param filter This is the emote to filter by if it exists
     */
    public CustomList(Context context,ArrayList<Mood> moods,Participant user,String filter){
        super(context,0,moods);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        users = db.collection("Users");
        this.moods = moods;
        this.context = context;
        this.user = user;
        this.list = this;
        this.filter = filter;
    }

    /**
     * This retrieves the view for the customlist to function
     * @param position This is the position of this item within the CustomList
     * @param convertView This is the view for this item
     * @param parent This is the parent of this view
     * @return returns a view for a Mood object within a ListView
     */
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup
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

        int id = context.getResources().getIdentifier(mood.getEmoticon(),"drawable",context.getPackageName());
        emoticon.setImageResource(id);

        time.setText(mood.getStringTime());
        date.setText(mood.getStringDate());
        ImageButton editButton = view.findViewById(R.id.ListEdit);
        ImageButton deleteButton = view.findViewById(R.id.ListDelete);
        // When the user clicks on the mood go to ViewMood with that mood
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
                intent.putExtra("user", user);
                int pos = user.getMoodHistory().indexOf(mood);
                intent.putExtra("pos", pos);
                ((Activity)context).startActivityForResult(intent,1);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mood temp = moods.get(position);
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
                if (temp.getPicture()!=null) {
                    FirebaseStorage.getInstance().getReference().child(temp.getPicture()).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("myTag","Deleted image successfully");
                                }
                            });
                }
            }
        });
        return view;
    }

    public String getEmote() {
        return filter;
    }
}
