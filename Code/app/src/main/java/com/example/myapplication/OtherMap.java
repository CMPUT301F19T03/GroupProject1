package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
/**
 * This class is responsible for the Othermap activity
 */

public class OtherMap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_map);
    }
    /**
     * This button returns the user to the Moodhistory view
     * @param view is the context for this view
     */
    public void ReturnButton(View view) {
        setResult(RESULT_OK);
        finish();
    }

    /**
     * This button brings the user to their own map where they can see their mood history locations
     * @param view is the view context for this class
     */
    public void UserButton(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
