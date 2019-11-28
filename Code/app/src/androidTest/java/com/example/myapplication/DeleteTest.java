package com.example.myapplication;
import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Array;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class DeleteTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<Login> rule =
            new ActivityTestRule<>(Login.class, true, true);


    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        //initialize the ShowActivity environment before testing
        solo.assertCurrentActivity("Wrong Activity", Login.class);
        solo.enterText((EditText) solo.getView(R.id.userText), "test_user_delete");
        solo.clickOnButton("confirm");

    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Add a mood to the user history and check the mood inserted using assertTrue
     */
    @Test
    public void deleteList() {

        solo.assertCurrentActivity("Wrong Activity 1", MoodHistory.class);



        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        ListView moodHistory = (ListView)solo.getView(R.id.mood_history);
        ArrayAdapter<Mood> adapt = (ArrayAdapter<Mood>) moodHistory.getAdapter();
        while (adapt.getCount() > 0){
            ImageButton delete = moodHistory.getChildAt(0).findViewById(R.id.ListDelete);
            solo.clickOnView(delete);
        }
        Log.d("myTag","Count: "+moodHistory.getCount());
    }



    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}