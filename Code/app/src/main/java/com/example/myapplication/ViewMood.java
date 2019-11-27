package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * This class is responsible for the View Mood activity
 */

public class ViewMood extends AppCompatActivity {
    Mood currentMood;
    TextView date;
    TextView time;
    TextView reason;
    TextView social;
    ImageView emote;
    ImageView picture;

    StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mood);
        storageRef = FirebaseStorage.getInstance().getReference();
        Intent intent = getIntent();
        currentMood = (Mood) intent.getSerializableExtra("Mood");

        date = findViewById(R.id.dateView);
        date.setText(currentMood.getStringDate());
        time = findViewById(R.id.timeView);
        time.setText(currentMood.getStringTime());
        reason = findViewById(R.id.reasonView);
        reason.setText(currentMood.getReason());
        social = findViewById(R.id.socialView);
        social.setText(currentMood.getSocialSituation());
        emote = findViewById(R.id.emoticonView);
        int id = getResources().getIdentifier(currentMood.getEmoticon(),"drawable",getPackageName());
        emote.setImageResource(id);
        picture = findViewById(R.id.imageView2);
        if (currentMood.getPicture()!=null) {
            StorageReference imRef = storageRef.child(currentMood.getPicture());
            imRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    picture.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("myTag","Failed to getBytes: "+e);
                    Log.d("myTag","Path: "+currentMood.getPicture());
                }
            });
        }

    }
    /**
     * This button returns the user to the Moodhistory view
     * @param view is the context for this view
     */
    public void ReturnButton(View view){
        finish();
    }

    public void LocationButton(View view) {
        if (currentMood.getLatitude()!=null) {
            Intent intent = new Intent(this, ViewMapActivity.class);
            intent.putExtra("Lat", currentMood.getLatitude());
            intent.putExtra("Long", currentMood.getLongitude());
            startActivity(intent);
        } else {
            Toast.makeText(this,"No Location for this mood",Toast.LENGTH_SHORT).show();
        }
    }
}
