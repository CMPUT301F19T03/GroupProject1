package com.example.myapplication;

import android.view.View;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
/* We are unable to test if adding a person to follow functions as it is not repeatable
    because we have no method to remove a person you are following so without that we cannot
    do any repeating
 */
public class CommunityTest {
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
        solo.sleep(5000);
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
    }

    @Test
    /**
     * This test serves to check that all buttons lead to the correct activities
     */
    public void CommunityButtons(){
        solo.clickOnButton("Community");
        solo.assertCurrentActivity("Wrong Activity",Community.class);
        solo.clickOnButton("Requests");
        solo.assertCurrentActivity("Wrong Activity", Requests.class);
        solo.clickOnButton("Return to Mood History");
        solo.assertCurrentActivity("Wrong Activity",Community.class);
        solo.clickOnButton("Follow");
        solo.assertCurrentActivity("Wrong Activity",FollowActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity",Community.class);
    }
    @Test
    /**
     * This tests the reject function of the app
     */
    public void RejectTest(){
        solo.clickOnButton("Community");
        solo.assertCurrentActivity("Wrong Activity",Community.class);
        solo.clickOnButton("Follow");
        solo.assertCurrentActivity("Wrong Activity",FollowActivity.class);
        solo.enterText((EditText) solo.getView(R.id.Username),"test_user_reject");
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity",Community.class);
        solo.clickOnButton("Return to Mood History");
        solo.clickOnButton("Log Out");
        solo.assertCurrentActivity("Wrong Activity", Login.class);
        solo.enterText((EditText) solo.getView(R.id.userText), "test_user_reject");
        solo.clickOnButton("confirm");
        solo.sleep(5000);
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.clickOnButton("Community");
        solo.assertCurrentActivity("Wrong Activity",Community.class);
        solo.clickOnButton("Requests");
        solo.assertCurrentActivity("Wrong Activity", Requests.class);
        solo.clickInList(0);
        solo.clickOnButton("Reject");
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }


}
