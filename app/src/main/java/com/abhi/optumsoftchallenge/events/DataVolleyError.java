package com.abhi.optumsoftchallenge.events;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 *  Author: Abhiraj Khare
 *  Description: interface for getting volley library error.
 */
public class DataVolleyError implements Response.ErrorListener{

    private static String TAG = "DataVolleyError";

    @Override
    public void onErrorResponse(VolleyError error) {
        //Log.e(TAG, error.toString()+"");
        EventBusSingleton.instance().postEvent(error);
    }
}
