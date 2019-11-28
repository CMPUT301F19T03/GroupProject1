package com.example.myapplication;
import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class AddTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<Login> rule =
            new ActivityTestRule<>(Login.class, true, true);


    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        //initialize the ShowActivity environment before testing
        solo.assertCurrentActivity("Wrong Activity", Login.class);
        solo.enterText((EditText) solo.getView(R.id.userText), "test_user");
        solo.clickOnButton("confirm");

    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * press return as soon as add is pressed
     */
    @Test
    public void addNone(){
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity", Add.class);
        solo.clickOnButton("Return to Mood History");
        solo.assertCurrentActivity("Wrong activity 2", MoodHistory.class);
    }

    @Test
    public void addNeutral(){
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity", Add.class);
        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.getView(R.id.mood_history);

    }
    /**
     * Add a mood to the user history and check the mood inserted using assertTrue
     * Just select mood (worst), date and time are today's
     */
    @Test
    public void addList_worst() {
        // Asserts that the current activity is the MainActivity. Otherwise, show �Wrong Activity�
        //add mood that is worst, and press confirm.
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity", Add.class);
        solo.getView(R.id.AddviewPager);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager),solo.LEFT);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager),solo.LEFT);
        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);


        //verify
        solo.assertCurrentActivity("Wrong Activity", ViewMood.class);
        solo.getView(R.id.mood_history);
        solo.clickInList(0);

    }

    /**
     * Add a mood (best)
     */
    @Test
    public void addList_great(){
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity", Add.class);
        solo.getView(R.id.AddviewPager);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager),solo.RIGHT);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager),solo.RIGHT);
        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.getView(R.id.mood_history);
        solo.clickInList(0);

        //verify
        solo.assertCurrentActivity("Wrong Activity", ViewMood.class);

    }

    /**
     * add a mood, but toggle e than once to end on bad
     */
    @Test
    public void addList_alternate(){
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity", Add.class);
        solo.getView(R.id.AddviewPager);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager),solo.RIGHT);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager),solo.LEFT);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager),solo.RIGHT);
        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.getView(R.id.mood_history);
        solo.clickInList(0);

        //verify
        solo.assertCurrentActivity("Wrong Activity", ViewMood.class);
    }

    /**
     * add a mood, but toggle more than once to end on good
     */
    @Test
    public void addList_alternate_2(){
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity", Add.class);
        solo.getView(R.id.AddviewPager);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager),solo.LEFT);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager),solo.RIGHT);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager),solo.LEFT);
        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.getView(R.id.mood_history);
        solo.clickInList(0);

        //verify
        solo.assertCurrentActivity("Wrong Activity", ViewMood.class);
    }

    /**
     * Check if upper limits keep great as given mood
     */

    @Test
    public void addList_KeepGreat() {
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity", Add.class);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager),solo.RIGHT);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager),solo.RIGHT);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager),solo.RIGHT);
        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.getView(R.id.mood_history);
        //solo.clickInList(0);

        //verify
        //solo.assertCurrentActivity("Wrong Activity", ViewMood.class);
    }

    /**
     * Check if lower limits keep the worst as mood
     */
    @Test
    public void addList_KeepWorst() {
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity", Add.class);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager), solo.LEFT);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager), solo.LEFT);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager), solo.LEFT);
        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.getView(R.id.mood_history);

        //verify

    }
    /**
     * Add a mood to the user history and check the mood inserted using assertTrue
     *  With mood (great) and reasoning "test 1" , confirm and add another mood (neutral)
     *  with reasoning "test 3"
     */
    @Test
    public void addList_reason(){
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity", Add.class);


        TextView tv = (TextView)solo.getView(R.id.reasonView);
        solo.clickOnView(tv);
        solo.enterText(0,"test1");

        solo.scrollUp();
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager),solo.RIGHT);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager),solo.RIGHT);

        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);

        //verify

        // new case
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity", Add.class);


        TextView tv_2 = (TextView)solo.getView(R.id.reasonView);
        solo.clickOnView(tv_2);
        solo.enterText(0,"test2");


        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        //verify by viewing mood
    }

    /**
     * try adding in reason with too many characters (>20)
     */
    @Test
    public void addList_toolongreason(){
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity", Add.class);


        TextView tv = (TextView)solo.getView(R.id.reasonView);
        solo.clickOnView(tv);
        solo.enterText(0,"hellohellohellohelloh");

        solo.scrollUp();
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager),solo.RIGHT);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager),solo.RIGHT);

        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);

        // verify

    }

    /**
     * Click on reason, don't fill it, and then go back.
     */
    @Test
    public void addList_noreason(){
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity", Add.class);


        TextView tv = (TextView)solo.getView(R.id.reasonView);
        solo.clickOnView(tv);
        solo.enterText(0,"");
        solo.scrollUp();
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager),solo.LEFT);
        solo.scrollViewToSide(solo.getView(R.id.AddviewPager),solo.LEFT);

        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);

    }

    /**
     * Add a mood with a location. Standard adding location test.
     *
     */
    @Test
    public void addList_Location(){
        solo.assertCurrentActivity("Wrong Activity g", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity f ", Add.class);

        solo.clickOnToggleButton("No");

        solo.assertCurrentActivity("Wrong Activity 1", AddMapActivity.class);
        solo.clickOnScreen(200,300);
        solo.clickOnButton("Add Location");
        solo.assertCurrentActivity("Wrong Activity 2", Add.class);
        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity 3", MoodHistory.class);
    }

    /**
     * Add a mood with a location, then toggle no, and put a new change that location
     *
     */
    @Test
    public void addList_LocationNo(){
        solo.assertCurrentActivity("Wrong Activity g", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity f ", Add.class);

        solo.clickOnToggleButton("No");

        solo.assertCurrentActivity("Wrong Activity 1", AddMapActivity.class);
        solo.clickOnScreen(200,300);
        solo.clickOnButton("Add Location");
        solo.assertCurrentActivity("Wrong Activity 2", Add.class);
        solo.clickOnButton("Yes");
        solo.clickOnButton("No");
        solo.clickOnScreen(400,400);
        solo.clickOnButton("Add Location");
        solo.assertCurrentActivity("Wrong Activity 2", Add.class);
        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity 3", MoodHistory.class);
    }

    /**
     * Add a mood with a location, then add, and press change
     *
     */
    @Test
    public void addList_Locationchange(){
        solo.assertCurrentActivity("Wrong Activity g", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity f ", Add.class);

        solo.clickOnToggleButton("No");

        solo.assertCurrentActivity("Wrong Activity 1", AddMapActivity.class);
        solo.clickOnScreen(500,600);
        solo.clickOnButton("Add Location");
        solo.assertCurrentActivity("Wrong Activity 2", Add.class);
        solo.clickOnButton("Change Location?");

        solo.clickOnScreen(800,1000);
        solo.clickOnButton("Edit Location");
        solo.assertCurrentActivity("Wrong Activity 3", Add.class);

        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong activity 4",MoodHistory.class);
    }

    /**
     * Add a mood where a location was to be added but then cancelled
     *
     */
    @Test
    public void addList_Locationcancelwithoutclick() {
        solo.assertCurrentActivity("Wrong Activity g", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity f ", Add.class);
        solo.clickOnToggleButton("No");
        solo.assertCurrentActivity("Wrong Activity 1", AddMapActivity.class);
        solo.clickOnButton("Cancel");
        solo.assertCurrentActivity("Wrong Activity g", Add.class);
        solo.clickOnButton("confirm");

        //verify by checking for "No"
    }

    /**
     * Add a mood where a location was to be added but then cancelled (with a click on a location. should inductively take care of without a click..
     *
     */
    @Test
    public void addList_Locationcancelwithclick() {
        solo.assertCurrentActivity("Wrong Activity g", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity f ", Add.class);
        solo.clickOnToggleButton("No");
        solo.assertCurrentActivity("Wrong Activity 1", AddMapActivity.class);
        solo.clickOnScreen(800, 1000);
        solo.clickOnButton("Cancel");
        solo.assertCurrentActivity("Wrong Activity g", Add.class);
        solo.clickOnButton("confirm");

        //verify by checking for "No"
    }

    /**
     * add a mood with certain spinner
     *
     */


    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}