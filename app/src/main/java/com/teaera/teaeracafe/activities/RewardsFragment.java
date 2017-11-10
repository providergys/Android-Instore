package com.teaera.teaeracafe.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.teaera.teaeracafe.R;
import com.teaera.teaeracafe.app.Application;
import com.teaera.teaeracafe.net.Model.UserInfo;
import com.teaera.teaeracafe.net.Request.UserProfileRequest;
import com.teaera.teaeracafe.net.Response.UserProfileResponse;
import com.teaera.teaeracafe.preference.UserPrefs;
import com.teaera.teaeracafe.utils.DialogUtils;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RewardsFragment extends Fragment {

    private Spinner drinkSpinner;
    private TextView drinkTextView;
    private TextView balanceTextView;
    private RatingBar ratingBar1;
    private RatingBar ratingBar2;
    private SweetAlertDialog loaderDialog;

    private ArrayList<String> drinkOptions = new ArrayList<String>();
    private UserInfo userInfo;

    public RewardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rewards, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        init();
    }

    private void init() {

        ratingBar1 = (RatingBar) getActivity().findViewById(R.id.ratingBar1);
        ratingBar2 = (RatingBar) getActivity().findViewById(R.id.ratingBar2);

        drinkSpinner = (Spinner) getActivity().findViewById(R.id.drinkSpinner);
        drinkTextView = (TextView) getActivity().findViewById(R.id.drinkTextView);
        balanceTextView = (TextView) getActivity().findViewById(R.id.balanceTextView);
        drinkOptions.add("No free drink");

        loadProfile();
    }

    private void updateProfile() {
        int rewards = userInfo.getRewardStar();
        int count = (rewards - rewards % 10) / 10;
        if (count >= 1) {
            for (int i=1; i<=count; i++) {
                drinkOptions.add(String.format("%d Free drink", i));
            }
        }

        int star = rewards-count*10;
        if (star <= 5) {
            ratingBar1.setRating(star);
            ratingBar2.setRating(0);
        } else {
            ratingBar1.setRating(5);
            ratingBar2.setRating(star - 5);
        }

        balanceTextView.setText("$" + userInfo.getBalance());

        drinkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                drinkTextView.setText(drinkSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,drinkOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drinkSpinner.setAdapter(adapter);
        drinkSpinner.setSelection(0);
        drinkTextView.setText(drinkSpinner.getSelectedItem().toString());

    }


    private void loadProfile() {
        showLoader(R.string.empty);

        Application.getServerApi().getUserProfile(new UserProfileRequest(UserPrefs.getUserInfo(getActivity()).getId())).enqueue(new Callback<UserProfileResponse>(){

            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(getActivity(), "Error", response.body().getMessage(), null, null);
                } else {
                    userInfo = response.body().getUser();
                    UserPrefs.saveUserInfo(getActivity(), userInfo);
                    updateProfile();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("Rewards", t.getLocalizedMessage());
                } else {
                    Log.d("Rewards", "Unknown error");
                }
            }
        });
    }

    public void showLoader(int resId) {
        loaderDialog = new SweetAlertDialog(this.getActivity(), SweetAlertDialog.PROGRESS_TYPE);
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
