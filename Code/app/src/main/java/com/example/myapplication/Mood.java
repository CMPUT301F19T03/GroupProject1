package com.example.myapplication;

import android.location.Location;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class Mood implements Serializable {
    Date date;
    Time time;
    Location location;
    String reason;
    String socialSituation;

    public Mood(Date date, Time time, String reason, String socialSituation) {
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.socialSituation = socialSituation;
    }

    public Mood(Date date, Time time, Location location, String reason, String socialSituation) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.reason = reason;
        this.socialSituation = socialSituation;
    }
}
