package com.abhi.optumsoftchallenge.network;

import com.android.volley.Request;

import java.util.HashMap;

/**
 * Created by abhi on 6/26/18.
 */

public class SensorConfigRequest extends VolleyRequest<HashMap> {
    private static String url = "http://interview.optumsoft.com/config";
    public SensorConfigRequest(){
        super(Request.Method.GET, url, HashMap.class, null, null, null);
    }
}
