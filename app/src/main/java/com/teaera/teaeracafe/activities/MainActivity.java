package com.teaera.teaeracafe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teaera.teaeracafe.R;
import com.teaera.teaeracafe.app.Application;
import com.teaera.teaeracafe.net.Model.PromotedMenuInfo;
import com.teaera.teaeracafe.preference.CartPrefs;
import com.teaera.teaeracafe.preference.UserPrefs;
import com.teaera.teaeracafe.utils.DownloadImageTask;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private RelativeLayout menuRelativeLayout;
    private TextView menuTextView;
    private ImageView cartImageView;
    private TextView cartTextView;
    private TextView usernameTextView;

    private boolean menuDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuRelativeLayout = (RelativeLayout) findViewById(R.id.menuRelativeLayout);
        menuRelativeLayout.setVisibility(View.GONE);

        menuTextView = (TextView) findViewById(R.id.menuTextView);
        cartImageView = (ImageView) findViewById(R.id.cartImageView);
        cartTextView = (TextView) findViewById(R.id.cartTextView);
        usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        usernameTextView.setText(UserPrefs.getUserInfo(this).getFirstname() + " " + UserPrefs.getUserInfo(this).getLastname());

        ImageView photoImageView = (ImageView) findViewById(R.id.photoImageView);
        new DownloadImageTask(photoImageView).execute(Application.getServerProfileImagePrefix() + UserPrefs.getUserInfo(this).getImage());

        Button dropMenuButton = (Button) findViewById(R.id.dropMenuButton);
        dropMenuButton.setOnClickListener(this);

        Button cartButton = (Button) findViewById(R.id.cartButton);
        cartButton.setOnClickListener(this);

        Button menuButton = (Button) findViewById(R.id.menuButton);
        menuButton.setOnClickListener(this);

        Button notificationButton = (Button) findViewById(R.id.notificationButton);
        notificationButton.setOnClickListener(this);

        Button rewardButton = (Button) findViewById(R.id.rewardButton);
        rewardButton.setOnClickListener(this);

        Button profileButton = (Button) findViewById(R.id.profileButton);
        profileButton.setOnClickListener(this);

        Button helpButton = (Button) findViewById(R.id.helpButton);
        helpButton.setOnClickListener(this);

        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(this);

        selectMenuItem(0);
    }

    @Override
    public void onResume() {
        super.onResume();

        updateCardIcon();
    }

    private void updateCardIcon() {
        if (!CartPrefs.isCartsExists(this) || CartPrefs.getCarts(this).size() == 0) {
            cartImageView.setVisibility(View.INVISIBLE);
            cartTextView.setVisibility(View.INVISIBLE);
        } else {
            int count = CartPrefs.getCarts(this).size();
            cartImageView.setVisibility(View.VISIBLE);
            cartTextView.setVisibility(View.VISIBLE);
            cartTextView.setText(Integer.toString(count));
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.dropMenuButton:
                if (!menuDisplayed)
                    showMenu();
                break;

            case R.id.cartButton:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;

            case R.id.menuButton:
                menuTextView.setText(R.string.menu_menu);
                selectMenuItem(0);
                break;
            case R.id.notificationButton:
                menuTextView.setText(R.string.menu_notification);
                selectMenuItem(1);
                break;
            case R.id.rewardButton:
                menuTextView.setText(R.string.menu_rewards);
                selectMenuItem(2);
                break;
            case R.id.profileButton:
                menuTextView.setText(R.string.menu_profile);
                selectMenuItem(3);
                break;
            case R.id.helpButton:
                menuTextView.setText(R.string.menu_help);
                selectMenuItem(4);
                break;
            case R.id.logoutButton:
                selectMenuItem(5);
                break;
        }
    }

    // Menu
    public void showMenu() {
        menuDisplayed = true;
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        menuRelativeLayout.startAnimation(animation);
        menuRelativeLayout.setVisibility(View.VISIBLE);
    }

    public void hideMenu() {
        menuDisplayed = false;
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        menuRelativeLayout.startAnimation(animation);
        menuRelativeLayout.setVisibility(View.GONE);
    }


    public void selectMenuItem(int menuItem) {

        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem) {
            case 0:
                fragmentClass = MenuFragment.class;
                break;
            case 1:
                fragmentClass = NotificationFragment.class;
                break;
            case 2:
                fragmentClass = RewardsFragment.class;
                break;
            case 3:
                fragmentClass = ProfileFragment.class;
                break;
            case 4:
                fragmentClass = HelpFragment.class;
                break;
            case 5:
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                return;
            default:
                fragmentClass = MenuFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager =  getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();

        hideMenu();
    }
}
