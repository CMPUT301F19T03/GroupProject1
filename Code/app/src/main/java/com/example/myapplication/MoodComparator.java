package com.example.myapplication;

import java.util.Comparator;

/**
 * This class is responsible for reverse ordering the mood history array list
 * https://stackoverflow.com/questions/2784514/sort-arraylist-of-custom-objects-by-property
 * is where I received the basic outline for the code that I then modified from.
 */

public class MoodComparator implements Comparator<Mood> {
    @Override
    /**
     * compare takes two moods and uses Date class' compareTo function to sort
     * compareTo sorts chronologically so the if statements are set up in a way that the opposite
     * values are returned than what is traditionally expected from compareTo
     * the nested if is to compare the time of each mood obejct if they are made on the same date. 
     */
    public int compare(Mood m1, Mood m2){
       if(m1.getDate().compareTo(m2.getDate())>0){
           return -1;
       }
       if(m1.getDate().compareTo(m2.getDate())==0){
           if (m1.getTime().compareTo(m2.getTime())>0){
               return -1;
           }
       }
       return 1;
    }
}
