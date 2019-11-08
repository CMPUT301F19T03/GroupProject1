package com.example.myapplication;

import android.location.Location;
import android.media.Image;

import com.google.firebase.firestore.Exclude;
import android.media.Image;
import android.util.Log;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

public class Mood implements Serializable {
    private Date datetime;
    private double longitude;
    private double latitude;
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
    public Mood(Date datetime, double lat, double lng, String reason, String socialSituation,int Emoticon, Image picture) {
        this.datetime = datetime;
        this.latitude = lat;
        this.longitude = lng;
        this.reason = reason;
        this.socialSituation = socialSituation;
        this.picture = picture;
        this.emoticon = Emoticon;
    }
    public Mood(Date datetime, double lat, double lng, String reason, String socialSituation,int Emoticon) {
        this.datetime = datetime;
        this.latitude = lat;
        this.longitude = lng;
        this.reason = reason;
        this.socialSituation = socialSituation;
        this.emoticon = Emoticon;
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
        c.setTime(this.datetime);
        return String.format(Locale.CANADA,"%4d-%02d-%02d",c.get(Calendar.YEAR),(c.get(Calendar.MONTH)+1),c.get(Calendar.DAY_OF_MONTH));
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
        return String.format(Locale.CANADA,"%02d:%02d",c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE));

    }

    public void setDatetime(Date date) {
        Log.d("myTag","Setting datetime");
        this.datetime = date;
    }

    public Date getDatetime() {
        return datetime;
    }

    @Exclude
    public Location getLocation() {
        Location location = new Location("");
        location.setLongitude(this.longitude);
        location.setLatitude(this.latitude);
        return location;
    }
    @Exclude
    public void setLocation(Location location) {
        if (location!=null) {
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
        }
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

    public int getEmoticon() {
        return emoticon;
    }

    public void setEmoticon(int emoticon) {
        this.emoticon = emoticon;
    }

    public Image getPicture() {
        return picture;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
