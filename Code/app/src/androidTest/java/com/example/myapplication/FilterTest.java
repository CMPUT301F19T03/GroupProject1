package com.example.myapplication;

import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class FilterTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<Login> rule =
            new ActivityTestRule<>(Login.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.assertCurrentActivity("Wrong Activity", Login.class);
        solo.enterText((EditText) solo.getView(R.id.userText), "test_user_filter");
        solo.clickOnButton("confirm");
        solo.sleep(5000);
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
    }

    /**
     * This test functions to check if all filter buttons are working
     * there is a user_test_filter set up already with multiple moods as to properly test functionality
     */
    @Test
    public void filter(){
        solo.clickOnButton("Filter");
        View greatView = solo.getView(R.id.GreatFilterButton);
        View goodView = solo.getView(R.id.GoodFilterButton);
        View neutView = solo.getView(R.id.NeutralFilterButton);
        View badView = solo.getView(R.id.BadFilterButton);
        View worstView = solo.getView(R.id.WorstFilterButton);
        solo.clickOnView(greatView);
        solo.clickOnView(goodView);
        solo.clickOnView(neutView);
        solo.clickOnView(badView);
        solo.clickOnView(worstView);
        solo.clickOnButton("Clear");
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}