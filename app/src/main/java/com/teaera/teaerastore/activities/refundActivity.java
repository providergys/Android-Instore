package com.teaera.teaerastore.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teaera.teaerastore.R;
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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class refundActivity extends BaseActivity implements View.OnClickListener, RefundOrderListAdapter.OnRefundCheckListener {

    private static final String TAG = "refundActivity";
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
    private Button selectAllButton;

    private ListView orderListView;
    private RefundOrderListAdapter refundOrderListAdapter;
    private ArrayList<Integer> checkList = new ArrayList<Integer>();
    private ArrayList<Integer> quantityList = new ArrayList<Integer>();

    private int rewards             = 0;
    private String totalPriceStr    = "0.00";
    private String subTotalStr      = "0.00";
    private String redeemCreditStr  = "0.00";
    private String taxAmountStr     = "0.00";
    private OrderInfo orderInfo;
    private int selectedItem        = 0;
    private boolean checkAll = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);
        orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");

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
        confirmRelativeLayout.setVisibility(View.GONE);
        confirmDateTextView = findViewById(R.id.confirmDateTextView);

        updateOriginalOrderInfo();

        orderListView = findViewById(R.id.orderListView);
        refundOrderListAdapter = new RefundOrderListAdapter(this, orderInfo.getDetails(), checkList);
        orderListView.setAdapter(refundOrderListAdapter);

        ImageButton closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(this);

        ImageButton authImageButton = findViewById(R.id.authImageButton);
        authImageButton.setOnClickListener(this);

        processButton = findViewById(R.id.processButton);
        processButton.setOnClickListener(this);

        selectAllButton = findViewById(R.id.selectAllButton);
        selectAllButton.setOnClickListener(this);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
    }

    private void updateOriginalOrderInfo() {

        this.checkAll = false;
        for (int i=0; i<orderInfo.getDetails().size(); i++) {
            if (orderInfo.getDetails().get(i).getRefunded().equals("1")) {
                checkList.add(1);
            } else {
                checkList.add(0);
            }

            int quantity = Integer.parseInt(orderInfo.getDetails().get(i).getQuantity()) - Integer.parseInt(orderInfo.getDetails().get(i).getRefundQuantity());
            quantityList.add(quantity);
            orderInfo.getDetails().get(i).setQuantity(Integer.toString(quantity));
        }

        rewardsTextView1.setText("+" + orderInfo.getRewards() + " Star");
        creditTextView1.setText("-$" + orderInfo.getRewardsCredit());
        taxTextView1.setText(String.format("$%.2f", (Float.parseFloat(orderInfo.getSubTotal()) - Float.parseFloat(orderInfo.getRewardsCredit())) * Float.parseFloat(orderInfo.getTax())));
        totalTextView1.setText("$" + orderInfo.getTotalPrice());

        int orderId = Integer.parseInt(orderInfo.getId());
        orderNumberTextView.setText(String.format("ORDER: #%05d", orderId));
        dateTextView.setText(getReceivedDate(orderInfo.getTimestamp()));
        nameTextView.setText(orderInfo.getUserName());
        emailTextView.setText(orderInfo.getEmail());
    }

    private void updateRefundInfo() {
        rewards = 0;
        float redeemCredit = 0;
        float subtotal = 0;
        float tax = 0;

        if (orderInfo.getDetails().size() > 0) {
            for (int i = 0; i<orderInfo.getDetails().size(); i++) {
                if (orderInfo.getDetails().get(i).getDrinkable().equals("1") && checkList.get(i) == 2) {
                    if (orderInfo.getDetails().get(i).getRedeemed().equals("0")) {
                        // calculate rewards
                        rewards = rewards + Integer.parseInt(orderInfo.getDetails().get(i).getRewards()) * Integer.parseInt(orderInfo.getDetails().get(i).getQuantity());
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
        subTotalStr = String.format("%.2f", subtotal);
        taxAmountStr= String.format("%.2f", tax);
        redeemCreditStr = String.format("%.2f", redeemCredit);
        totalPriceStr = String.format("%.2f", subtotal+tax-redeemCredit);

        subtotalTextView.setText("-$" + subTotalStr);
        taxTextView.setText("-$" + taxAmountStr);
        creditTextView.setText("-$" + redeemCreditStr);
        totalTextView.setText("-$" + totalPriceStr);
        rewardsTextView.setText("-" + rewards + " Star");
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
                    if (checkAll) {
                        for (int i=0; i<checkList.size(); i++) {
                            if (checkList.get(i) == 0) {
                                checkList.set(i, 2);
                            }
                        }
                        checkAll = false;
                    } else {
                        checkList.set(selectedItem, 2);
                    }

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

            String redeem = "0";
            if (rewards>9) {
                int redeemInt = Integer.parseInt(String.valueOf(rewards / 10));
                redeem = String.valueOf(redeemInt);
            }

            Application.getServerApi().refundOrder("text/plain",new RefundOrderRequest(orderInfo.getUserId(), orderInfo.getId(), subTotalStr, redeemCreditStr, orderInfo.getTax(), taxAmountStr, totalPriceStr, rewards, redeem, refundItems)).enqueue(new Callback<RefundOrderResponse>(){
                @Override
                public void onResponse(Call<RefundOrderResponse> call, Response<RefundOrderResponse> response) {
                    hideLoader();

                    if (response.body().isError()) {
                        DialogUtils.showDialog(refundActivity.this, "Error", response.body().getMessage(), null, null);
                    } else {
                        orderInfo = response.body().getOrder();
                        updateOriginalOrderInfo();
                        updateRefundInfo();
                        refundOrderListAdapter.updateOrderItems(orderInfo.getDetails());
                        refundOrderListAdapter.notifyDataSetChanged();
                        processButton.setVisibility(View.INVISIBLE);
                        confirmRelativeLayout.setVisibility(View.VISIBLE);
                        confirmDateTextView.setText(getReceivedDate(orderInfo.getTimestamp()));
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
                this.checkAll = false;
                authRelativeLayout.setVisibility(View.GONE);
                refundOrderListAdapter.notifyDataSetChanged();
                hideKeyboard();
                break;

            case R.id.authImageButton:
                hideKeyboard();
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

            case R.id.selectAllButton:
                this.checkAll = true;
                authRelativeLayout.setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    public void onRefundCheckClicked(int position) {
        selectedItem = position;
        authRelativeLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void onUnRefundCheckClicked(int position) {
        checkList.set(position, 0);
        refundOrderListAdapter.updateCheckList(checkList);
        refundOrderListAdapter.notifyDataSetChanged();
        updateRefundInfo();
    }

    @Override
    public void onPlusClicked(int position) {
        int refundQuantity = Integer.parseInt(orderInfo.getDetails().get(position).getQuantity());
        if (refundQuantity < quantityList.get(position)) {
            refundQuantity ++;
            orderInfo.getDetails().get(position).setQuantity(Integer.toString(refundQuantity));
            refundOrderListAdapter.updateOrderItems(orderInfo.getDetails());
            refundOrderListAdapter.notifyDataSetChanged();
            updateRefundInfo();
        }
    }

    @Override
    public void onMinusClicked(int position) {
        int refundQuantity = Integer.parseInt(orderInfo.getDetails().get(position).getQuantity());
        if (refundQuantity > 0) {
            refundQuantity --;
            orderInfo.getDetails().get(position).setQuantity(Integer.toString(refundQuantity));
            refundOrderListAdapter.updateOrderItems(orderInfo.getDetails());
            refundOrderListAdapter.notifyDataSetChanged();
            updateRefundInfo();
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
