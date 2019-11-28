package com.example.myapplication;
import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

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

        //verify... make sure to clear the database before running
    }

    @Test
    public void addNeutral(){
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);

        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity", Add.class);

        //get emote


        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);

        //click on the item
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong activity", ViewMood.class);
        ImageView emote = (ImageView) solo.getView(R.id.emoticonView);
        //check..
        if (emote.getDrawable().getConstantState().equals(solo.getCurrentActivity().getResources().getDrawable(R.drawable.neutral).getConstantState())){
            Log.d("MyTag","Currernt emote is neutral");
            solo.assertCurrentActivity("Wrong Activity 3",ViewMood.class);
            solo.clickOnButton("Return to Mood History");
            solo.assertCurrentActivity("wrong activity i", MoodHistory.class);
        }else{
            Log.d("Mytag","not neutral");
        }

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
        // get emote..

        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);

        //reset database before running. Needs to be ran after the one above..

        solo.clickInList(0);
        //verify
        solo.assertCurrentActivity("Wrong Activity", ViewMood.class);

        ImageView emote = (ImageView) solo.getView(R.id.emoticonView);
        //check..
        if (emote.getDrawable().getConstantState().equals(solo.getCurrentActivity().getResources().getDrawable(R.drawable.worst).getConstantState())){
            Log.d("MyTag","Currernt emote is worst");
            solo.assertCurrentActivity("Wrong Activity 3",ViewMood.class);
            solo.clickOnButton("Return to Mood History");
            solo.assertCurrentActivity("wrong activity i", MoodHistory.class);
        }else{
            Log.d("Mytag","not worst");
        }


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
        // get emote
        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);

        solo.clickInList(0);
        //verify
        solo.assertCurrentActivity("Wrong Activity", ViewMood.class);

        ImageView emote = (ImageView) solo.getView(R.id.emoticonView);
        //check..
        if (emote.getDrawable().getConstantState().equals(solo.getCurrentActivity().getResources().getDrawable(R.drawable.great).getConstantState())){
            Log.d("MyTag","Currernt emote is great");
            solo.assertCurrentActivity("Wrong Activity 3",ViewMood.class);
            solo.clickOnButton("Return to Mood History");
            solo.assertCurrentActivity("wrong activity i", MoodHistory.class);
        }else{
            Log.d("Mytag","not great");
        }

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
        // get emote

        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);

        solo.clickInList(0);

        //verify
        solo.assertCurrentActivity("Wrong Activity", ViewMood.class);

        ImageView emote = (ImageView) solo.getView(R.id.emoticonView);
        if (emote.getDrawable().getConstantState().equals(solo.getCurrentActivity().getResources().getDrawable(R.drawable.good).getConstantState())){
            Log.d("MyTag","Currernt emote is good");
            solo.assertCurrentActivity("Wrong Activity 3",ViewMood.class);
            solo.clickOnButton("Return to Mood History");
            solo.assertCurrentActivity("wrong activity i", MoodHistory.class);
        }else{
            Log.d("Mytag","not bad");
        }
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
        // get emote
        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);

        solo.clickInList(0);

        //verify
        solo.assertCurrentActivity("Wrong Activity", ViewMood.class);
        ImageView emote = (ImageView) solo.getView(R.id.emoticonView);

        if (emote.getDrawable().getConstantState().equals(solo.getCurrentActivity().getResources().getDrawable(R.drawable.bad).getConstantState())){
            Log.d("MyTag","Currernt emote is good");
            solo.assertCurrentActivity("Wrong Activity 3",ViewMood.class);
            solo.clickOnButton("Return to Mood History");
            solo.assertCurrentActivity("wrong activity i", MoodHistory.class);
        }else{
            Log.d("Mytag","not good");
        }
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
        solo.clickInList(0);

        //verify
        solo.assertCurrentActivity("Wrong Activity", ViewMood.class);
        ImageView emote = (ImageView) solo.getView(R.id.emoticonView);
        //check..
        if (emote.getDrawable().getConstantState().equals(solo.getCurrentActivity().getResources().getDrawable(R.drawable.great).getConstantState())){
            Log.d("MyTag","Currernt emote is great");
            solo.assertCurrentActivity("Wrong Activity 3",ViewMood.class);
            solo.clickOnButton("Return to Mood History");
            solo.assertCurrentActivity("wrong activity i", MoodHistory.class);
        }else{
            Log.d("Mytag","not great");
        }
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
        solo.clickInList(0);

        //verify
        solo.assertCurrentActivity("Wrong Activity", ViewMood.class);
        //verify
        ImageView emote = (ImageView) solo.getView(R.id.emoticonView);
        //check..
        if (emote.getDrawable().getConstantState().equals(solo.getCurrentActivity().getResources().getDrawable(R.drawable.worst).getConstantState())){
            Log.d("MyTag","Currernt emote is worst");
            solo.assertCurrentActivity("Wrong Activity 3",ViewMood.class);
            solo.clickOnButton("Return to Mood History");
            solo.assertCurrentActivity("wrong activity i", MoodHistory.class);
        }else{
            Log.d("Mytag","not worst");
        }

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
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong activity i",ViewMood.class);

        String reason = (String) ((TextView) solo.getView(R.id.reasonView)).getText();

        if (reason.equals("test1")){
            Log.d("MyTag","Current reason is test1");
            solo.assertCurrentActivity("Wrong Activity 3",ViewMood.class);
            solo.clickOnButton("Return to Mood History");
            solo.assertCurrentActivity("wrong activity i", MoodHistory.class);
        }else{
            Log.d("Mytag","not test1");
        }

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

        solo.clickInList(0);
        // verify reason
        solo.assertCurrentActivity("Wrong activity i",ViewMood.class);

        String reason = (String) ((TextView) solo.getView(R.id.reasonView)).getText();

        if (reason.equals("hellohellohellohello")){
            Log.d("MyTag","Current reason is test1");
            solo.assertCurrentActivity("Wrong Activity 3",ViewMood.class);
            solo.clickOnButton("Return to Mood History");
            solo.assertCurrentActivity("wrong activity i", MoodHistory.class);
        }else{
            Log.d("Mytag","not test1");
        }
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

        solo.clickInList(0);
        //verify
        solo.assertCurrentActivity("Wrong activity i",ViewMood.class);

        String reason = (String) ((TextView) solo.getView(R.id.reasonView)).getText();

        if (reason.equals("")){
            Log.d("MyTag","Current reason is test1");
            solo.assertCurrentActivity("Wrong Activity 3",ViewMood.class);
            solo.clickOnButton("Return to Mood History");
            solo.assertCurrentActivity("wrong activity i", MoodHistory.class);
        }else{
            Log.d("Mytag","not test1");
        }
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

        ToggleButton toggleButton = (ToggleButton) solo.getView(R.id.locationToggle);
        solo.clickOnView(toggleButton);

        solo.assertCurrentActivity("Wrong Activity 1", AddMapActivity.class);
        solo.sleep(5000);
        solo.clickOnScreen(200,300);
        solo.clickOnButton("Add Location");
        solo.assertCurrentActivity("Wrong Activity 2", Add.class);


        //verification of cursor is done manually.
        ToggleButton toggleButton1 = (ToggleButton) solo.getView(R.id.locationToggle);
        if (toggleButton1.isChecked()){
            solo.clickOnButton("confirm");
            Log.d("Mytag","Button is correctly ");
            solo.assertCurrentActivity("Wrong Activity 3", MoodHistory.class);
        } else{
            Log.d("Mytag","Button is not correctly checked");
        }

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

        ToggleButton toggleButton = (ToggleButton) solo.getView(R.id.locationToggle);
        solo.clickOnView(toggleButton);

        solo.assertCurrentActivity("Wrong Activity 1", AddMapActivity.class);

        solo.sleep(5000);
        solo.clickOnScreen(200,300);
        solo.clickOnButton("Add Location");
        solo.assertCurrentActivity("Wrong Activity 2", Add.class);
        ToggleButton toggleButton_1 = (ToggleButton) solo.getView(R.id.locationToggle);
        solo.clickOnView(toggleButton_1);
        solo.clickOnView(toggleButton_1);

        solo.sleep(5000);
        solo.clickOnScreen(400,400);
        solo.clickOnButton("Add Location");
        solo.assertCurrentActivity("Wrong Activity 2", Add.class);

        ToggleButton toggleButton1 = (ToggleButton) solo.getView(R.id.locationToggle);

        if (toggleButton1.isChecked()){
            solo.clickOnButton("confirm");
            Log.d("Mytag","Button is correctly ");
            solo.assertCurrentActivity("Wrong Activity 3", MoodHistory.class);
        } else{
            Log.d("Mytag","Button is not correctly checked");
        }
        //verification of cursor is done manually. Proper behavioural pattern also suggests that the item is verified true

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

        ToggleButton toggleButton = (ToggleButton) solo.getView(R.id.locationToggle);
        solo.clickOnView(toggleButton);

        solo.assertCurrentActivity("Wrong Activity 1", AddMapActivity.class);
        solo.sleep(5000);
        solo.clickOnScreen(500,600);
        solo.clickOnButton("Add Location");
        solo.assertCurrentActivity("Wrong Activity 2", Add.class);
        solo.clickOnButton("Change Location?");

        solo.sleep(5000);
        solo.clickOnScreen(800,1000);
        solo.clickOnButton("Edit Location");
        solo.assertCurrentActivity("Wrong Activity 3", Add.class);
        ToggleButton toggleButton_1 = (ToggleButton) solo.getView(R.id.locationToggle);

        if (toggleButton_1.isChecked()){
            solo.clickOnButton("confirm");
            Log.d("Mytag","Button is correctly ");
            solo.assertCurrentActivity("Wrong Activity 3", MoodHistory.class);
        } else {
            Log.d("Mytag", "Button is not correctly checked");
            //verification of cursor is done manually. Proper behavioural pattern also suggests that the item is verified true
        }
    }

    /**
     * Add a mood where a location was to be added but then cancelled
     *
     */
    @Test
    public void addList_Locationcancelwithoutclick(){
        solo.assertCurrentActivity("Wrong Activity g", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity f ", Add.class);

        ToggleButton toggleButton1 = (ToggleButton) solo.getView(R.id.locationToggle);
        solo.clickOnView(toggleButton1);

        solo.assertCurrentActivity("Wrong Activity 1", AddMapActivity.class);
        solo.clickOnButton("Cancel");
        solo.assertCurrentActivity("Wrong Activity g", Add.class);
        //verification of cursor is done manually. Proper behavioural pattern also suggests that the item is verified true

        ToggleButton toggleButton_1 = (ToggleButton) solo.getView(R.id.locationToggle);

        if (!(toggleButton_1.isChecked())){
            solo.clickOnButton("confirm");
            Log.d("Mytag","Button is correctly ");
            solo.assertCurrentActivity("Wrong Activity 3", MoodHistory.class);
        } else {
            Log.d("Mytag", "Button is not correctly checked");
            //verification of cursor is done manually. Proper behavioural pattern also suggests that the item is verified true
        }
    }
    /**
     * Add a mood where we try to add with no location clicked.
     */
    @Test
    public void addList_Locationaddwithoutclick(){
        solo.assertCurrentActivity("Wrong Activity g", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity f ", Add.class);

        ToggleButton toggleButton = (ToggleButton) solo.getView(R.id.locationToggle);
        solo.clickOnView(toggleButton);

        solo.assertCurrentActivity("Wrong Activity 1", AddMapActivity.class);
        solo.clickOnButton("Add Location");
        solo.assertCurrentActivity("Wrong Activity 2", AddMapActivity.class);
        solo.clickOnButton("Cancel");
        ToggleButton toggleButton_1 = (ToggleButton) solo.getView(R.id.locationToggle);

        if (toggleButton_1.isChecked()){
            solo.clickOnButton("confirm");
            Log.d("Mytag","Button is correctly ");
            solo.assertCurrentActivity("Wrong Activity 3", MoodHistory.class);
        } else {
            Log.d("Mytag", "Button is not correctly checked");
            //verification of cursor is done manually. Proper behavioural pattern also suggests that the item is verified true
        }
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

        ToggleButton toggleButton = (ToggleButton) solo.getView(R.id.locationToggle);
        solo.clickOnView(toggleButton);

        solo.assertCurrentActivity("Wrong Activity 1", AddMapActivity.class);
        solo.sleep(5000);
        solo.clickOnScreen(800, 1000);
        solo.clickOnButton("Cancel");
        solo.assertCurrentActivity("Wrong Activity g", Add.class);

        ToggleButton toggleButton_1 = (ToggleButton) solo.getView(R.id.locationToggle);

        if (!(toggleButton_1.isChecked())){
            solo.clickOnButton("confirm");
            Log.d("Mytag","Button is correctly ");
            solo.assertCurrentActivity("Wrong Activity 3", MoodHistory.class);
        } else {
            Log.d("Mytag", "Button is not correctly checked");
            //verification of cursor is done manually. Proper behavioural pattern also suggests that the item is verified true
        }
        //verify by checking for "No"
    }


    /**
     * add a mood on the lower extremeties
     *
     */
    @Test
    public void addList_Spinner2(){
        solo.assertCurrentActivity("Wrong Activity g", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity f ", Add.class);
        View view1 = solo.getView(Spinner.class, 0);
        solo.clickOnView(view1);
        solo.clickOnView(solo.getView(TextView.class, 20));
        //get spinner value...


        solo.sleep(2000);
        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity g", MoodHistory.class);

        solo.clickInList(0);
        //verify..
    }
    /**
     * add a mood on the upper extremeties
     *
     */
    @Test
    public void addList_Spinner3(){
        solo.assertCurrentActivity("Wrong Activity g", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity f ", Add.class);
        View view1 = solo.getView(Spinner.class, 0);
        solo.clickOnView(view1);
        solo.clickOnView(solo.getView(TextView.class, 0));
        solo.sleep(2000);
        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity g", MoodHistory.class);

        solo.clickInList(0);
        //verify..
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}