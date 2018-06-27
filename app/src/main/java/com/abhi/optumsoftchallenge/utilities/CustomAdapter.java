package com.abhi.optumsoftchallenge.utilities;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.abhi.optumsoftchallenge.R;

import java.util.HashMap;
import java.util.List;

/**
 *  Author: Abhiraj Khare
 *  Description: Adapter to display lists.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private List<String> mDataset;
    private FragmentActivity mActivity;
    HashMap<Integer, Boolean> radioSelected = new HashMap<>();
    private PopupWindow popupWindow;

    public CustomAdapter(List<String> myDataset, FragmentActivity mActivity, PopupWindow popupWindow) {
        mDataset = myDataset;
        this.mActivity = mActivity;
        this.popupWindow = popupWindow;

        radioSelected.put(0, true);
        for(int i=1; i<mDataset.size(); i++) {
            radioSelected.put(i, false);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView cellName;
        public RadioButton cellRadio;
        public ViewHolder(View v) {
            super(v);
            cellName = (TextView) v.findViewById(R.id.sensor_textView);
            cellRadio = (RadioButton) v.findViewById(R.id.sensor_radioButton);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(cellRadio.isChecked()) {
                radioSelected.put(getAdapterPosition(), false);
            }
            else {
                radioSelected.put(getAdapterPosition(), true);
            }
            notifyDataSetChanged();
            popupWindow.dismiss();
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String element = mDataset.get(position);
        holder.cellName.setText(element);
        holder.cellRadio.setChecked(radioSelected.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public HashMap<Integer, Boolean> getRadioSelected() {
        return radioSelected;
    }
}

