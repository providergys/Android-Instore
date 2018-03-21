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
import com.teaera.teaerastore.net.Model.OrderInfo;
import com.teaera.teaerastore.preference.StorePrefs;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private RelativeLayout menuRelativeLayout;
    private TextView locationTextView;

    private boolean menuDisplayed = false;

    public ArrayList<OrderInfo> orders = new ArrayList<OrderInfo>();

    private int selectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuRelativeLayout = findViewById(R.id.menuRelativeLayout);
        menuRelativeLayout.setVisibility(View.GONE);

        locationTextView = findViewById(R.id.locationTextView);
        locationTextView.setText(StorePrefs.getStoreInfo(this).getName());

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

        switch (view.getId()) {
            case R.id.logoImageView:
                if (!menuDisplayed)
                    showMenu();
                break;
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
                break;
            case R.id.storeProfileButton:
                selectMenuItem(3);
                break;
            case R.id.signOutButton:
                StorePrefs.clearStoreInfo(this);
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;

        }
    }

}
