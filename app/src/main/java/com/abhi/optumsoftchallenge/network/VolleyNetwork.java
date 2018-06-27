package com.abhi.optumsoftchallenge.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.util.LruCache;

import com.abhi.optumsoftchallenge.events.ConnectionStartedEvent;
import com.abhi.optumsoftchallenge.events.EventBusSingleton;
import com.abhi.optumsoftchallenge.events.NoInternetConnectionEvent;
import com.abhi.optumsoftchallenge.utilities.CustomApplication;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 *  Author: Abhiraj Khare
 *  Description: Volley request is added to Volley RequestQueue which executes Network operations.
 */
public class VolleyNetwork
{
    private static VolleyNetwork mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private VolleyNetwork() {
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache()
        {
            private final LruCache<String, Bitmap>
                    cache = new LruCache<String, Bitmap>(30);

            @Override
            public Bitmap getBitmap(String url)
            {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap)
            {
                cache.put(url, bitmap);
            }
        });
    }

    public static synchronized VolleyNetwork getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new VolleyNetwork();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(CustomApplication.getmContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        if(isNetworkAvailable()) {
            getRequestQueue().add(req);
            EventBusSingleton.instance().postEvent(new ConnectionStartedEvent());
        }
        else {
            EventBusSingleton.instance().postEvent(new NoInternetConnectionEvent());
        }
    }

    public ImageLoader getImageLoader()
    {
        return mImageLoader;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) CustomApplication.getmContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}


