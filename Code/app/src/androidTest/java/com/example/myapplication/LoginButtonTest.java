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
public class LoginButtonTest {
    private Solo solo;
    ActivityTestRule<Login> rule =
            new ActivityTestRule<>(Login.class, true, true);
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }
    @Test
    public void MoodViewTest(){
        solo.assertCurrentActivity("Wrong Activity", Login.class);
        solo.enterText((EditText) solo.getView(R.id.userText),"Tim");
        solo.clickOnButton("confirm");
        solo.clearEditText((EditText) solo.getView(R.id.userText));
        assertTrue(solo.waitForText("View", 1, 2000));
        solo.clickOnButton("View");
        assertTrue(solo.waitForText("Test1",1,2000));
    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}

