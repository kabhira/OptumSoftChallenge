package com.abhi.optumsoftchallenge.network;

import android.util.Log;

import com.abhi.optumsoftchallenge.events.EventBusSingleton;
import com.abhi.optumsoftchallenge.events.SensorDeleteEvent;
import com.abhi.optumsoftchallenge.events.SensorInitEvent;
import com.abhi.optumsoftchallenge.events.SensorUpdateEvent;
import com.abhi.optumsoftchallenge.model.DataManager;
import com.abhi.optumsoftchallenge.model.DeleteMessage;
import com.abhi.optumsoftchallenge.model.InitMessage;
import com.abhi.optumsoftchallenge.model.Message;
import com.abhi.optumsoftchallenge.model.UpdateMessage;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by abhi on 6/26/18.
 */

public class SocketNetwork {

    private static final SocketNetwork ourInstance = new SocketNetwork();
    private String TAG = this.getClass().getSimpleName();
    private Socket mSocket;
    //private Socket mSensorNameSocket;
    private final Gson gson = new Gson();

    public static SocketNetwork getInstance() {
        return ourInstance;
    }

    private SocketNetwork() {

    }

    public void createServerConnection() {


        try {
            mSocket = IO.socket("http://interview.optumsoft.com");
        } catch (URISyntaxException e) {Log.e(TAG, e.getMessage());}

        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_ERROR, onError);
        mSocket.on("data", onData);
        mSocket.connect();

        /*IO.Options options = new IO.Options();
        options.forceNew = true;
        options.path = "/sensornames";

        try {
            mSensorNameSocket = IO.socket("http://interview.optumsoft.com", options);
        } catch (URISyntaxException e) {Log.e(TAG, e.getMessage());}

        mSensorNameSocket.on(Socket.EVENT_CONNECT, onSensorNameConnect);
        mSensorNameSocket.on(Socket.EVENT_DISCONNECT, onSensorNameDisconnect);
        //mSensorNameSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        //mSensorNameSocket.on(Socket.EVENT_ERROR, onError);
        mSensorNameSocket.on("data", onSensorNameData);
        mSensorNameSocket.emit("subscribe", "temperature0");
        mSensorNameSocket.connect();*/


    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e(TAG, "onConnectError");
        }
    };

    private Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e(TAG, "onError");
        }
    };

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e(TAG, "Connected");

        }
    };

    public void emitSensors() {
        for (String sensor : DataManager.getInstance().getSensors()) {
            mSocket.emit("subscribe", sensor);
        }
    }

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e(TAG, "Disconnected");
        }
    };

    private Emitter.Listener onData = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e(TAG, "onData");
            JSONObject jsonObject = (JSONObject) args[0];
            try {
                String type = jsonObject.getString("type");
                if(type.equals("init")) {
                    InitMessage initMessage = gson.fromJson(args[0].toString(), InitMessage.class);
                    HashMap<String, Message> messageHashMap = new HashMap<>();
                    ArrayList<Message> recentArrayList = new ArrayList<>();
                    // add recent objects
                    for (int i=0; i<initMessage.getRecent().size(); i++) {
                        Message message = initMessage.getRecent().get(i);
                        message.setScale("recent");
                        messageHashMap.put(message.getKey(), message);
                        recentArrayList.add(message);
                    }


                    ArrayList<Message> minuteArrayList = new ArrayList<>();
                    // add minute objects
                    for (int i=0; i<initMessage.getMinute().size(); i++) {
                        Message message = initMessage.getMinute().get(i);
                        message.setScale("minute");
                        messageHashMap.put(message.getKey(), message);
                        minuteArrayList.add(message);
                    }

                    DataManager.getInstance().setMessageHashMap(messageHashMap);
                    DataManager.getInstance().getRecentGraphPlottingArray().add(recentArrayList);
                    DataManager.getInstance().getMinuteGraphPlottingArray().add(minuteArrayList);
                    EventBusSingleton.instance().postEvent(new SensorInitEvent());
                }
                else if(type.equals("update")) {
                    UpdateMessage updateMessage = gson.fromJson(args[0].toString(), UpdateMessage.class);
                    HashMap<String, Message> messageHashMap = DataManager.getInstance().getMessageHashMap();
                    if(messageHashMap.containsKey(updateMessage.getKey()) ) {
                        messageHashMap.get(updateMessage.getKey()).setVal(updateMessage.getVal());
                    }
                    EventBusSingleton.instance().postEvent(new SensorUpdateEvent());
                }
                else if(type.equals("delete")) {
                    DeleteMessage deleteMessage = gson.fromJson(args[0].toString(), DeleteMessage.class);
                    HashMap<String, Message> messageHashMap = DataManager.getInstance().getMessageHashMap();
                    if(messageHashMap.containsKey(deleteMessage.getKey()) ) {
                        messageHashMap.remove(deleteMessage.getKey());
                    }
                    EventBusSingleton.instance().postEvent(new SensorDeleteEvent());
                }

            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    };

    public void cleanup() {
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off("data", onData);
    }




    /*private Emitter.Listener onSensorNameConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e(TAG, "SensorName Connected");
        }
    };

    private Emitter.Listener onSensorNameDisconnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e(TAG, "SensorName Disconnected");
        }
    };

    private Emitter.Listener onSensorNameData = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e(TAG, "onSensorNameData");
            DataManager.getInstance().setSensors( gson.fromJson( ((String)args[0]), ArrayList.class) );
            ArrayList<String> fs = DataManager.getInstance().getSensors();
            Log.e(TAG, fs.get(0));

            mSensorNameSocket.disconnect();
            mSensorNameSocket.off(Socket.EVENT_CONNECT, onSensorNameConnect);
            mSensorNameSocket.off(Socket.EVENT_DISCONNECT, onSensorNameDisconnect);
            mSensorNameSocket.off("data", onSensorNameData);
        }
    };*/
}
