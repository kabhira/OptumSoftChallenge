package com.abhi.optumsoftchallenge.model;

/**
 * Created by abhi on 6/26/18.
 */

public class DeleteMessage {
    private String type;
    private float key;
    private String sensor;
    private String scale;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getKey() {
        return key;
    }

    public void setKey(float key) {
        this.key = key;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }
}
