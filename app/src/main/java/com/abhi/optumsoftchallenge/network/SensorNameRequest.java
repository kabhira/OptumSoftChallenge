package com.abhi.optumsoftchallenge.network;

import com.android.volley.Request;

import java.util.ArrayList;

/**
 *  Author: Abhiraj Khare
 *  Description: Network request to be executed by Volley to fetch json.
 */

public class SensorNameRequest extends VolleyRequest<ArrayList> {

    private static String url = "http://interview.optumsoft.com/sensornames";
    public SensorNameRequest(){
        super(Request.Method.GET, url, ArrayList.class, null, null, null);
    }
}