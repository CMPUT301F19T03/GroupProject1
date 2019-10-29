package com.example.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class Participant implements Serializable {
    String name;
    ArrayList<Mood> moodHistory;
    ArrayList<String> requests;
    ArrayList<String> following;

    public Participant(){}

    public Participant(String name) {
        this.name = name;
        this.moodHistory = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.following = new ArrayList<>();
    }

    public void addMood(Mood mood) {
        moodHistory.add(mood);
    }
    public void addRequest(String request) {
        requests.add(request);
    }
    public void addFollowing(String name) {
        following.add(name);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Mood> getMoodHistory() {
        return moodHistory;
    }

    public ArrayList<String> getRequests() {
        return requests;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMoodHistory(ArrayList<Mood> moodHistory) {
        this.moodHistory = moodHistory;
    }

    public void setRequests(ArrayList<String> requests) {
        this.requests = requests;
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }
}
