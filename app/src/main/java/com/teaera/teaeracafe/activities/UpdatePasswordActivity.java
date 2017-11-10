package com.teaera.teaeracafe.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.teaera.teaeracafe.R;
import com.teaera.teaeracafe.app.Application;
import com.teaera.teaeracafe.net.Request.UpdatePasswordRequest;
import com.teaera.teaeracafe.net.Response.BaseResponse;
import com.teaera.teaeracafe.preference.UserPrefs;
import com.teaera.teaeracafe.utils.DialogUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText oldPassEditText;
    private EditText newPassEditText;
    private EditText confirmPassEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        init();
    }

    private void init() {

        oldPassEditText = (EditText) findViewById(R.id.oldPassEditText);
        newPassEditText = (EditText) findViewById(R.id.newPassEditText);
        confirmPassEditText = (EditText) findViewById(R.id.confirmPassEditText);

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

    }

    private void checkValidation() {
        String oldPass = oldPassEditText.getText().toString();
        String newPass = newPassEditText.getText().toString();
        String confirmPass = confirmPassEditText.getText().toString();

        if (oldPass.isEmpty()) {
            DialogUtils.showDialog(this, "Error", getString(R.string.enter_old_pass), null, null);
            return;
        }

        if (newPass.isEmpty()) {
            DialogUtils.showDialog(this, "Error", getString(R.string.enter_new_pass), null, null);
            return;
        }

        if (confirmPass.isEmpty()) {
            DialogUtils.showDialog(this, "Error", getString(R.string.enter_confirm_pass), null, null);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            DialogUtils.showDialog(this, "Error", getString(R.string.confirm_pass_error), null, null);
            return;
        }

        updatePassword(oldPass, newPass);
    }

    private void clearField() {
        oldPassEditText.setText("");
        newPassEditText.setText("");
        confirmPassEditText.setText("");
    }

    private void updatePassword(String oldPass, String newPass) {
        showLoader(R.string.empty);

        Application.getServerApi().updatePassword(new UpdatePasswordRequest(UserPrefs.getUserInfo(this).getId(), oldPass, newPass)).enqueue(new Callback<BaseResponse>(){

            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(UpdatePasswordActivity.this, "Error", response.body().getMessage(), null, null);
                    clearField();
                } else {
                    DialogUtils.showDialog(UpdatePasswordActivity.this, "Confirm", response.body().getMessage(), null, null);
                    clearField();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("Update Password", t.getLocalizedMessage());
                } else {
                    Log.d("Update Password", "Unknown error");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton:
                checkValidation();
                break;
            case R.id.backButton:
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;
        }
    }
}
