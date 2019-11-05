package com.example.myapplication;

import android.graphics.Color;

public class State {

    private String color;
    private String emoticons;

    public State(String color, String emoticons){
        this.color = color;
        this.emoticons = emoticons;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEmoticons() {
        return emoticons;
    }

    public void setEmoticons(String emoticons) {
        this.emoticons = emoticons;
    }
}
