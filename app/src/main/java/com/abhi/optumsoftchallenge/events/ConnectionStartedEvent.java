package com.abhi.optumsoftchallenge.events;

import android.util.Log;

/**
 *  Author: Abhiraj Khare
 *  Description: Event fired by event bus, when network request is sent by volley.
 */
public class ConnectionStartedEvent {
    public ConnectionStartedEvent(){
        Log.i("Event", "ConnectionStartedEvent");
    }
}
