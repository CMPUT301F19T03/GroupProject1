package com.example.myapplication;
import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import android.widget.EditText;
import android.widget.ListView;

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
        //initialize the ShowActivity environment before testing
        solo.assertCurrentActivity("Wrong Activity", Login.class);
        solo.enterText((EditText) solo.getView(R.id.userText), "test_user");
        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity", Add.class);
        solo.clickOnImageButton(2);
        solo.enterText((EditText) solo.getView(R.id.addReasonText), "test 1 to edit");
        solo.clickOnButton("confirm");

    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Add a mood to the user history and edits the mood and Reason
     */
    @Test
    public void EditList() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.clickInList(1);
        solo.clickOnButton("Edit");
        solo.assertCurrentActivity("Wrong Activity", Edit.class);
        solo.clickOnImageButton(1);
        solo.clearEditText((EditText) solo.getView(R.id.addReasonText));
        solo.enterText((EditText) solo.getView(R.id.addReasonText), "test 1 edited");
        solo.clickOnButton("confirm");
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);

    }


    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}