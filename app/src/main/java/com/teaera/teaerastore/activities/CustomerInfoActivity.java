package com.teaera.teaerastore.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.teaera.teaerastore.R;
import com.teaera.teaerastore.adapter.CustomerOrderListAdapter;
import com.teaera.teaerastore.adapter.OrderListAdapter;
import com.teaera.teaerastore.app.Application;
import com.teaera.teaerastore.net.Model.OrderInfo;
import com.teaera.teaerastore.net.Request.AddRewardsRequest;
import com.teaera.teaerastore.net.Request.GetCustomerRequest;
import com.teaera.teaerastore.net.Response.BaseResponse;
import com.teaera.teaerastore.net.Response.GetOrdersResponse;
import com.teaera.teaerastore.preference.StorePrefs;
import com.teaera.teaerastore.utils.DialogUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerInfoActivity extends BaseActivity implements View.OnClickListener, CustomerOrderListAdapter.OnOrderItemClickListener {

    private RelativeLayout authRelativeLayout;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView rewardTextView;

    private CustomerOrderListAdapter orderListAdapter;
    private ListView orderListView;

    private TextView addRewardTextView;
    private Spinner rewardSpinner;
    private EditText passwordEditText;

    private ArrayList<OrderInfo> orders = new ArrayList<OrderInfo>();
    private String userId;
    int pageNumber = 1;
    int rewardStars = 0;

    private String[] rewards = {"- None -", "1 Stars", "2 Stars", "3 Stars", "4 Stars", "5 Stars", "6 Stars", "7 Stars", "8 Stars", "9 Stars", "10 Stars"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        userId = getIntent().getStringExtra("userId");
        if (!userId.isEmpty()) {
            getCustomerInfo();
        }

        init();
    }

    private void init() {

        authRelativeLayout = findViewById(R.id.authRelativeLayout);
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        orderListView = findViewById(R.id.orderListView);
        rewardTextView = findViewById(R.id.rewardTextView);
        rewardSpinner = findViewById(R.id.rewardSpinner);
        addRewardTextView = findViewById(R.id.addRewardTextView);
        passwordEditText = findViewById(R.id.passwordEditText);

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,rewards);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rewardSpinner.setAdapter(adapter);
        rewardSpinner.setSelection(0);

        addRewardTextView.setText(rewardSpinner.getSelectedItem().toString());
        addRewardTextView.setOnClickListener(this);

        rewardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                addRewardTextView.setText(rewardSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ImageButton closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(this);

        ImageButton loginImageButton = findViewById(R.id.loginImageButton);
        loginImageButton.setOnClickListener(this);

        Button addRewardsButton = findViewById(R.id.addRewardsButton);
        addRewardsButton.setOnClickListener(this);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
    }

    private void updateCustomerInfo() {
        nameTextView.setText(orders.get(0).getUserName());
        emailTextView.setText(orders.get(0).getEmail());
        rewardStars = orders.get(0).getRewardStar();
        rewardTextView.setText(Integer.toString(rewardStars));

        orderListAdapter = new CustomerOrderListAdapter(this, orders, (CustomerOrderListAdapter.OnOrderItemClickListener) this);
        orderListView.setAdapter(orderListAdapter);
    }

    private void getCustomerInfo() {
        if (userId.isEmpty())
            return;

        showLoader(R.string.empty);

        Application.getServerApi().getCustomerInfo(new GetCustomerRequest(userId, StorePrefs.getStoreInfo(CustomerInfoActivity.this).getId(), pageNumber)).enqueue(new Callback<GetOrdersResponse>(){

            @Override
            public void onResponse(Call<GetOrdersResponse> call, Response<GetOrdersResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(CustomerInfoActivity.this, "Error", response.body().getMessage(), null, null);
                } else {
                    orders = response.body().getOrders();
                    pageNumber = Integer.parseInt(response.body().getPageNumber());
                    updateCustomerInfo();
                }
            }

            @Override
            public void onFailure(Call<GetOrdersResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("CustomerInfo", t.getLocalizedMessage());
                } else {
                    Log.d("CustomerInfo", "Unknown error");
                }
            }
        });
    }

    private void addRewards(String pass) {
        showLoader(R.string.empty);

        Application.getServerApi().addRewards(new AddRewardsRequest(StorePrefs.getStoreInfo(CustomerInfoActivity.this).getId(), userId, pass, Integer.toString(rewardSpinner.getSelectedItemPosition()))).enqueue(new Callback<BaseResponse>(){

            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(CustomerInfoActivity.this, "Error", response.body().getMessage(), null, null);
                } else {
                    rewardStars = rewardStars + rewardSpinner.getSelectedItemPosition();
                    rewardTextView.setText(Integer.toString(rewardStars));

                    rewardSpinner.setSelection(0);
                    passwordEditText.setText("");
                    authRelativeLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("CustomerInfo", t.getLocalizedMessage());
                } else {
                    Log.d("CustomerInfo", "Unknown error");
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;

            case R.id.addRewardsButton:
                authRelativeLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.closeButton:
                rewardSpinner.setSelection(0);
                passwordEditText.setText("");
                authRelativeLayout.setVisibility(View.GONE);
                break;

            case R.id.addRewardTextView:
                rewardSpinner.performClick();
                break;

            case R.id.loginImageButton:
                String pass = passwordEditText.getText().toString();
                if (pass.isEmpty()) {
                    DialogUtils.showDialog(this, "Error", getString(R.string.enter_authcode), null, null);
                    return;
                }

                if (rewardSpinner.getSelectedItemPosition() == 0) {
                    DialogUtils.showDialog(this, "Error", getString(R.string.choose_rewards), null, null);
                    return;
                }

                addRewards(pass);
                break;

        }
    }

    @Override
    public void onOrderItemClickListener(OrderInfo info) {

    }
}
