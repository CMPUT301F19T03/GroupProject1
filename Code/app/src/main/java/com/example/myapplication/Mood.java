package com.example.myapplication;

import android.location.Location;
import android.media.Image;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Mood implements Serializable {
    private Date datetime;
    private Location location;
    private String reason;
    private String socialSituation;
    private int emoticon;
    private Image picture;

    public Mood(){}

    public Mood(Date datetime, String reason, String socialSituation, int Emoticon) {
        this.datetime = datetime;
        this.reason = reason;
        this.socialSituation = socialSituation;
        this.emoticon = Emoticon;
    }
    public Mood(Date datetime, Location location, String reason, String socialSituation,int Emoticon, Image picture) {
        this.datetime = datetime;
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

    @Exclude
    public Date getDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.datetime);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    @Exclude
    public String getStringDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(datetime);
        return c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DAY_OF_MONTH);
    }
    @Exclude
    public Date getTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.datetime);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long diff = cal.getTime().getTime()-getDate().getTime();
        return new Date(diff);
    }
    @Exclude
    public String getStringTime() {
        Calendar c = Calendar.getInstance();
        c.setTime(datetime);
        return c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);

    }

    public void setDatetime(Date date) {
        this.datetime = datetime;
    }

    public Date getDatetime() {
        return datetime;
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

    public Image getPicture() {
        return picture;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
    }
}
