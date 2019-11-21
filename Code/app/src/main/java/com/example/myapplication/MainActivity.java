package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    public static String PACKAGE_NAME;
    public static Resources RESOURCES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        RESOURCES = getResources();

        Intent intent= new Intent(this,Login.class);
        startActivity(intent);

    }
}
