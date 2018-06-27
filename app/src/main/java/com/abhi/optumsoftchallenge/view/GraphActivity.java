package com.abhi.optumsoftchallenge.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.abhi.optumsoftchallenge.R;
import com.abhi.optumsoftchallenge.events.EventBusSingleton;
import com.abhi.optumsoftchallenge.events.NoInternetConnectionEvent;
import com.abhi.optumsoftchallenge.events.SensorDeleteEvent;
import com.abhi.optumsoftchallenge.events.SensorInitEvent;
import com.abhi.optumsoftchallenge.events.SensorUpdateEvent;
import com.abhi.optumsoftchallenge.model.ConfigMinMax;
import com.abhi.optumsoftchallenge.model.DataManager;
import com.abhi.optumsoftchallenge.model.Message;
import com.abhi.optumsoftchallenge.network.SensorConfigRequest;
import com.abhi.optumsoftchallenge.network.SensorNameRequest;
import com.abhi.optumsoftchallenge.network.SocketNetwork;
import com.abhi.optumsoftchallenge.network.VolleyNetwork;
import com.abhi.optumsoftchallenge.utilities.CustomAdapter;
import com.abhi.optumsoftchallenge.utilities.CustomAlertDialog;
import com.abhi.optumsoftchallenge.utilities.CustomApplication;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

public class GraphActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    private CustomAdapter mAdapter;
    private GraphView graph;
    private RadioButton recentRadio, minutesRadio;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        graph = (GraphView) findViewById(R.id.graph);
        recentRadio = (RadioButton) findViewById(R.id.recent_radio);
        recentRadio.setChecked(true);
        minutesRadio = (RadioButton) findViewById(R.id.minutes_radio);
        radioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);

        SocketNetwork.getInstance().createServerConnection();
        SensorNameRequest sensorNameRequest = new SensorNameRequest();
        VolleyNetwork.getInstance(CustomApplication.getmContext()).addToRequestQueue(sensorNameRequest);

        SensorConfigRequest sensorConfigRequest = new SensorConfigRequest();
        VolleyNetwork.getInstance(CustomApplication.getmContext()).addToRequestQueue(sensorConfigRequest);


    }

    @Override
    public void onResume() {
        super.onResume();
        EventBusSingleton.instance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBusSingleton.instance().unregister(this);
    }

    @Subscribe
    public void sensorNameData(ArrayList event){
        DataManager.getInstance().setSensors(event);
        SocketNetwork.getInstance().emitSensors();


        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.popup,null);
        final PopupWindow popupWindow = new PopupWindow(customView, RecyclerView.LayoutParams.MATCH_PARENT, 300);
        RecyclerView mRecyclerView = (RecyclerView) customView.findViewById(R.id.list_recycler_view);
        //mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CustomAdapter(DataManager.getInstance().getSensors(), this, popupWindow);
        mRecyclerView.setAdapter(mAdapter);


        final ConstraintLayout constraintLayout = findViewById(R.id.parent_layout);
        findViewById(R.id.sensor_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                else {
                    popupWindow.showAtLocation(constraintLayout, Gravity.BOTTOM,0,0);
                }

            }
        });
    }

    @Subscribe
    public void sensorConfigData(HashMap<String, ConfigMinMax> event){
        DataManager.getInstance().setConfigMinMaxHashMap(event);
    }

    @Subscribe
    public void sensorUpdateEvent(SensorUpdateEvent event){
        //refresh
        refreshGraph();
    }

    @Subscribe
    public void sensorDeleteEvent(SensorDeleteEvent event){
        //refresh
        refreshGraph();
    }

    @Subscribe
    public void SensorInitEvent(SensorInitEvent event){


    }

    public void refreshGraph() {

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLACK);
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.DKGRAY);
        colors.add(Color.GRAY);
        colors.add(Color.GREEN);
        colors.add(Color.LTGRAY);
        colors.add(Color.MAGENTA);
        colors.add(Color.RED);
        colors.add(Color.YELLOW);

        graph.removeAllSeries();
        for(int sel=0; sel<mAdapter.getRadioSelected().size(); sel++) {
            if(mAdapter.getRadioSelected().containsKey(sel) && mAdapter.getRadioSelected().get(sel)) {

                ArrayList<Message> innerArray;
                if(recentRadio.isChecked())
                    innerArray = DataManager.getInstance().getRecentGraphPlottingArray().get(sel);
                else
                    innerArray = DataManager.getInstance().getMinuteGraphPlottingArray().get(sel);

                DataPoint dataPoints[] = new DataPoint[innerArray.size()];
                for(int i=0; i<innerArray.size(); i++) {
                    DataPoint dataPoint = new DataPoint(i, innerArray.get(i).getVal());
                    dataPoints[i] = dataPoint;
                }
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
                series.setColor(colors.get(sel));
                graph.addSeries(series);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SocketNetwork.getInstance().cleanup();
    }

    @Subscribe
    public void noInternetConnectionEvent(NoInternetConnectionEvent event){
        CustomAlertDialog customAlertDialog = new CustomAlertDialog(this);
        customAlertDialog.createAlertDialog("No Internet...");
    }
}
