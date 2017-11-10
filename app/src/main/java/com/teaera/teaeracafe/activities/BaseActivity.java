package com.teaera.teaeracafe.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.teaera.teaeracafe.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by admin on 01/08/2017.
 */

public class BaseActivity extends Activity {

    private SharedPreferences preferences;
    private SweetAlertDialog loaderDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }


    public void showLoader(int resId) {
        loaderDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loaderDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.loader_color));
        loaderDialog.setTitleText(getString(resId));
        loaderDialog.setCancelable(false);
        loaderDialog.show();
    }

    public void hideLoader() {
        if (loaderDialog != null) {
            loaderDialog.dismissWithAnimation();
        }
    }
}
