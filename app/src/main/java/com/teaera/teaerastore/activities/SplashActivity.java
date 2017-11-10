package com.teaera.teaerastore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.teaera.teaerastore.R;
import com.teaera.teaerastore.app.Application;
import com.teaera.teaerastore.net.Model.StoreInfo;
import com.teaera.teaerastore.net.Request.GetStoreRequest;
import com.teaera.teaerastore.net.Response.GetStoresResponse;
import com.teaera.teaerastore.net.Response.StoreResponse;
import com.teaera.teaerastore.preference.StorePrefs;
import com.teaera.teaerastore.utils.DialogUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private ArrayList<StoreInfo> stores = new ArrayList<StoreInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (StorePrefs.isLoggedIn(SplashActivity.this)) {
                    loadData();
                } else {
                    getStores();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void getStores() {
//        showLoader(R.string.empty);

        Application.getServerApi().getStores().enqueue(new Callback<GetStoresResponse>(){

            @Override
            public void onResponse(Call<GetStoresResponse> call, Response<GetStoresResponse> response) {
//                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(SplashActivity.this, "Error", response.body().getMessage(), null, null);
                } else {
                    stores = response.body().getStores();
                    Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                    intent.putExtra("stores", stores);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<GetStoresResponse> call, Throwable t) {
//                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("Splash", t.getLocalizedMessage());
                } else {
                    Log.d("Splash", "Unknown error");
                }
            }
        });
    }


    private void loadData() {
        Application.getServerApi().getStoreById(new GetStoreRequest(StorePrefs.getStoreInfo(this).getId())).enqueue(new Callback<StoreResponse>(){

            @Override
            public void onResponse(Call<StoreResponse> call, Response<StoreResponse> response) {
                if (response.body().isError()) {
                    DialogUtils.showDialog(SplashActivity.this, "Error", response.body().getMessage(), null, null);
                } else {
                    StorePrefs.saveStoreInfo(SplashActivity.this, response.body().getStoreInfo());
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<StoreResponse> call, Throwable t) {
                if (t.getLocalizedMessage() != null) {
                    Log.d("Splash", t.getLocalizedMessage());
                } else {
                    Log.d("Splash", "Unknown error");
                }
            }
        });



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
