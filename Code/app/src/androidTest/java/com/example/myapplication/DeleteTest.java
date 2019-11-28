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
import android.widget.ImageView;
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
        solo.clickOnButton("Add");
        solo.clickOnButton("confirm");

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        ListView moodHistory = (ListView)solo.getView(R.id.mood_history);
        ArrayAdapter<Mood> adapt = (ArrayAdapter<Mood>) moodHistory.getAdapter();
        while (adapt.getCount() > 0){
            ImageButton delete = moodHistory.getChildAt(0).findViewById(R.id.ListDelete);
            solo.clickOnView(delete);
            solo.sleep(500);
        }
        if (moodHistory.getCount() == 0){
            Log.d("myTag","Count: "+moodHistory.getCount());
        }else{
            assertTrue(1 == -1);
        }
    }

    /**
     * Delete element somewhere in the middle and then verify list
     *
     */
    @Test
    public void deletemid() {

        solo.assertCurrentActivity("Wrong Activity 1", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.clickOnButton("confirm");

        solo.clickOnButton("Add");
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager), solo.LEFT);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager), solo.LEFT);
        solo.clickOnButton("confirm");

        solo.clickOnButton("Add");
        solo.clickOnButton("confirm");

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        ListView moodHistory = (ListView)solo.getView(R.id.mood_history);

        ImageButton delete = moodHistory.getChildAt(1).findViewById(R.id.ListDelete);
        solo.clickOnView(delete);
        solo.sleep(500);

        if (moodHistory.getCount() == 2){
            Log.d("myTag","Count: "+moodHistory.getCount());
        }else{
            assertTrue(1 == -1);
        }
        solo.clickInList(1);
        ImageView emote = (ImageView) solo.getView(R.id.emoticonView);

        if (emote.getDrawable().getConstantState().equals(solo.getCurrentActivity().getResources().getDrawable(R.drawable.neutral).getConstantState())){
            Log.d("myTag","middle delete works");
        }else{
            assertTrue(1==11);
        }
        solo.clickOnButton("Return to Mood History");
        ArrayAdapter<Mood> adapt = (ArrayAdapter<Mood>) moodHistory.getAdapter();

        while (adapt.getCount() > 0){
            ImageButton delete2 = moodHistory.getChildAt(0).findViewById(R.id.ListDelete);
            solo.clickOnView(delete2);
            solo.sleep(500);
        }
    }
    /**
     * Delete element somewhere at the end and then verify list
     *
     */
    @Test
    public void deleteend() {

        solo.assertCurrentActivity("Wrong Activity 1", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.clickOnButton("confirm");

        solo.clickOnButton("Add");
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager), solo.LEFT);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager), solo.LEFT);
        solo.clickOnButton("confirm");

        solo.clickOnButton("Add");
        solo.clickOnButton("confirm");

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        ListView moodHistory = (ListView)solo.getView(R.id.mood_history);

        ImageButton delete = moodHistory.getChildAt(2).findViewById(R.id.ListDelete);
        solo.clickOnView(delete);
        solo.sleep(500);

        if (moodHistory.getCount() == 2){
            Log.d("myTag","Count: "+moodHistory.getCount());
        }else{
            assertTrue(1 == -1);
        }
        solo.clickInList(2);
        ImageView emote = (ImageView) solo.getView(R.id.emoticonView);

        if (emote.getDrawable().getConstantState().equals(solo.getCurrentActivity().getResources().getDrawable(R.drawable.worst).getConstantState())){
            Log.d("myTag","middle delete works");
        }else{
            assertTrue(1==11);
        }
        solo.clickOnButton("Return to Mood History");
        ArrayAdapter<Mood> adapt = (ArrayAdapter<Mood>) moodHistory.getAdapter();

        while (adapt.getCount() > 0){
            ImageButton delete2 = moodHistory.getChildAt(0).findViewById(R.id.ListDelete);
            solo.clickOnView(delete2);
            solo.sleep(500);
        }
    }




    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}