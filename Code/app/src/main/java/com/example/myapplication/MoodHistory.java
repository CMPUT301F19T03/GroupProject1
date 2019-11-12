package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is dedicated to the activity_mood_history view and will handle that view's needs
 */
public class MoodHistory extends AppCompatActivity {
    String TAG = "myTag";
    ListView moodHistory;
    ArrayAdapter<Mood> moodArrayAdapter;
    ArrayList<Mood> moodArrayList;
    Participant user;
    FirebaseFirestore db;
    CollectionReference users;
    ArrayList<Mood> greatMoods;
    ArrayList<Mood> goodMoods;
    ArrayList<Mood> neutralMoods;
    ArrayList<Mood> badMoods;
    ArrayList<Mood> worstMoods;
    ArrayAdapter<Mood> greatAdapter;
    ArrayAdapter<Mood> goodAdapter;
    ArrayAdapter<Mood> neutralAdapter;
    ArrayAdapter<Mood> badAdapter;
    ArrayAdapter<Mood> worstAdapter;
    int selected = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);
        db = FirebaseFirestore.getInstance();
        users = db.collection("Users");
        users.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d(TAG, "Something changed");
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("Username")==user.getName()) {
                        user = queryDocumentSnapshots.getDocuments().get(0).get("Participant", Participant.class);
                    }
                }
            }
        });
        Log.d(TAG,"bad: "+R.drawable.bad);
        Log.d(TAG,"good: "+R.drawable.good);
        Log.d(TAG,"great: "+R.drawable.great);
        Log.d(TAG,"neutral: "+R.drawable.neutral);
        Log.d(TAG,"worst: "+R.drawable.worst);
        Intent intent = getIntent();
        user = (Participant) intent.getSerializableExtra("User");
        moodArrayList = user.getMoodHistory();
        moodArrayAdapter = new CustomList(this,moodArrayList);

        moodHistory = findViewById(R.id.mood_history);
        moodHistory.setAdapter(moodArrayAdapter);
        moodHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Mood temp = (Mood)moodHistory.getItemAtPosition(pos);
                selected = moodArrayList.indexOf((temp));
            }
        });

    }

    /**
     * this button sends the user to the add activity
     * @param view is the view context for this class
     */
    public void addButton(View view) {
        Intent intent = new Intent(this, Add.class);
        intent.putExtra("moodList",moodArrayList);
        startActivityForResult(intent,1);
    }
    /**
     * this button sends the user to the edit activity
     * @param view is the view context for this class
     */
    public void editButton(View view) {
        if (selected!=-1) {
            Intent intent = new Intent(this, Edit.class);
            intent.putExtra("moodList", moodArrayList);
            intent.putExtra("pos", selected);
            startActivityForResult(intent,1);
        }
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
     * this button sends the user to the ViewMood activity
     * @param view is the view context for this class
     */
    public void viewButton(View view) {
        if (selected!=-1) {
            Intent intent = new Intent(this, ViewMood.class);
            intent.putExtra("Mood",moodArrayList.get(selected));
            selected = -1;
            startActivity(intent);
        }
    }
    /**
     * this button sends the user to the Followed mood history activity
     * @param view is the view context for this class
     */
    public void viewFollowButton(View view) {
        Intent intent = new Intent(this, FollowHistory.class);
        startActivity(intent);
    }
    /**
     * this button sends the user to the requests activity
     * @param view is the view context for this class
     */
    public void requestButton(View view) {
        Intent intent = new Intent(this, Requests.class);
        startActivity(intent);
    }

    /**
     * this button sets the Visibility of the button filters to visible, and allows the user to filter the search based on the given mood state...
     * @param view is the view context for this class.
     */
    public void filterButton(View view){
        Button allButton = findViewById(R.id.allButton);
        Button greatButton = findViewById(R.id.greatButton);
        Button goodButton = findViewById(R.id.goodButton);
        Button neutralButton = findViewById(R.id.neutralButton);
        Button badButton = findViewById(R.id.badButton);
        Button worstButton = findViewById(R.id.worstButton);

        allButton.setVisibility(View.VISIBLE);
        greatButton.setVisibility(View.VISIBLE);
        goodButton.setVisibility(View.VISIBLE);
        neutralButton.setVisibility(View.VISIBLE);
        badButton.setVisibility(View.VISIBLE);
        worstButton.setVisibility(View.VISIBLE);
    }

    public void deleteButton(View view) {
        if (selected!=-1) {
            moodArrayList.remove(selected);
            moodArrayAdapter.notifyDataSetChanged();
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
                                Log.d(TAG,"Deleting from user: "+updated.getName());
                                users.document(queryDocumentSnapshots.getDocuments().get(0).getId())
                                        .update(userUpdate)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Log.d(TAG,"deleted successfully");
                                            }
                                        });

                            }
                        }
                    });
            selected=-1;
        }
    }


    public void allButton(View view){
        moodArrayAdapter = new CustomList(this,moodArrayList);
        moodHistory.setAdapter(moodArrayAdapter);
    }
    public void greatButton(View view){
        filteredMoods(greatMoods,greatAdapter,"great");
    }
    public void goodButton(View view){
        filteredMoods(goodMoods,goodAdapter,"good");
    }
    public void neutralButton(View view){
        filteredMoods(neutralMoods,neutralAdapter,"neutral");
    }
    public void badButton(View view){
        filteredMoods(badMoods,badAdapter,"bad");
    }
    public void worstButton(View view) {
        filteredMoods(worstMoods,worstAdapter,"worst");
    }

    public void filteredMoods(ArrayList<Mood> filteredMoods, ArrayAdapter<Mood> filterAdapter,String state){
        filteredMoods = new ArrayList<>();
        for (int i = 0; i < moodArrayList.size();i++){

            if (moodArrayList.get(i).getEmoticon().equals(state)){
                filteredMoods.add(moodArrayList.get(i));
            }
        }
        moodHistory = findViewById(R.id.mood_history);
        filterAdapter = new CustomList(this,filteredMoods);
        moodHistory.setAdapter(filterAdapter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1) {
            if (resultCode==RESULT_OK) {
                Log.d(TAG,"Return from add");
                moodArrayList = (ArrayList<Mood>) data.getSerializableExtra("Addmood");
                moodArrayAdapter = new CustomList(this,moodArrayList);
                moodHistory.setAdapter(moodArrayAdapter);
                user.setMoodHistory(moodArrayList);
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
                                    Log.d(TAG,"Updating user: "+updated.getName());
                                    users.document(queryDocumentSnapshots.getDocuments().get(0).getId())
                                            .update(userUpdate)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    Log.d(TAG,"Updated user");
                                                }
                                            });

                                }
                            }
                        });

            }
        }
    }
}
