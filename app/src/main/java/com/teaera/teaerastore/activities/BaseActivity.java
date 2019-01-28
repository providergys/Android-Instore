package com.teaera.teaerastore.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by admin on 29/08/2017.
 */

public class BaseActivity extends Activity {

    private SharedPreferences preferences;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }


    public void showLoader(int resId) {
        dialog = ProgressDialog.show(this, "",
                getString(resId), true);
    }

    public void hideLoader() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public String getReceivedDate(String dateString) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat newSdf = new SimpleDateFormat("MM/dd/yy, hh:mm a");

        try {
            Date date = sdf.parse(dateString);
            return  newSdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
}
