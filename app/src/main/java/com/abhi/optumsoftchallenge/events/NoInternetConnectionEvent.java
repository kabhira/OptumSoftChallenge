package com.abhi.optumsoftchallenge.events;

import android.util.Log;

/**
 *  Author: Abhiraj Khare
 *  Description: Event fired by event bus, when device network connection is not present.
 */
public class NoInternetConnectionEvent {
    public NoInternetConnectionEvent(){
        Log.i("Event", "NoInternetConnectionEvent");
    }
}
