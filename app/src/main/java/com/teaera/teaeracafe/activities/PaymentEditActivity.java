package com.teaera.teaeracafe.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.teaera.teaeracafe.R;

public class PaymentEditActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_edit);

        init();
    }

    void init() {


        ImageButton deleteCardImageButton = (ImageButton) findViewById(R.id.deleteCardImageButton);
        deleteCardImageButton.setOnClickListener(this);

        ImageButton deletePaypalImageButton = (ImageButton) findViewById(R.id.deletePaypalImageButton);
        deletePaypalImageButton.setOnClickListener(this);


        ImageButton deleteWalletImageButton = (ImageButton) findViewById(R.id.deleteWalletImageButton);
        deleteWalletImageButton.setOnClickListener(this);

        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.deleteCardImageButton:
                break;

            case R.id.deletePaypalImageButton:
                break;

            case R.id.deleteWalletImageButton:
                break;

            case R.id.addButton:
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;

            case R.id.backButton:
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;
        }
    }

}
