package com.teaera.teaerastore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.teaera.teaerastore.R;
import com.teaera.teaerastore.app.Application;
import com.teaera.teaerastore.net.Model.StoreInfo;
import com.teaera.teaerastore.net.Request.LoginRequest;
import com.teaera.teaerastore.net.Response.GetStoresResponse;
import com.teaera.teaerastore.net.Response.StoreResponse;
import com.teaera.teaerastore.preference.StorePrefs;
import com.teaera.teaerastore.utils.DialogUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BaseActivity implements View.OnClickListener{

    private Spinner locationSpinner;
    private TextView locationTextView;
    private EditText passwordEditText;

    private ArrayList<StoreInfo> stores = new ArrayList<StoreInfo>();
    private ArrayList<String> storeNames = new ArrayList<String>();
    private String selectedLocationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        stores = (ArrayList<StoreInfo>) getIntent().getSerializableExtra("stores");

        init();

        if (stores.size() == 0) {
            getStores();
        } else {
            updateStores();
        }
    }

    private void init() {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        passwordEditText = findViewById(R.id.passwordEditText);
        locationTextView = findViewById(R.id.locationTextView1);
        locationSpinner = findViewById(R.id.locationSpinner);

        ImageButton loginImageButton = findViewById(R.id.loginImageButton);
        loginImageButton.setOnClickListener(this);
    }

    private void updateStores() {

        for (int i = 0; i < stores.size(); i++) {
            storeNames.add(stores.get(i).getName());
        }

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,storeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);
        locationSpinner.setSelection(0);
        selectedLocationId = stores.get(0).getId();

        locationTextView.setText(locationSpinner.getSelectedItem().toString());
        locationTextView.setOnClickListener(this);

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                locationTextView.setText(locationSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getStores() {
        showLoader(R.string.empty);

        Application.getServerApi().getStores().enqueue(new Callback<GetStoresResponse>(){

            @Override
            public void onResponse(Call<GetStoresResponse> call, Response<GetStoresResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(SignInActivity.this, "Error", response.body().getMessage(), null, null);
                } else {
                    stores = response.body().getStores();
                    updateStores();
                }
            }

            @Override
            public void onFailure(Call<GetStoresResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("SignIn", t.getLocalizedMessage());
                } else {
                    Log.d("SignIn", "Unknown error");
                }
            }
        });
    }

    private void loginToServer(String pass) {
        showLoader(R.string.empty);

        Application.getServerApi().login(new LoginRequest(selectedLocationId, pass)).enqueue(new Callback<StoreResponse>(){

            @Override
            public void onResponse(Call<StoreResponse> call, Response<StoreResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(SignInActivity.this, "Error", response.body().getMessage(), null, null);
                } else {
                    StorePrefs.setLoggedIn(SignInActivity.this, true);
                    StorePrefs.saveStoreInfo(SignInActivity.this, response.body().getStoreInfo());
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<StoreResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("SignIn", t.getLocalizedMessage());
                } else {
                    Log.d("SignIn", "Unknown error");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.loginImageButton:
                String pass = passwordEditText.getText().toString();
                if (pass.isEmpty()) {
                    DialogUtils.showDialog(this, "Error", getString(R.string.enter_password), null, null);
                } else {
                    loginToServer(pass);
                }

                break;
            case R.id.locationTextView1:
                locationSpinner.performClick();
                break;
        }
    }
}
