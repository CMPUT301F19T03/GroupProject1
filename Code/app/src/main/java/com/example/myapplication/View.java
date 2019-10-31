package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


public class View extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

    }
    public void ReturnButton(android.view.View view){
        Intent intent = new Intent(this,MoodHistory.class);
        startActivity(intent);
    }
}
