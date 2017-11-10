package com.teaera.teaerastore.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teaera.teaerastore.R;
import com.teaera.teaerastore.adapter.DetailsOrderListAdapter;
import com.teaera.teaerastore.adapter.RefundOrderListAdapter;
import com.teaera.teaerastore.app.Application;
import com.teaera.teaerastore.net.Model.OrderInfo;
import com.teaera.teaerastore.net.Model.OrderItemInfo;
import com.teaera.teaerastore.net.Request.LoginRequest;
import com.teaera.teaerastore.net.Request.RefundOrderRequest;
import com.teaera.teaerastore.net.Response.BaseResponse;
import com.teaera.teaerastore.net.Response.RefundOrderResponse;
import com.teaera.teaerastore.preference.StorePrefs;
import com.teaera.teaerastore.utils.DialogUtils;

import java.sql.Struct;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class refundActivity extends BaseActivity implements View.OnClickListener, RefundOrderListAdapter.OnRefundCheckListener {

    private TextView titleTextView;
    private RelativeLayout authRelativeLayout;
    private EditText codeEditText;

    private TextView orderNumberTextView;
    private TextView dateTextView;
    private TextView nameTextView;
    private TextView emailTextView;

    private TextView rewardsTextView1;
    private TextView creditTextView1;
    private TextView taxTextView1;
    private TextView totalTextView1;

    private TextView rewardsTextView;
    private TextView creditTextView;
    private TextView subtotalTextView;
    private TextView taxTextView;
    private TextView totalTextView;

    private RelativeLayout confirmRelativeLayout;
    private TextView confirmDateTextView;
    private Button processButton;

    private ListView orderListView;
    private RefundOrderListAdapter refundOrderListAdapter;
    private ArrayList<Integer> checkList = new ArrayList<Integer>();
    private ArrayList<Integer> refundList = new ArrayList<Integer>();
    private OrderInfo orderInfo;
    private int selectedItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);
        orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");

        for (int i=0; i<orderInfo.getDetails().size(); i++) {
            if (orderInfo.getDetails().get(i).getRefunded().equals("1")) {
                checkList.add(1);
            } else {
                checkList.add(0);
            }
        }
        init();
    }

    private void init() {

        titleTextView = findViewById(R.id.titleTextView);

        authRelativeLayout = findViewById(R.id.authRelativeLayout);
        authRelativeLayout.setVisibility(View.GONE);
        codeEditText = findViewById(R.id.codeEditText);

        orderNumberTextView = findViewById(R.id.orderNumberTextView);
        dateTextView = findViewById(R.id.dateTextView);
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);

        rewardsTextView1 = findViewById(R.id.rewardsTextView1);
        creditTextView1 = findViewById(R.id.creditTextView1);
        taxTextView1 = findViewById(R.id.taxTextView1);
        totalTextView1 = findViewById(R.id.totalTextView1);

        rewardsTextView = findViewById(R.id.rewardsTextView);
        creditTextView = findViewById(R.id.creditTextView);
        subtotalTextView = findViewById(R.id.subtotalTextView);
        taxTextView = findViewById(R.id.taxTextView);
        totalTextView = findViewById(R.id.totalTextView);

        confirmRelativeLayout = findViewById(R.id.confirmRelativeLayout);
        confirmDateTextView = findViewById(R.id.confirmDateTextView);
        confirmDateTextView.setVisibility(View.GONE);

        int rewards = 0;
        if (orderInfo.getDetails().size() > 0) {
            for (int i = 0; i<orderInfo.getDetails().size(); i++) {
                if (orderInfo.getDetails().get(i).getDrinkable().equals("1") && orderInfo.getDetails().get(i).getRedeemed().equals("0")) {
                    rewards = rewards + 1 * Integer.parseInt(orderInfo.getDetails().get(i).getQuantity());
                }
            }
        }
        rewardsTextView1.setText("+" + rewards + " Star");
        creditTextView1.setText("-$" + orderInfo.getRewardsCredit());
        taxTextView1.setText(String.format("$%.3f", (Float.parseFloat(orderInfo.getSubTotal()) - Float.parseFloat(orderInfo.getRewardsCredit())) * Float.parseFloat(orderInfo.getTax())));
        totalTextView1.setText("$" + orderInfo.getTotalPrice());

        orderNumberTextView.setText("ORDER: #" + orderInfo.getId());
        dateTextView.setText(orderInfo.getTimestamp());
        nameTextView.setText(orderInfo.getUserName());
        emailTextView.setText(orderInfo.getEmail());


        orderListView = findViewById(R.id.orderListView);
        refundOrderListAdapter = new RefundOrderListAdapter(this, orderInfo.getDetails(), checkList);
        orderListView.setAdapter(refundOrderListAdapter);

        ImageButton closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(this);

        ImageButton authImageButton = findViewById(R.id.authImageButton);
        authImageButton.setOnClickListener(this);

        processButton = findViewById(R.id.processButton);
        processButton.setOnClickListener(this);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
    }

    private void updateRefundInfo() {
        int rewards = 0;
        float redeemCredit = 0;
        float subtotal = 0;
        float tax = 0;

        if (orderInfo.getDetails().size() > 0) {
            for (int i = 0; i<orderInfo.getDetails().size(); i++) {
                if (orderInfo.getDetails().get(i).getDrinkable().equals("1") && checkList.get(i) == 2) {
                    if (orderInfo.getDetails().get(i).getRedeemed().equals("0")) {
                        // calculate rewards
                        rewards = rewards + 1 * Integer.parseInt(orderInfo.getDetails().get(i).getQuantity());
                    } else {
                        // calculate redeemed free drink
                        redeemCredit = redeemCredit + Float.parseFloat(orderInfo.getDetails().get(i).getPrice()) * Integer.parseInt(orderInfo.getDetails().get(i).getQuantity());
                    }
                }

                if (orderInfo.getDetails().get(i).getRedeemed().equals("0") && checkList.get(i) == 2) {
                    subtotal = subtotal + Float.parseFloat(orderInfo.getDetails().get(i).getPrice()) * Integer.parseInt(orderInfo.getDetails().get(i).getQuantity());
                }
            }
        }

        tax = (subtotal-redeemCredit) * Float.parseFloat(orderInfo.getTax());
        rewardsTextView.setText("-" + rewards + " Star");
        creditTextView.setText(String.format("-$%.3f", redeemCredit));
        subtotalTextView.setText(String.format("-$%.3f", subtotal));
        taxTextView.setText(String.format("-$%.3f", tax));
        totalTextView.setText(String.format("-$%.3f", subtotal+tax-redeemCredit));
    }

    private void auth(String pass) {
        showLoader(R.string.empty);

        Application.getServerApi().authorizeCode(new LoginRequest(StorePrefs.getStoreInfo(refundActivity.this).getId(), pass)).enqueue(new Callback<BaseResponse>(){

            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    refundOrderListAdapter.notifyDataSetChanged();
                    DialogUtils.showDialog(refundActivity.this, "Error", response.body().getMessage(), null, null);
                } else {
                    authRelativeLayout.setVisibility(View.GONE);
                    checkList.set(selectedItem, 2);
                    refundOrderListAdapter.updateCheckList(checkList);
                    refundOrderListAdapter.notifyDataSetChanged();
                    updateRefundInfo();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideLoader();
                refundOrderListAdapter.notifyDataSetChanged();
                if (t.getLocalizedMessage() != null) {
                    Log.d("Refund", t.getLocalizedMessage());
                } else {
                    Log.d("Refund", "Unknown error");
                }
            }
        });

    }


    private void refund() {
        ArrayList<OrderItemInfo> refundItems = new ArrayList<OrderItemInfo>();
        for (int i=0; i<checkList.size(); i++) {
            if (checkList.get(i) == 2) {
                refundItems.add(orderInfo.getDetails().get(i));
            }
        }

        if (refundItems.size() > 0) {
            showLoader(R.string.empty);
            Application.getServerApi().refundOrder(new RefundOrderRequest(orderInfo.getId(), orderInfo.getSubTotal(), orderInfo.getRewardsCredit(), orderInfo.getTax(), orderInfo.getTotalPrice(), refundItems)).enqueue(new Callback<RefundOrderResponse>(){

                @Override
                public void onResponse(Call<RefundOrderResponse> call, Response<RefundOrderResponse> response) {
                    hideLoader();
                    if (response.body().isError()) {
                        DialogUtils.showDialog(refundActivity.this, "Error", response.body().getMessage(), null, null);
                    } else {
                        orderInfo = response.body().getOrder();
                        updateRefundInfo();
                        processButton.setVisibility(View.GONE);
                        confirmRelativeLayout.setVisibility(View.VISIBLE);
                        confirmDateTextView.setText(orderInfo.getTimestamp());
                    }
                }

                @Override
                public void onFailure(Call<RefundOrderResponse> call, Throwable t) {
                    hideLoader();
                    if (t.getLocalizedMessage() != null) {
                        Log.d("Refund", t.getLocalizedMessage());
                    } else {
                        Log.d("Refund", "Unknown error");
                    }
                }
            });

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;

            case R.id.closeButton:
                authRelativeLayout.setVisibility(View.GONE);
                refundOrderListAdapter.notifyDataSetChanged();
                break;

            case R.id.authImageButton:
                String pass = codeEditText.getText().toString();
                if (pass.isEmpty()) {
                    DialogUtils.showDialog(this, "Error", getString(R.string.enter_authcode), null, null);
                } else {
                    auth(pass);
                    codeEditText.setText("");
                }
                break;

            case R.id.processButton:
                refund();
                break;
        }
    }

    @Override
    public void onRefundCheckClicked(int position) {
        selectedItem = position;
        authRelativeLayout.setVisibility(View.VISIBLE);

    }
}
