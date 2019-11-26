package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This class is responsible for the Requests activity
 */
public class Requests extends ArrayAdapter<Participant> {
    Participant user;
    ArrayList<Participant> requests;
    private Context context;

    public Requests(Context context, ArrayList<Participant> requests){
        super(context,0, requests);
        this.requests = requests;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.request_content, parent, false);

        }

        Participant request = requests.get(position);

        TextView requestName = view.findViewById(R.id.request_text);

        requestName.setText(request.getName());


        return view;

        /***super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        Intent intent = getIntent();
        user = (Participant) intent.getSerializableExtra("User");
        TextView request = findViewById(R.id.sent);
        //following = user.getName();
        //request.setText(following.get(0)); ***/

    }
    /**
     * This button returns the user to the Moodhistory view
     * @param view is the context for this view
     */
}

