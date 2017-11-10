package com.teaera.teaeracafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.teaera.teaeracafe.R;
import com.teaera.teaeracafe.app.Application;
import com.teaera.teaeracafe.net.Request.ForgotPasswordRequest;
import com.teaera.teaeracafe.net.Response.BaseResponse;
import com.teaera.teaeracafe.utils.DialogUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotActivity extends BaseActivity implements View.OnClickListener{

    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        emailEditText = (EditText) findViewById(R.id.emailEditText);

        ImageButton forgotImageButton = (ImageButton) findViewById(R.id.forgotImageButton);
        forgotImageButton.setOnClickListener(this);

        ImageButton backImageButton = (ImageButton) findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.forgotImageButton:
                requestForgotPassword();
                break;

            case R.id.backImageButton:
                Intent intent = new Intent(ForgotActivity.this, SignInActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                finish();
                break;
        }
    }

    private void requestForgotPassword() {

        String email = emailEditText.getText().toString();

        if (email.isEmpty()) {
            DialogUtils.showDialog(this, "Error", getString(R.string.enter_email), null, null);
            return;
        }

        showLoader(R.string.empty);

        Application.getServerApi().forgotPassword(new ForgotPasswordRequest(email)).enqueue(new Callback<BaseResponse>(){

            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(ForgotActivity.this, "Error", response.body().getMessage(), null, null);
                } else {
                    DialogUtils.showDialog(ForgotActivity.this, "Alert", response.body().getMessage(), null, null);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("Forgot", t.getLocalizedMessage());
                } else {
                    Log.d("Forgot", "Unknown error");
                }
            }
        });

    }
}
