package com.teaera.teaerastore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.teaera.teaerastore.R;
import com.teaera.teaerastore.utils.DialogUtils;

public class SignInActivity extends BaseActivity implements View.OnClickListener{

    private Spinner locationSpinner;
    private TextView locationTextView;
    private EditText passwordEditText;


    String[] locationNames = {"Cupertino, CA", "SanJose, CA"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();

    }

    private void init() {

        passwordEditText = findViewById(R.id.passwordEditText);
        locationTextView = findViewById(R.id.locationTextView1);
        locationSpinner = findViewById(R.id.locationSpinner);


        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,locationNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);
        locationSpinner.setSelection(0);
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

        ImageButton loginImageButton = findViewById(R.id.loginImageButton);
        loginImageButton.setOnClickListener(this);

    }

    private void loginToServer(String pass) {

        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        finish();


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
