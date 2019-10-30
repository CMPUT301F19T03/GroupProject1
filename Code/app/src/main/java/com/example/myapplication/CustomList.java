package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class is dedicated to acting as the custom adapter to our list used for the Mood History
 * All of it is based on the code provided by Lab 3
 */
public class CustomList extends ArrayAdapter<Mood> {
    private ArrayList<Mood> moods;
    private Context context;

    public CustomList(Context context,ArrayList<Mood> moods){
        super(context,0,moods);
        this.moods=moods;
        this.context=context;
    }

    /**
     * This retrieves the view for the customlist to function
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {

        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }
        Mood mood = moods.get(position);
        ImageView emoticon = view.findViewById(R.id.emoticon_image);
        TextView time = view.findViewById(R.id.time_text);
        TextView date = view.findViewById(R.id.date_text);
        emoticon.setImageResource(R.drawable.neutral); //This is a placeholder until the Mood class is sorted out
        time.setText(mood.getTime().toString());
        date.setText(mood.getDate().toString());
        return view;
    }
}
