package com.abhi.optumsoftchallenge.utilities;

import android.app.Application;
import android.content.Context;

import com.abhi.optumsoftchallenge.model.DataManager;

/**
 *  Author: Abhiraj Khare
 *  Description: custom application class to access application context.
 */
public class CustomApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        DataManager.getInstance();
    }

    public static Context getmContext(){
        return mContext;
    }
}
