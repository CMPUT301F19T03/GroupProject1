package com.example.myapplication;

import android.content.res.Resources;
import android.location.Location;
import android.media.Image;

import com.google.firebase.firestore.Exclude;
import android.media.Image;
import android.util.Log;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

public class FollowMood implements Serializable {
    private Date datetime;
    private String emoticon;
    private String user;



    public FollowMood(){}

    public FollowMood(Date datetime, String user, String Emoticon) {
        this.datetime = datetime;
        this.user = user;
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


    public String getEmoticon() {
        return emoticon;
    }

    public void setEmoticon(String emoticon) {
        this.emoticon = emoticon;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Exclude
    public int getEmoteIcon() {
        Resources res = MainActivity.RESOURCES;
        return res.getIdentifier(this.emoticon,"drawable",MainActivity.PACKAGE_NAME);
    }
    @Exclude
    public void setEmoteIcon(int emoteIcon) {
        Resources res = MainActivity.RESOURCES;
        this.emoticon = res.getResourceEntryName(emoteIcon);
    }

}
