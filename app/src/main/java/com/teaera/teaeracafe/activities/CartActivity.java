package com.teaera.teaeracafe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.teaera.teaeracafe.R;
import com.teaera.teaeracafe.adapter.CartListAdapter;
import com.teaera.teaeracafe.app.Application;
import com.teaera.teaeracafe.net.Model.CartInfo;
import com.teaera.teaeracafe.net.Model.LocationInfo;
import com.teaera.teaeracafe.net.Request.OrderRequest;
import com.teaera.teaeracafe.net.Response.BaseResponse;
import com.teaera.teaeracafe.preference.CartPrefs;
import com.teaera.teaeracafe.preference.LocationPrefs;
import com.teaera.teaeracafe.preference.UserPrefs;
import com.teaera.teaeracafe.utils.DialogUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends BaseActivity implements View.OnClickListener, CartListAdapter.OnQuantityChangeListener {

    private Spinner locationSpinner;
    private TextView locationTextView;
    private TextView redeemTextView;
    private TextView rewardTextView;
    private ListView cartListView;
    private TextView subtotalTextView;
    private TextView creditTextView;
    private TextView taxTextView;
    private TextView totalTextView;
    private TextView estimateTextView;

    private CartListAdapter cartListAdapter;


    private ArrayList<LocationInfo> locations;
    private ArrayList<String> locationNames = new ArrayList<String>();
    private ArrayList<String> waitingTimes = new ArrayList<String>();
    private ArrayList<CartInfo> carts = new ArrayList<CartInfo>();
    private ArrayList<CartInfo> sortedCarts = new ArrayList<CartInfo>();
    private String selectedLocationID;

    private int redeems = 0;
    private int rewards = 0;
    private float totalPrice;
    private float subTotal = 0;
    private float redeemCredit = 0;
    private float storeTax;
    private float tax = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        locations = LocationPrefs.getLocations(this);
        for (int i = 0; i < locations.size(); i++) {
            locationNames.add(locations.get(i).getLocationName());
            waitingTimes.add(locations.get(i).getWaitingTime());
        }
        carts = CartPrefs.getCarts(this);

        init();
    }

    void init() {

        redeemTextView = (TextView) findViewById(R.id.redeemTextView);
        rewardTextView = (TextView) findViewById(R.id.rewardTextView);
        subtotalTextView = (TextView) findViewById(R.id.subtotalTextView);
        creditTextView = (TextView) findViewById(R.id.creditTextView);
        taxTextView = (TextView) findViewById(R.id.taxTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);
        estimateTextView = (TextView) findViewById(R.id.estimateTextView);

        redeemTextView.setText(Integer.toString(redeems));
        rewardTextView.setText(String.format("+%d", rewards));

        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                locationTextView.setText(locationSpinner.getSelectedItem().toString());
                updateCartInfo(position);
                cartListAdapter.updateCategories(sortedCarts);
                cartListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,locationNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);
        locationSpinner.setSelection(0);
        updateCartInfo(0);

        locationTextView = (TextView) findViewById(R.id.locationTextView1);
        locationTextView.setText(locationSpinner.getSelectedItem().toString());

        cartListView = (ListView) findViewById(R.id.cartListView);
        cartListAdapter = new CartListAdapter(this, sortedCarts);
        cartListView.setAdapter(cartListAdapter);

        Button orderButton = (Button) findViewById(R.id.orderButton);
        orderButton.setOnClickListener(this);
        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
        Button redeemPlusButton = (Button) findViewById(R.id.redeemPlusButton);
        redeemPlusButton.setOnClickListener(this);
        Button redeemMinusButton = (Button) findViewById(R.id.redeemMinusButton);
        redeemMinusButton.setOnClickListener(this);
        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
    }


    private void updateCartInfo(int index) {

        sortedCarts.clear();
        selectedLocationID = locations.get(index).getId();
        storeTax = Float.parseFloat(locations.get(index).getTax());
        estimateTextView.setText("Current wait time: " + waitingTimes.get(index) + "mins");

        if (carts != null) {
            for (int i = 0; i<carts.size(); i++) {
                if (carts.get(i).getLocationId().equals(selectedLocationID)) {
                    sortedCarts.add(carts.get(i));
                }
            }
        }

        calculateOrder();
    }

    private void calculateOrder() {

        totalPrice = 0;
        rewards = 0;
        redeemCredit = 0;
        subTotal = 0;
        tax = 0;

        for (int i=0; i<sortedCarts.size(); i++) {
            if (sortedCarts.get(i).getDrinkable().equals("1") && sortedCarts.get(i).getRedeemed().equals("0")) {
                // calculate rewards
                rewards = rewards + 1 * Integer.parseInt(sortedCarts.get(i).getQuantity());
            }

            if (sortedCarts.get(i).getDrinkable().equals("1") && sortedCarts.get(i).getRedeemed().equals("1")) {
                // calculate redeemed free drink
                redeemCredit = redeemCredit + Float.parseFloat(sortedCarts.get(i).getPrice()) * Integer.parseInt(sortedCarts.get(i).getQuantity());
            }

            if (sortedCarts.get(i).getRedeemed().equals("0")) {
                subTotal = subTotal + Float.parseFloat(sortedCarts.get(i).getPrice()) * Integer.parseInt(sortedCarts.get(i).getQuantity());
            }
        }


        tax = (subTotal-redeemCredit) * storeTax;
        totalPrice = subTotal+tax-redeemCredit;
        subtotalTextView.setText(String.format("$%.2f", subTotal));
        creditTextView.setText(String.format("-$%.2f", redeemCredit));
        taxTextView.setText(String.format("$%.2f", tax));
        totalTextView.setText(String.format("$%.2f", totalPrice));

    }

    private void removeOrderCarts() {

        for (int i = 0; i<sortedCarts.size(); i++) {
            for (int j=0; j<carts.size(); j++) {
                if (carts.get(j).getCartIndex() == sortedCarts.get(i).getCartIndex()) {
                    carts.remove(j);
                    break;
                }
            }
        }

        for (int i = 0; i < carts.size(); i++) {
            carts.get(i).setCartIndex(i);
        }

        CartPrefs.clearCarts(CartActivity.this);
        CartPrefs.setCarts(CartActivity.this, carts);
        carts = CartPrefs.getCarts(CartActivity.this);

        updateCartInfo(locationSpinner.getSelectedItemPosition());
        cartListAdapter.updateCategories(sortedCarts);
        cartListAdapter.notifyDataSetChanged();


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.orderButton:
                if (sortedCarts.size() == 0) {
                    DialogUtils.showDialog(CartActivity.this, "Error", getString(R.string.empty_order), null, null);
                    return;
                }

                sendOrderToServer();
                break;

            case R.id.addButton:
                // Move to first page
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;

            case R.id.redeemMinusButton:
                if (redeems == 0)
                    return;

                redeems -= 1;
                redeemTextView.setText(Integer.toString(redeems));
                break;

            case R.id.redeemPlusButton:
                redeems += 1;
                redeemTextView.setText(Integer.toString(redeems));
                break;

            case R.id.backButton:
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;
        }
    }

    private void sendOrderToServer() {

        showLoader(R.string.empty);
        Application.getServerApi().placeOrderToServer(new OrderRequest(UserPrefs.getUserInfo(this).getId(), selectedLocationID, Float.toString(subTotal), Float.toString(redeemCredit), Float.toString(storeTax), Float.toString(totalPrice), Integer.toString(rewards), sortedCarts)).enqueue(new Callback<BaseResponse>(){

            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(CartActivity.this, "Error", response.body().getMessage(), null, null);
                } else {
                    DialogUtils.showDialog(CartActivity.this, "Confirm", getString(R.string.success_order), null, null);
                    removeOrderCarts();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("Cart", t.getLocalizedMessage());
                } else {
                    Log.d("Cart", "Unknown error");
                }
            }
        });

    }

    @Override
    public void onPlusButtonClicked(int cartIndex) {
        for (int i = 0; i<carts.size(); i ++) {
            if (carts.get(i).getCartIndex() == cartIndex) {
                int num = Integer.parseInt(carts.get(i).getQuantity());
                carts.get(i).setQuantity(Integer.toString(num+1));
            }
        }

        updateCartInfo(locationSpinner.getSelectedItemPosition());
        cartListAdapter.updateCategories(sortedCarts);
        cartListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onMinusButtonClicked(int cartIndex) {
        for (int i = 0; i<carts.size(); i ++) {
            if (carts.get(i).getCartIndex() == cartIndex) {
                int num = Integer.parseInt(carts.get(i).getQuantity());
                carts.get(i).setQuantity(Integer.toString(num-1));
            }
        }

        updateCartInfo(locationSpinner.getSelectedItemPosition());
        cartListAdapter.updateCategories(sortedCarts);
        cartListAdapter.notifyDataSetChanged();
    }
}
