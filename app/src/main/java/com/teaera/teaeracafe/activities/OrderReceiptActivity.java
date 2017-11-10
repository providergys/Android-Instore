package com.teaera.teaeracafe.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.teaera.teaeracafe.R;
import com.teaera.teaeracafe.adapter.CartListAdapter;
import com.teaera.teaeracafe.adapter.ItemListAdapter;
import com.teaera.teaeracafe.app.Application;
import com.teaera.teaeracafe.net.Model.LocationInfo;
import com.teaera.teaeracafe.net.Model.OrderDetailsInfo;
import com.teaera.teaeracafe.net.Model.OrderInfo;
import com.teaera.teaeracafe.net.Request.OrderDetailsRequest;
import com.teaera.teaeracafe.net.Request.RefundOrdersRequest;
import com.teaera.teaeracafe.net.Response.OrderDetailsResponse;
import com.teaera.teaeracafe.net.Response.RefundOrdersResponse;
import com.teaera.teaeracafe.preference.LocationPrefs;
import com.teaera.teaeracafe.utils.DialogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderReceiptActivity extends BaseActivity implements View.OnClickListener {

    private TextView orderNumberTextView;
    private TextView locationTextView;
    private TextView estimateTextView;
    private TextView receivedDateTextView;
    private TextView rewardTextView;
    private TextView subtotalTextView;
    private TextView creditTextView;
    private TextView taxTextView;
    private TextView totalTextView;
    private ListView itemListView;

    private ItemListAdapter itemListAdapter;

    private OrderInfo orderInfo;
    private ArrayList<OrderDetailsInfo> orderDetails = new ArrayList<OrderDetailsInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_receipt);

        orderInfo = (OrderInfo) getIntent().getSerializableExtra("order");
        init();
    }

    void init() {

        orderNumberTextView = (TextView) findViewById(R.id.orderNumberTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        estimateTextView = (TextView) findViewById(R.id.estimateTextView);
        receivedDateTextView = (TextView) findViewById(R.id.receivedDateTextView);
        rewardTextView = (TextView) findViewById(R.id.rewardTextView);
        subtotalTextView = (TextView) findViewById(R.id.subtotalTextView);
        creditTextView = (TextView) findViewById(R.id.creditTextView);
        taxTextView = (TextView) findViewById(R.id.taxTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);

        itemListView = (ListView) findViewById(R.id.itemListView);
        itemListAdapter = new ItemListAdapter(this, orderDetails);
        itemListView.setAdapter(itemListAdapter);


        Button refundButton = (Button) findViewById(R.id.refundButton);
        refundButton.setOnClickListener(this);

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        updateOrderInfo();
    }

    private void updateOrderInfo() {

        orderNumberTextView.setText("Order #" + orderInfo.getId());
        locationTextView.setText(getLocationName(orderInfo.getLocationId()));
        estimateTextView.setText("Estimate Pickup Time: " + getEstimatedTime(orderInfo.getTimeStamp(), orderInfo.getLocationId()));
        receivedDateTextView.setText(orderInfo.getTimeStamp());
        receivedDateTextView.setText(getReceivedDate(orderInfo.getTimeStamp()));
        totalTextView.setText("$" + orderInfo.getTotalPrice());
        subtotalTextView.setText("$" + orderInfo.getSubTotal());
        creditTextView.setText("-$" + orderInfo.getRewardsCredit());
        float tax = (Float.parseFloat(orderInfo.getSubTotal()) - Float.parseFloat(orderInfo.getRewardsCredit())) * Float.parseFloat(orderInfo.getTax());
        taxTextView.setText(String.format("$%.3f", tax));

        loadOrderDetails();

    }

    private void loadOrderDetails() {
        showLoader(R.string.empty);

        Application.getServerApi().getOrderDetails(new OrderDetailsRequest(orderInfo.getId())).enqueue(new Callback<OrderDetailsResponse>(){

            @Override
            public void onResponse(Call<OrderDetailsResponse> call, Response<OrderDetailsResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(OrderReceiptActivity.this, "Error", response.body().getMessage(), null, null);
                } else {
                    orderDetails = response.body().getOrders();
                    if (orderDetails.size() > 0) {
                        updateOrderDetails();
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderDetailsResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("Order Receipt", t.getLocalizedMessage());
                } else {
                    Log.d("Order Receipt", "Unknown error");
                }
            }
        });
    }

    private String getLocationName(String locationId) {
        ArrayList<LocationInfo> info = LocationPrefs.getLocations(this);
        for (int i=0; i<info.size(); i++) {
            if (info.get(i).getId().equals(locationId)) {
                return info.get(i).getLocationName();
            }
        }

        return "";
    }

    private String getEstimatedTime(String dateString, String locationId) {

        int waitingTime = 0;
        ArrayList<LocationInfo> info = LocationPrefs.getLocations(this);
        for (int i=0; i<info.size(); i++) {
            if (info.get(i).getId().equals(locationId)) {
                waitingTime = Integer.parseInt(info.get(i).getWaitingTime());
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat timeSdf = new SimpleDateFormat("hh:mm a");

        try {
            Date date = sdf.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, waitingTime);
            return  timeSdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String getReceivedDate(String dateString) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat newSdf = new SimpleDateFormat("MM/dd/yy, hh:mm a");

        try {
            Date date = sdf.parse(dateString);
            return  newSdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    private void updateOrderDetails() {
        itemListAdapter.updateCategories(orderDetails);
        itemListAdapter.notifyDataSetChanged();

        int rewards = 0;
        for (int i = 0; i<orderDetails.size(); i++) {
            if (orderDetails.get(i).getDrinkable().equals("1") && orderDetails.get(i).getRedeemed().equals("0") && orderDetails.get(i).getRefunded().equals("0")) {
                rewards = rewards + 1 * Integer.parseInt(orderDetails.get(i).getQuantity());
            }
        }
        rewardTextView.setText("+" + rewards);

    }

    private void refundOrders() {
        showLoader(R.string.empty);

        Application.getServerApi().refundOrders(new RefundOrdersRequest(orderInfo.getId(), orderInfo.getSubTotal(), orderInfo.getRewardsCredit(), orderInfo.getTotalPrice(), orderInfo.getTax(), orderDetails)).enqueue(new Callback<RefundOrdersResponse>(){

            @Override
            public void onResponse(Call<RefundOrdersResponse> call, Response<RefundOrdersResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(OrderReceiptActivity.this, "Error", response.body().getMessage(), null, null);
                } else {
                    DialogUtils.showDialog(OrderReceiptActivity.this, "Confirm", getString(R.string.success_refund_order), null, null);
                    orderInfo = response.body().getOrder();
                    orderDetails = response.body().getOrderDetails();
                    updateOrderInfo();
                }
            }

            @Override
            public void onFailure(Call<RefundOrdersResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("Order Receipt", t.getLocalizedMessage());
                } else {
                    Log.d("Order Receipt", "Unknown error");
                }
            }
        });
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.refundButton:
                refundOrders();
                break;

            case R.id.backButton:
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;
        }
    }

}
