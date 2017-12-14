package com.teaera.teaerastore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teaera.teaerastore.R;
import com.teaera.teaerastore.app.Application;
import com.teaera.teaerastore.net.Model.OrderInfo;
import com.teaera.teaerastore.net.Request.SearchOrderRequest;
import com.teaera.teaerastore.net.Response.SearchOrdersResponse;
import com.teaera.teaerastore.preference.SearchOrderPrefs;
import com.teaera.teaerastore.preference.StorePrefs;
import com.teaera.teaerastore.utils.DialogUtils;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private RelativeLayout menuRelativeLayout;
//    private RelativeLayout searchRelativeLayout;
//    private ImageButton closeSearchButton;
    private TextView locationTextView;

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText orderEditText;
    private TextView fromTextView;
    private TextView toTextView;

    private SweetAlertDialog loaderDialog;
    private boolean menuDisplayed = false;

    public boolean isSearched = false;
    public ArrayList<OrderInfo> orders = new ArrayList<OrderInfo>();

    private int selectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuRelativeLayout = findViewById(R.id.menuRelativeLayout);
        menuRelativeLayout.setVisibility(View.GONE);

//        searchRelativeLayout = findViewById(R.id.searchRelativeLayout);
//        searchRelativeLayout.setVisibility(View.GONE);

        locationTextView = findViewById(R.id.locationTextView);
        locationTextView.setText(StorePrefs.getStoreInfo(this).getName());

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        orderEditText = findViewById(R.id.orderEditText);
        fromTextView = findViewById(R.id.fromTextView);
        toTextView = findViewById(R.id.toTextView);

//        closeSearchButton = findViewById(R.id.closeSearchButton);
//        closeSearchButton.setOnClickListener(this);

        ImageView logoImageView = findViewById(R.id.logoImageView);
        logoImageView.setOnClickListener(this);

        Button newOrderButton = findViewById(R.id.newOrderButton);
        newOrderButton.setOnClickListener(this);

        Button completedOrderButton = findViewById(R.id.completedOrderButton);
        completedOrderButton.setOnClickListener(this);

        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);

        Button storeProfileButton = findViewById(R.id.storeProfileButton);
        storeProfileButton.setOnClickListener(this);

        Button signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(this);

        ImageButton closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(this);

//        ImageButton searchImageButton = findViewById(R.id.searchImageButton);
//        searchImageButton.setOnClickListener(this);

//        SearchOrderPrefs.clearSearchOrders(MainActivity.this);
//        SearchOrderPrefs.setSearched(MainActivity.this, false);
        selectMenuItem(0);
    }


    public void selectMenuItem(int menuItem) {

        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        selectedIndex = menuItem;
        switch(menuItem) {
            case 0:
                fragmentClass = NewOrderFragment.class;
                break;
            case 1:
                fragmentClass = CompletedOrderFragment.class;
                break;
            case 2:
                fragmentClass = SearchFragment.class;
                break;
            case 3:
                fragmentClass = StoreProfileFragment.class;
                break;
            default:
                fragmentClass = NewOrderFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager =  getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.framelayout, fragment).commit();

        hideMenu();
    }

//    private void searchOrder(String firstName, String lastName, String order, String fromDate, String toDate) {
//
//        if (!fromDate.isEmpty() && !toDate.isEmpty()) {
//            fromDate = fromDate + " 00:00:00";
//            toDate = toDate + " 23:59:59";
//        }
//
//        showLoader(R.string.empty);
//
//        Application.getServerApi().searchOrders(new SearchOrderRequest(firstName, lastName, order, fromDate, toDate, StorePrefs.getStoreInfo(MainActivity.this).getId(), Integer.toString(selectedIndex))).enqueue(new Callback<SearchOrdersResponse>(){
//
//            @Override
//            public void onResponse(Call<SearchOrdersResponse> call, Response<SearchOrdersResponse> response) {
//                hideLoader();
//                if (response.body().isError()) {
//                    DialogUtils.showDialog(MainActivity.this, "Error", response.body().getMessage(), null, null);
//                } else {
//
//                    SearchOrderPrefs.saveSearchOrders(MainActivity.this, response.body().getOrders());
//                    SearchOrderPrefs.setSearched(MainActivity.this, true);
//                    selectMenuItem(selectedIndex);
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SearchOrdersResponse> call, Throwable t) {
//                hideLoader();
//                if (t.getLocalizedMessage() != null) {
//                    Log.d("Main", t.getLocalizedMessage());
//                } else {
//                    Log.d("Main", "Unknown error");
//                }
//            }
//        });
//    }


    // Menu
    public void showMenu() {
        menuDisplayed = true;
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        menuRelativeLayout.startAnimation(animation);
        menuRelativeLayout.setVisibility(View.VISIBLE);
        //searchRelativeLayout.setVisibility(View.GONE);
    }

    public void hideMenu() {
        menuDisplayed = false;
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        menuRelativeLayout.startAnimation(animation);
        menuRelativeLayout.setVisibility(View.GONE);
        //searchRelativeLayout.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View view) {
//        if (view.getId() == R.id.logoImageView || view.getId() == R.id.closeSearchButton || view.getId() == R.id.closeButton) {
//            SearchOrderPrefs.clearSearchOrders(MainActivity.this);
//            SearchOrderPrefs.setSearched(MainActivity.this, false);
//        }

        switch (view.getId()) {
            case R.id.logoImageView:
                if (!menuDisplayed)
                    showMenu();
                break;
//            case R.id.closeSearchButton:
//                searchRelativeLayout.setVisibility(View.GONE);
//                break;
            case R.id.closeButton:
                hideMenu();
                break;
            case R.id.newOrderButton:
                selectMenuItem(0);
                break;
            case R.id.completedOrderButton:
                selectMenuItem(1);
                break;
            case R.id.searchButton:
                selectMenuItem(2);
//                hideMenu();
//                if (selectedIndex == 0 || selectedIndex == 1) {
//                    searchRelativeLayout.setVisibility(View.VISIBLE);
//                }

                break;
            case R.id.storeProfileButton:
                selectMenuItem(3);
                break;
            case R.id.signOutButton:
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;

//            case R.id.searchImageButton:
//                String firstName = firstNameEditText.getText().toString();
//                String lastName = lastNameEditText.getText().toString();
//                String order = orderEditText.getText().toString();
//                String fromDate = fromTextView.getText().toString();
//                String toDate = toTextView.getText().toString();
//
//                if (firstName.isEmpty() && lastName.isEmpty() && order.isEmpty()) {
//                    if (fromDate.isEmpty() && toDate.isEmpty()) {
//                        DialogUtils.showDialog(MainActivity.this, "Error", getString(R.string.empty_search_options), null, null);
//                        break;
//                    } else if (fromDate.isEmpty() || toDate.isEmpty()) {
//                        DialogUtils.showDialog(MainActivity.this, "Error", getString(R.string.error_date_search), null, null);
//                        break;
//                    }
//                }
//
//                searchOrder(firstName, lastName, order, fromDate, toDate);
//
//                break;
        }
    }

//    public void showLoader(int resId) {
//        loaderDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        loaderDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.loader_color));
//        loaderDialog.setTitleText(getString(resId));
//        loaderDialog.setCancelable(false);
//        loaderDialog.show();
//    }
//
//    public void hideLoader() {
//        if (loaderDialog != null) {
//            loaderDialog.dismissWithAnimation();
//        }
//    }

}
