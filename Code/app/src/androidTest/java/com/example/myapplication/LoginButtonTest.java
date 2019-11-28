package com.example.myapplication;
import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import android.widget.EditText;
import android.widget.ListView;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.opencensus.stats.View;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


@RunWith(AndroidJUnit4.class)
public class LoginButtonTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<Login> rule =
            new ActivityTestRule<>(Login.class, true, true);
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }
    //Test if empty username
    @Test
    public void emptyuser() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", Login.class);
        solo.enterText((EditText) solo.getView(R.id.userText),"");
        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity 2", Login.class);


    }
    @Test
    public void MoodViewTest(){

        // Purely for navigational purposes
        solo.assertCurrentActivity("Wrong Activity", Login.class);
        solo.enterText((EditText) solo.getView(R.id.userText),"Tim");
        solo.clickOnButton("confirm");
        solo.clearEditText((EditText) solo.getView(R.id.userText));
        solo.assertCurrentActivity("Wrong Activity 2", MoodHistory.class);
        assertTrue(solo.waitForText("View",1,2000));
        solo.clickOnButton("View");
        solo.assertCurrentActivity("Wrong Activity 3", MoodHistory.class);
        //solo.goBack(); <--Issue here with solo.goBack() is it needed?
        //solo.assertCurrentActivity("Wrong Activity 2B", Login.class);
    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}

