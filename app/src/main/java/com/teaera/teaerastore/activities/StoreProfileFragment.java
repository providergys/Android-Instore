package com.teaera.teaerastore.activities;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.teaera.teaerastore.R;
import com.teaera.teaerastore.app.Application;
import com.teaera.teaerastore.net.Model.StoreInfo;
import com.teaera.teaerastore.net.Request.UpdateStoreRequest;
import com.teaera.teaerastore.net.Response.StoreResponse;
import com.teaera.teaerastore.preference.StorePrefs;
import com.teaera.teaerastore.utils.DialogUtils;

import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreProfileFragment extends Fragment implements View.OnClickListener {

    private TextView storeNametextView;
    private TextView storeAddressTextView;

    private Spinner waitTimeSpinner;
    private TextView waitTimeTextView;
    private Spinner openHourSpinner;
    private TextView openHourTextView;
    private Spinner closeHourSpinner;
    private TextView closeHourTextView;

    private ImageView timeCheckImageView;
    private ImageView hourCheckImageView;

    private TextView fromTextView;
    private TextView toTextView;

    private String[] waitingTimes = {"- None -", "5 MINS", "10 MINS", "15 MINS", "20 MINS", "25 MINS", "30 MINS", "35 MINS", "40 MINS", "45 MINS", "50 MINS", "55 MINS", "60 MINS"};
    private String[] hours = {"- None -", "12:00 AM", "12:30 AM", "1:00 AM", "1:30 AM", "2:00 AM", "2:30 AM", "3:00 AM", "3:30 AM", "4:00 AM", "4:30 AM", "5:00 AM", "5:30 AM", "6:00 AM", "6:30 AM", "7:00 AM", "7:30 AM", "8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM", "1:00 PM", "1:30 PM", "2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM", "5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM", "8:00 PM", "8:30 PM", "9:00 PM", "9:30 PM", "10:00 PM", "10:30 PM", "11:00 PM", "11:30 PM", "12:00 AM"};

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SweetAlertDialog loaderDialog;

    public StoreProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        init();
        updateTimeSpinners();
    }

    private void init() {
        storeNametextView = getActivity().findViewById(R.id.storeNametextView);
        storeAddressTextView = getActivity().findViewById(R.id.storeAddressTextView);
        storeNametextView.setText(StorePrefs.getStoreInfo(getActivity()).getName());
        storeAddressTextView.setText(StorePrefs.getStoreInfo(getActivity()).getAddress());

        waitTimeSpinner = getActivity().findViewById(R.id.waitTimeSpinner);
        openHourSpinner = getActivity().findViewById(R.id.openHourSpinner);
        closeHourSpinner = getActivity().findViewById(R.id.closeHourSpinner);
        waitTimeTextView = getActivity().findViewById(R.id.waitTimeTextView);
        openHourTextView = getActivity().findViewById(R.id.openHourTextView);
        closeHourTextView = getActivity().findViewById(R.id.closeHourTextView);

        timeCheckImageView = getActivity().findViewById(R.id.timeCheckImageView);
        hourCheckImageView = getActivity().findViewById(R.id.hourCheckImageView);

        fromTextView = getActivity().findViewById(R.id.fromTextView);
        toTextView = getActivity().findViewById(R.id.toTextView);
        fromTextView.setOnClickListener(this);
        toTextView.setOnClickListener(this);

        ImageButton saveImageButton = getActivity().findViewById(R.id.saveImageButton);
        saveImageButton.setOnClickListener(this);

        ImageButton processImageButton = getActivity().findViewById(R.id.processImageButton);
        processImageButton.setOnClickListener(this);

        Calendar now = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener fromDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                fromTextView.setText(i + " / " + (i1 + 1) + " / " + i2);
            }
        };

        fromDatePickerDialog = new DatePickerDialog(
                getActivity(), fromDateSetListener, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

        DatePickerDialog.OnDateSetListener toDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                toTextView.setText(i + " / " + (i1 + 1) + " / " + i2);
            }
        };

        toDatePickerDialog = new DatePickerDialog(
                getActivity(), toDateSetListener, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

    }

    void updateTimeSpinners() {
        StoreInfo info = StorePrefs.getStoreInfo(getActivity());
        int selectedWaitTime = Integer.parseInt(info.getWaitingTime())/5;

        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,waitingTimes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waitTimeSpinner.setAdapter(adapter);
        waitTimeSpinner.setSelection(selectedWaitTime);
        waitTimeTextView.setOnClickListener(this);

        waitTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                waitTimeTextView.setText(waitTimeSpinner.getSelectedItem().toString());
                if (position != 0) {
                    timeCheckImageView.setImageResource(R.drawable.check);
                } else {
                    timeCheckImageView.setImageResource(R.drawable.uncheck);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter adapter1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,hours);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        openHourSpinner.setAdapter(adapter1);
        openHourSpinner.setSelection(getIndexFromHours(info.getOpeningHour()));
        openHourTextView.setOnClickListener(this);

        openHourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == 0 ) {
                    hourCheckImageView.setImageResource(R.drawable.uncheck);
                } else {
                    openHourTextView.setText(openHourSpinner.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter adapter2 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,hours);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        closeHourSpinner.setAdapter(adapter1);
        closeHourSpinner.setSelection(getIndexFromHours(info.getClosingHour()));
        closeHourTextView.setOnClickListener(this);

        closeHourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (openHourSpinner.getSelectedItemPosition() > position) {
                    hourCheckImageView.setImageResource(R.drawable.uncheck);
                    DialogUtils.showDialog(getActivity(), "Error", getString(R.string.error_closing_hour), null, null);
                } else {
                    closeHourTextView.setText(closeHourSpinner.getSelectedItem().toString());
                    hourCheckImageView.setImageResource(R.drawable.check);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private int getIndexFromHours(String hour) {
        for (int i=0; i<hours.length; i++) {
            if (hours[i].equals(hour))
                return i;
        }
        return 0;
    }

    private void saveStoreData() {
        if (waitTimeSpinner.getSelectedItemPosition() !=0 && openHourSpinner.getSelectedItemPosition() != 0 && closeHourSpinner.getSelectedItemPosition() != 0) {

            String waiting = Integer.toString(waitTimeSpinner.getSelectedItemPosition() * 5);
            showLoader(R.string.empty);

            Application.getServerApi().updateStore(new UpdateStoreRequest(StorePrefs.getStoreInfo(getActivity()).getId(), waiting, openHourTextView.getText().toString(), closeHourTextView.getText().toString())).enqueue(new Callback<StoreResponse>(){

                @Override
                public void onResponse(Call<StoreResponse> call, Response<StoreResponse> response) {
                    hideLoader();
                    if (response.body().isError()) {
                        DialogUtils.showDialog(getActivity(), "Error", response.body().getMessage(), null, null);
                    } else {
                        StorePrefs.saveStoreInfo(getActivity(), response.body().getStoreInfo());
                    }
                }

                @Override
                public void onFailure(Call<StoreResponse> call, Throwable t) {
                    hideLoader();
                    if (t.getLocalizedMessage() != null) {
                        Log.d("StoreInfo", t.getLocalizedMessage());
                    } else {
                        Log.d("StoreInfo", "Unknown error");
                    }
                }
            });
        }
    }

    private void printData() {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.waitTimeTextView:
                waitTimeSpinner.performClick();
                break;
            case R.id.openHourTextView:
                openHourSpinner.performClick();
                break;
            case R.id.closeHourTextView:
                closeHourSpinner.performClick();
                break;
            case R.id.fromTextView:
                fromDatePickerDialog.show();
                break;
            case R.id.toTextView:
                toDatePickerDialog.show();
                break;
            case R.id.saveImageButton:
                saveStoreData();
                break;
            case R.id.processImageButton:
                printData();
                break;
        }
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
