package com.example.myapplication;

import android.graphics.Color;

public class State {

    private Color color;
    private String emoticons;

    public State(Color color, String emoticons){
        this.color = color;
        this.emoticons = emoticons;
    }
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getEmoticons() {
        return emoticons;
    }

    public void setEmoticons(String emoticons) {
        this.emoticons = emoticons;
    }
}
