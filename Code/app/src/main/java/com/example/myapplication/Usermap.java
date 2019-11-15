package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
/**
 * This class is responsible for the Usermap activity
 */
public class Usermap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermap);
    }
    /**
     * This button returns the user to the Moodhistory view
     * @param view is the context for this view
     */
    public void ReturnButton(View view) {
        finish();
    }

    /**
     * This button sends the user to the other map view which displays their followers
     * @param view is the context for this view
     */
    public void OthersButton(View view) {
        Intent intent = new Intent(this, OtherMap.class);
        startActivityForResult(intent,1);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            finish();
        }
    }
}
