package com.example.myapplication;
import android.media.Image;

import androidx.annotation.Nullable;

import com.google.protobuf.NullValue;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;
public class LoginInfoTest1 {
    private Participant mockUser() {
        Participant participant = new Participant("mockUser");
        participant.addMood(mockMood());
        return participant;
    }
    private Mood mockMood(){
        Date date = new Date(1998,13,1);
        return new Mood(date,"No reason","Out with Friends","great");
    }

    /**
     * testAdd is a simple backend test to see if one can add objects of varying types into the mockUser.
     */
    @Test
    public void testAdd() {
        Participant participant = mockUser();
        assertEquals(1,participant.getMoodHistory().size());

        Date date = new Date(1925,2,10);
        Mood mood = new Mood(date,"Breakup","At home","worst");
        participant.addMood(mood);
        assertEquals(2,participant.getMoodHistory().size());
        assertFalse("Wrong Index",participant.getMoodHistory().get(1).getReason() == "No Reason");
        assertTrue("Right Index",participant.getMoodHistory().get(1).getReason() == "Breakup");
        assertTrue(participant.getMoodHistory().get(1).getEmoticon() == "worst");
        assertTrue(participant.getMoodHistory().get(1).getSocialSituation() == "At home");

        // Item was added correctly. Now onto inserting something with nulls.. Should pass because this is not firebase testing...
        Mood secondmood = new Mood();
        participant.addMood(secondmood);
        assertTrue("False Add, should succeed backend only.",participant.getMoodHistory().get(2) == secondmood);
    }

    /**
     * testEdit adds an object to the mock user, edits its values, and re
     */
    @Test
    public void testEdit(){
        Participant participant = mockUser();
        assertEquals(1,participant.getMoodHistory().size());

        Date date1 = new Date(1925,2,10);
        Mood mood = new Mood(date1,"Breakup","At home","worst");

        participant.addMood(mood);
        assertTrue("Right Index",participant.getMoodHistory().get(1).getReason() == "Breakup");
        assertTrue(participant.getMoodHistory().get(1).getEmoticon() == "worst");
        assertTrue(participant.getMoodHistory().get(1).getSocialSituation() == "At home");

        participant.getMoodHistory().get(1).setReason("Wife");
        participant.getMoodHistory().get(1).setEmoticon("great");
        participant.getMoodHistory().get(1).setSocialSituation("Court");

//        Date date2 = new Date(1232,6,3);
//        participant.getMoodHistory().get(1).setDate();

        assertTrue(participant.getMoodHistory().get(1).getReason() == "Wife");
        assertTrue(participant.getMoodHistory().get(1).getEmoticon() == "great");
        assertTrue(participant.getMoodHistory().get(1).getSocialSituation() == "Court");
        assertTrue(participant.getMoodHistory().get(1).getDatetime() == date1);

        assertTrue("Wrong Name",participant.getName()== "mockUser");
    }
    @Test
    public void testDelete(){
        Participant participant = mockUser();
        assertEquals(1,participant.getMoodHistory().size());

        Date date1 = new Date(1925,2,10);
        Mood mood = new Mood(date1,"Breakup","At home","worst");
        participant.addMood(mood);

        participant.getMoodHistory().remove(1);

        assertEquals(1,participant.getMoodHistory().size());

        Mood mood2 = new Mood(date1,"arb reason","At work","neutral");
        participant.addMood(mood2);
        Mood mood3 = new Mood(date1,"arb reason 2","At work again","bad");
        participant.addMood(mood3);

        participant.getMoodHistory().remove(1);

        assertEquals(2,participant.getMoodHistory().size());
        assertTrue(participant.getMoodHistory().get(1).getReason() == "arb reason 2");
        assertTrue(participant.getMoodHistory().get(1).getSocialSituation() == "At work again");
        assertTrue(participant.getMoodHistory().get(1).getEmoticon() == "bad");

    }
}
