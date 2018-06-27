package com.abhi.optumsoftchallenge.utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.abhi.optumsoftchallenge.R;

/**
 *  Author: Abhiraj Khare
 *  Description: Class to show progress and alert dialog.
 */
public class CustomAlertDialog {

    private Activity mActivity;
    private ProgressDialog progress;

    public CustomAlertDialog(Activity activity){
        mActivity = activity;
    }

    public void createAlertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(message)
                .setTitle(mActivity.getResources().getString(R.string.alert_dialog_title_text))
                .setPositiveButton(mActivity.getResources().getString(R.string.alert_dialog_positive_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mActivity.finish();
                    }
                }).create().show();
    }

    public void createProgressDialog(String message){
        progress = new ProgressDialog(mActivity);
        progress.setMessage(message+"...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
    }

    public void dismissProgressDialog(){
        if(progress != null && progress.isShowing())
            progress.dismiss();
    }
}
