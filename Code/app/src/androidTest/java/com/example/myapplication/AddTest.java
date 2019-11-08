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
     * Add a mood to the user history and check the mood inserted using assertTrue
     * Just select mood (worst), date and time are today's
     */
    @Test
    public void addList() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity", Add.class);
        solo.clickOnImageButton(2);
        solo.enterText((EditText) solo.getView(R.id.addReasonText), "test 1");
        solo.clickOnButton("confirm");
    }

    /**
     * Add a mood to the user history and check the mood inserted using assertTrue
     *  With mood (great) and reasoning "test 1" , confirm and add another mood (neutral)
     *  with reasoning "test 3"
     */
    @Test
    public void addList2() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MoodHistory.class);
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity", Add.class);
        solo.clickOnImageButton(5);
        solo.enterText((EditText) solo.getView(R.id.addReasonText), "test 2");

        solo.clickOnButton("confirm");
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity", Add.class);
        solo.clickOnImageButton(3);
        solo.enterText((EditText) solo.getView(R.id.addReasonText), "test 3");
        solo.clickOnButton("confirm");
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}