package com.teaera.teaerastore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.teaera.teaerastore.R;
import com.teaera.teaerastore.app.Application;
import com.teaera.teaerastore.preference.UserPrefs;
import com.teaera.teaerastore.utils.DialogUtils;

public class SplashActivity extends BaseActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (UserPrefs.isLoggedIn(SplashActivity.this)) {
                    loadData();
                } else {
                    Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void loadData() {
//        showLoader(R.string.empty);
//
//        Application.getServerApi().getCategories().enqueue(new Callback<GetCategoryResponse>(){
//
//            @Override
//            public void onResponse(Call<GetCategoryResponse> call, Response<GetCategoryResponse> response) {
//                hideLoader();
//                if (response.body().isError()) {
//                    DialogUtils.showDialog(SplashActivity.this, "Error", response.body().getMessage(), null, null);
//                } else {
//                    LocationPrefs.setLocations(SplashActivity.this, response.body().getLocations());
//                    CategoryPrefs.setCategories(SplashActivity.this, response.body().getCategories());
//                    PromotedMenuPrefs.setPromotedMenu(SplashActivity.this, response.body().getPromoted());
//
//                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
//                    finish();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetCategoryResponse> call, Throwable t) {
//                hideLoader();
//                if (t.getLocalizedMessage() != null) {
//                    Log.d("Splash", t.getLocalizedMessage());
//                } else {
//                    Log.d("Splash", "Unknown error");
//                }
//            }
//        });
    }

}
