package com.abhi.optumsoftchallenge.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by abhi on 6/26/18.
 */

public class DataManager {

    private static final DataManager ourInstance = new DataManager();
    private String TAG = this.getClass().getSimpleName();
    private ArrayList<String> sensors;
    private HashMap<String, ConfigMinMax> configMinMaxHashMap;
    private HashMap<String, Message> messageHashMap;
    private ArrayList<ArrayList<Message>> recentGraphPlottingArray = new ArrayList<>();
    private ArrayList<ArrayList<Message>> minuteGraphPlottingArray = new ArrayList<>();

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
    }

    public ArrayList<String> getSensors() {
        return sensors;
    }

    public void setSensors(ArrayList<String> sensors) {
        this.sensors = sensors;
    }

    public HashMap<String, ConfigMinMax> getConfigMinMaxHashMap() {
        return configMinMaxHashMap;
    }

    public void setConfigMinMaxHashMap(HashMap<String, ConfigMinMax> configMinMaxHashMap) {
        this.configMinMaxHashMap = configMinMaxHashMap;
    }

    public HashMap<String, Message> getMessageHashMap() {
        return messageHashMap;
    }

    public void setMessageHashMap(HashMap<String, Message> messageHashMap) {
        this.messageHashMap = messageHashMap;
    }

    public ArrayList<ArrayList<Message>> getRecentGraphPlottingArray() {
        return recentGraphPlottingArray;
    }

    public ArrayList<ArrayList<Message>> getMinuteGraphPlottingArray() {
        return minuteGraphPlottingArray;
    }
}
