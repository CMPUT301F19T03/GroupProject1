package com.example.myapplication;
import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.Add;
import com.example.myapplication.Login;
import com.example.myapplication.MoodHistory;
import com.example.myapplication.R;
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
public class EditTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<Login> rule =
            new ActivityTestRule<>(Login.class, true, true);


    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.assertCurrentActivity("Wrong Activity", Login.class);
        solo.enterText((EditText) solo.getView(R.id.userText), "test_user_tim");
        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity", Add.class);
        solo.clickOnButton("confirm");
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    // How I got the button click to work: https://stackoverflow.com/questions/21479646/unable-to-click-on-imageview-in-robotium/21480263
    public void changeMood() {
        solo.assertCurrentActivity("Wrong Activity",MoodHistory.class);
        View view = solo.getView("ListEdit");
        solo.clickOnView(view);
        solo.assertCurrentActivity("Wrong Activity", Edit.class);
        solo.scrollToSide(solo.LEFT);
        solo.clickOnButton("confirm");
        solo.clickInList(0);
        ImageView emote=(ImageView) solo.getView(R.id.emoticonView);
        if (emote.getDrawable().getConstantState().equals(solo.getCurrentActivity().getResources().getDrawable(R.drawable.bad).getConstantState())){
            Log.d("myTag", "Current emote is bad");
        } else {
            Log.d("MyTag","not bad");
        }
        View button = solo.getView(R.id.returnButton);
        solo.clickOnView(button);
    }
    @Test
    public void changeText(){
        solo.assertCurrentActivity("Wrong Activity",MoodHistory.class);
        View view = solo.getView("ListEdit");
        solo.clickOnView(view);
        solo.assertCurrentActivity("Wrong Activity", Edit.class);
        solo.enterText((EditText)solo.getView(R.id.addReasonText),"Test Text");
        solo.clickOnButton("confirm");
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity",ViewMood.class);
        TextView textView= (TextView)solo.getView(R.id.reasonView);
        assertTrue(textView.getText().toString().equals("Test Text"));
        View button = solo.getView(R.id.returnButton);
        solo.clickOnView(button);
    }

    @Test
    public void changeTime(){
        solo.assertCurrentActivity("Wrong Activity",MoodHistory.class);
        View view = solo.getView("ListEdit");
        solo.clickOnView(view);
        solo.assertCurrentActivity("Wrong Activity", Edit.class);
        solo.clickOnButton("Edit time?");
        solo.setTimePicker(0,23,23);
        solo.clickOnButton("OK");
        solo.clickOnButton("confirm");
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity",ViewMood.class);
        TextView textView= (TextView)solo.getView(R.id.timeView);
        assertTrue(textView.getText().toString().equals("23:23"));
        View button = solo.getView(R.id.returnButton);
        solo.clickOnView(button);
    }
    @Test
    public void changeDate(){
        solo.assertCurrentActivity("Wrong Activity",MoodHistory.class);
        View view = solo.getView("ListEdit");
        solo.clickOnView(view);
        solo.assertCurrentActivity("Wrong Activity", Edit.class);
        solo.clickOnButton("Edit date?");
        solo.setDatePicker(0,2019,10,23);
        solo.clickOnButton("OK");
        solo.clickOnButton("confirm");
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity",ViewMood.class);
        TextView textView= (TextView)solo.getView(R.id.dateView);
        //Calender functions strangely so this is the "correct" date
        assertTrue(textView.getText().toString().equals("2019-11-23"));
        View button = solo.getView(R.id.returnButton);
        solo.clickOnView(button);
    }
    @Test
    public void changeLocation(){
        solo.assertCurrentActivity("Wrong Activity",MoodHistory.class);
        View view = solo.getView("ListEdit");
        solo.clickOnView(view);
        solo.assertCurrentActivity("Wrong Activity", Edit.class);
        View location = solo.getView(R.id.editLocationToggle);
        solo.clickOnView(location);
        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Activity",AddMapActivity.class);
        solo.clickOnScreen(500,600);
        solo.clickOnButton("Add Location");
        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Activity",Edit.class);
        solo.clickOnButton("Edit Location?");
        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Activity",EditMapActivity.class);
        solo.clickOnScreen(500,600);
        solo.clickOnButton("Edit Location");
        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Activity",Edit.class);
        solo.clickOnButton("confirm");
    }

    @Test
    public void editSocialSituation(){
        solo.assertCurrentActivity("Wrong Activity",MoodHistory.class);
        View view = solo.getView("ListEdit");
        solo.clickOnView(view);
        solo.assertCurrentActivity("Wrong Activity", Edit.class);
        View view1= solo.getView(Spinner.class,0);
        solo.clickOnView(view1);
        solo.clickOnView(solo.getView(TextView.class,20));
        solo.clickOnButton("confirm");
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity",ViewMood.class);
        TextView textView= (TextView)solo.getView(R.id.socialView);
        assertTrue(textView.getText().toString().equals("WITH A CROWD"));
        View button = solo.getView(R.id.returnButton);
        solo.clickOnView(button);

    }

    @After
    public void tearDown() throws Exception{
        View view = solo.getView("ListDelete");
        solo.clickOnView(view);
        solo.finishOpenedActivities();
    }

}
