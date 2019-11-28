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

import org.w3c.dom.Text;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class is dedicated to acting as the custom adapter to our list used for the Mood History
 * All of it is based on the code provided by Lab 3
 */
public class FollowCustomList extends ArrayAdapter<Mood> {
    private ArrayList<Mood> moods;
    private Context context;
    private CollectionReference users;
    Participant user;
    private FollowCustomList list;

    public FollowCustomList(Context context,ArrayList<Mood> moods,Participant user){
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
     * @param position This is the position of this item within the CustomList
     * @param convertView This is the view for this item
     * @param parent This is the parent of this view
     * @return returns a view for a Mood object within a ListView
     */
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup
            parent) {
        //Sort the array whenever it needs to be drawn
        Collections.sort(moods, new MoodComparator());

        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.follow_content, parent,false);
        }
        final Mood mood = moods.get(position);
        ImageView emoticon = view.findViewById(R.id.emoticon_image1);
        TextView time = view.findViewById(R.id.time_text1);
        TextView date = view.findViewById(R.id.date_text1);
        TextView user = view.findViewById(R.id.user_text);
        user.setText(mood.getStringDate());
        emoticon.setImageResource(mood.getEmoteIcon());
        time.setText(mood.getStringTime());
        date.setText(mood.getStringDate());




        return view;
    }
}
