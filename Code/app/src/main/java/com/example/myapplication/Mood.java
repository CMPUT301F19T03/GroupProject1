package com.example.myapplication;

import android.location.Location;
import android.media.Image;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class Mood implements Serializable {
    private Date date;
    private Time time;
    private Location location;
    private String reason;
    private String socialSituation;
    private int emoticon;
    private Image picture;

    public Image getPicture() {
        return picture;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
    }

    public Mood(Date date, Time time, String reason, String socialSituation, int Emoticon) {
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.socialSituation = socialSituation;
        this.emoticon = Emoticon;
    }
    public Mood(Date date, Time time, Location location, String reason, String socialSituation,int Emoticon, Image picture) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.reason = reason;
        this.socialSituation = socialSituation;
        this.emoticon = Emoticon;
        this.picture = picture;
    }

    public int getEmoticon() {
        return emoticon;
    }

    public void setEmoticon(int emoticon) {
        this.emoticon = emoticon;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSocialSituation() {
        return socialSituation;
    }

    public void setSocialSituation(String socialSituation) {
        this.socialSituation = socialSituation;
    }
}
