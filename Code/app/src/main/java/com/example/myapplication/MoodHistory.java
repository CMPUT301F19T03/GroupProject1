package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ListView;

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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is dedicated to the activity_mood_history view and will handle that view's needs
 */
public class MoodHistory extends MyAppBase {
    ListView moodHistory;
    ArrayAdapter<Mood> moodArrayAdapter;
    CustomList filterAdapter;

    Participant user;

    Drawable buttonBackground;
    int filterPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);
        Intent intent = getIntent();
        user = (Participant) intent.getSerializableExtra("User");
        moodArrayAdapter = new CustomList(this,user.getMoodHistory(),user,null);

        moodHistory = findViewById(R.id.mood_history);
        moodHistory.setAdapter(moodArrayAdapter);

        buttonBackground = findViewById(R.id.WorstFilterButton).getBackground();
        filterPressed = getResources().getColor(android.R.color.darker_gray);

    }

    @Override
    public void setUser(Participant user) {
        this.user = user;
        moodArrayAdapter.clear();
        moodArrayAdapter.addAll(user.getMoodHistory());
        recoverFilter();
    }

    /**
     * this button sends the user to the add activity
     * @param view is the view context for this class
     */
    public void addButton(View view) {
        Intent intent = new Intent(this, Add.class);
        intent.putExtra("User",user);
        startActivityForResult(intent,1);
    }
    /**
     * this button sends the user to the usermap activity
     * @param view is the view context for this class
     */
    public void mapButton(View view) {
        Intent intent = new Intent(this, Usermap.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }
    /**
     * this button sends the user to the Followed mood history activity
     * @param view is the view context for this class
     */
    public void communityButton(View view) {
        Intent intent = new Intent(this, Community.class);
        intent.putExtra("User",user);
        startActivityForResult(intent,2);
    }
    /**
     * this button sends the user to the requests activity
     * @param view is the view context for this class
     */
    public void logOut(View view) {
        finish();
    }

    /**
     * clear the filter and show all moods for the user
     * @param view the button that was pressed
     */
    public  void clearFilterButton(View view) {
        unSetFilterColors();
        moodHistory.setAdapter(moodArrayAdapter);
        filterAdapter = null;
    }

    /**
     * show or hide the linearlayout that holds the filter buttons
     * @param view the button that was clicked
     */
    public void filterButton(View view) {
        HorizontalScrollView filterScroll = findViewById(R.id.FilterScroll);
        if (filterScroll.getVisibility()==View.GONE) {
            filterScroll.setVisibility(View.VISIBLE);
        } else {
            filterScroll.setVisibility(View.GONE);
        }
    }

    /**
     * create the filtered view with all matching moods in it
     * @param view the button that was clicked to select a view
     */
    public void createFilter(View view) {
        unSetFilterColors();
        view.setBackgroundColor(filterPressed);
        ArrayList<Mood> filterList = new ArrayList<>();
        for (Mood mood : user.getMoodHistory()) {
            if (mood.getEmoticon().equals(view.getTag().toString())) {
                filterList.add(mood);
            }
        }
        filterAdapter = new CustomList(this,filterList,user,view.getTag().toString());
        moodHistory.setAdapter(filterAdapter);
    }

    /**
     * called when an activity that was called by this finishes
     * @param requestCode code that was sent when the activity was started
     * @param resultCode code that the activity returns
     * @param data any data the activity sends back
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        unSetFilterColors();
        findViewById(R.id.FilterScroll).setVisibility(View.GONE);
        recoverFilter();
    }

    /**
     * Clear the backgrounds of all filter buttons back to default
     */
    public void unSetFilterColors() {
        int padding = getResources().getDimensionPixelSize(R.dimen.padding_large);

        ImageButton greatFilter = findViewById(R.id.GreatFilterButton);
        greatFilter.setBackground(buttonBackground);
        greatFilter.setPadding(padding,padding,padding,padding);

        ImageButton goodFilter = findViewById(R.id.GoodFilterButton);
        goodFilter.setBackground(buttonBackground);
        goodFilter.setPadding(padding,padding,padding,padding);

        ImageButton neutralFilter = findViewById(R.id.NeutralFilterButton);
        neutralFilter.setBackground(buttonBackground);
        neutralFilter.setPadding(padding,padding,padding,padding);

        ImageButton badFilter = findViewById(R.id.BadFilterButton);
        badFilter.setBackground(buttonBackground);
        badFilter.setPadding(padding,padding,padding,padding);

        ImageButton worstFilter = findViewById(R.id.WorstFilterButton);
        worstFilter.setBackground(buttonBackground);
        worstFilter.setPadding(padding,padding,padding,padding);
    }

    /**
     * if the filter exists recreate it
     */
    public void recoverFilter() {
        if (filterAdapter!=null) {
            ImageButton filter = null;
            switch (filterAdapter.getEmote()) {
                case "great":
                    filter = findViewById(R.id.GreatFilterButton);
                    break;
                case "good":
                    filter = findViewById(R.id.GoodFilterButton);
                    break;
                case "neutral":
                    filter = findViewById(R.id.NeutralFilterButton);
                    break;
                case "bad":
                    filter = findViewById(R.id.BadFilterButton);
                    break;
                case "worst":
                    filter = findViewById(R.id.WorstFilterButton);
                    break;
            }
            findViewById(R.id.FilterScroll).setVisibility(View.VISIBLE);
            createFilter(filter);
        } else {
            clearFilterButton(findViewById(R.id.ClearFilterButton));
        }
    }
}
