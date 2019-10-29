package com.example.myapplication;

import android.location.Location;
import android.media.Image;

import java.sql.Time;
import java.util.Date;

public class Mood {

    private Date date;
    private Time time;
    private Location location;
    private State state;
    private String string;
    private String socialSituation;
    private int picture;

    public Mood(Date date, Time time, Location location, State state, String string, String socialSituation, int picture) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.state = state;
        this.string = string;
        this.socialSituation = socialSituation;
        this.picture = picture;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getSocialSituation() {
        return socialSituation;
    }

    public void setSocialSituation(String socialSituation) {
        this.socialSituation = socialSituation;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }
}
