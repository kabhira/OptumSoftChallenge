package com.abhi.optumsoftchallenge.model;

import java.util.ArrayList;

/**
 * Created by abhi on 6/26/18.
 */

public class InitMessage {

    private String type;
    private ArrayList<Message> recent;
    private ArrayList<Message> minute;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Message> getRecent() {
        return recent;
    }

    public void setRecent(ArrayList<Message> recent) {
        this.recent = recent;
    }

    public ArrayList<Message> getMinute() {
        return minute;
    }

    public void setMinute(ArrayList<Message> minute) {
        this.minute = minute;
    }
}
