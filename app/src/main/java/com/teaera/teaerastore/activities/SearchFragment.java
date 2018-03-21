package com.teaera.teaerastore.activities;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teaera.teaerastore.R;
import com.teaera.teaerastore.adapter.DetailsOrderListAdapter;
import com.teaera.teaerastore.adapter.OrderListAdapter;
import com.teaera.teaerastore.app.Application;
import com.teaera.teaerastore.net.Model.OrderInfo;
import com.teaera.teaerastore.net.Request.SearchOrderRequest;
import com.teaera.teaerastore.net.Request.UpdateOrderRequest;
import com.teaera.teaerastore.net.Response.BaseResponse;
import com.teaera.teaerastore.net.Response.SearchOrdersResponse;
import com.teaera.teaerastore.preference.StorePrefs;
import com.teaera.teaerastore.utils.DialogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements View.OnClickListener, OrderListAdapter.OnOrderItemClickListener {

    private OrderListAdapter orderListAdapter;
    private ListView orderListView;
    private DetailsOrderListAdapter detailsOrderListAdapter;
    private ListView detailsOrderListView;

    private TextView orderNumberTextView;
    private TextView dateTextView;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView subtotalTextView;
    private TextView creditTextView;
    private TextView taxTextView;
    private TextView totalTextView;
    private TextView rewardTextView;

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText orderEditText;
    private TextView fromTextView;
    private TextView toTextView;

    private ImageView statusImageView;
    private Button progressStatusButton;
    private Button readyStatusButton;
    private Button completedStatusButton;

    private RelativeLayout customerRelativeLayout;
    private RelativeLayout searchRelativeLayout;
    private RelativeLayout noResultLayout;
    private Button refundButton;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private ProgressDialog dialog;
    int pageNumber = 1;
    int selectedOrder = 0;
    public ArrayList<OrderInfo> orders = new ArrayList<OrderInfo>();
    public boolean isSearched = false;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");



    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
            init();
    }


    private void init() {

        orderListView = getActivity().findViewById(R.id.orderListView);
        orderListAdapter = new OrderListAdapter(getActivity(), orders, (OrderListAdapter.OnOrderItemClickListener) this);
        orderListView.setAdapter(orderListAdapter);
        detailsOrderListView = getActivity().findViewById(R.id.detailsOrderListView);

        orderNumberTextView = getActivity().findViewById(R.id.orderNumberTextView);
        dateTextView = getActivity().findViewById(R.id.dateTextView);
        nameTextView = getActivity().findViewById(R.id.nameTextView);
        emailTextView = getActivity().findViewById(R.id.emailTextView);
        subtotalTextView = getActivity().findViewById(R.id.subtotalTextView);
        creditTextView = getActivity().findViewById(R.id.creditTextView);
        taxTextView = getActivity().findViewById(R.id.taxTextView);
        totalTextView = getActivity().findViewById(R.id.totalTextView);
        rewardTextView = getActivity().findViewById(R.id.rewardTextView);

        firstNameEditText = getActivity().findViewById(R.id.firstNameEditText);
        lastNameEditText = getActivity().findViewById(R.id.lastNameEditText);
        orderEditText = getActivity().findViewById(R.id.orderEditText);
        fromTextView = getActivity().findViewById(R.id.fromTextView);
        toTextView = getActivity().findViewById(R.id.toTextView);
        fromTextView.setOnClickListener(this);
        toTextView.setOnClickListener(this);

        statusImageView = getActivity().findViewById(R.id.statusImageView);
        progressStatusButton = getActivity().findViewById(R.id.progressStatusButton);
        progressStatusButton.setOnClickListener(this);
        readyStatusButton = getActivity().findViewById(R.id.readyStatusButton);
        readyStatusButton.setOnClickListener(this);
        completedStatusButton = getActivity().findViewById(R.id.completedStatusButton);
        completedStatusButton.setOnClickListener(this);

        customerRelativeLayout = getActivity().findViewById(R.id.customerRelativeLayout);
        customerRelativeLayout.setOnClickListener(this);

        searchRelativeLayout = getActivity().findViewById(R.id.searchRelativeLayout);
        searchRelativeLayout.setVisibility(View.GONE);

        noResultLayout = getActivity().findViewById(R.id.noResultLayout);
        noResultLayout.setVisibility(View.VISIBLE);

        refundButton = getActivity().findViewById(R.id.refundButton);
        refundButton.setOnClickListener(this);

        ImageButton searchButton = getActivity().findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);

        ImageButton searchImageButton = getActivity().findViewById(R.id.searchImageButton);
        searchImageButton.setOnClickListener(this);

        ImageButton closeSearchButton = getActivity().findViewById(R.id.closeSearchButton);
        closeSearchButton.setOnClickListener(this);

        Calendar now = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener fromDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                fromTextView.setText(i + "-" + (i1 + 1) + "-" + i2);
            }
        };

        fromDatePickerDialog = new DatePickerDialog(
                getActivity(), fromDateSetListener, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

        DatePickerDialog.OnDateSetListener toDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                toTextView.setText(i + "-" + (i1 + 1) + "-" + i2);
            }
        };

        toDatePickerDialog = new DatePickerDialog(
                getActivity(), toDateSetListener, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));


        isSearched = false;

    }

    private void updateOrderList(int position) {
        orderListAdapter.updateOrders(orders, position);
        orderListAdapter.notifyDataSetChanged();
        updateOrderDetails(position);
        selectedOrder = position;
    }

    private void updateOrderDetails(int position) {

        if (orders.size() == 0) {
            orderNumberTextView.setText("");
            dateTextView.setText("");
            nameTextView.setText("");
            emailTextView.setText("");
            subtotalTextView.setText("");
            creditTextView.setText("");
            taxTextView.setText("");
            totalTextView.setText("");
            rewardTextView.setText("");

            showStatus("4");
            detailsOrderListView.setVisibility(View.INVISIBLE);
            noResultLayout.setVisibility(View.VISIBLE);
            return;
        }

        detailsOrderListView.setVisibility(View.VISIBLE);
        noResultLayout.setVisibility(View.GONE);

        OrderInfo info = orders.get(position);

        int orderId = Integer.parseInt(info.getId());
        orderNumberTextView.setText(String.format("ORDER #%05d", orderId));
        dateTextView.setText(getReceivedDate(info.getTimestamp()));
        nameTextView.setText(info.getUserName());
        emailTextView.setText(info.getEmail());
        subtotalTextView.setText(String.format("$%.2f",Float.parseFloat(info.getSubTotal())));
        creditTextView.setText(String.format("$%.2f",Float.parseFloat(info.getRewardsCredit())));
        taxTextView.setText(String.format("$%.2f",Float.parseFloat(info.getTaxAmount())));
        totalTextView.setText(String.format("$%.2f",Float.parseFloat(info.getTotalPrice())));
        rewardTextView.setText("+" + info.getRewards());

        showStatus(info.getStatus());

        detailsOrderListAdapter = new DetailsOrderListAdapter(getActivity(), info.getDetails());
        detailsOrderListView.setAdapter(detailsOrderListAdapter);
    }

    private void showStatus(String status) {
        refundButton.setVisibility(View.GONE);
        progressStatusButton.setEnabled(false);
        readyStatusButton.setEnabled(false);
        completedStatusButton.setEnabled(false);

        switch (status) {
            case "0":
                refundButton.setVisibility(View.VISIBLE);
                statusImageView.setVisibility(View.VISIBLE);
                statusImageView.setImageResource(R.drawable.progress_new);
                progressStatusButton.setEnabled(true);
                break;
            case "1":
                statusImageView.setVisibility(View.VISIBLE);
                statusImageView.setImageResource(R.drawable.progress_in_progress);
                readyStatusButton.setEnabled(true);
                break;
            case "2":
                statusImageView.setVisibility(View.VISIBLE);
                statusImageView.setImageResource(R.drawable.progress_ready);
                completedStatusButton.setEnabled(true);
                break;
            case "3":
                statusImageView.setVisibility(View.VISIBLE);
                statusImageView.setImageResource(R.drawable.progress_completed);
                completedStatusButton.setEnabled(true);
                break;
            default:
                statusImageView.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSearch() {
        searchRelativeLayout.setVisibility(View.GONE);
        firstNameEditText.setText("");
        lastNameEditText.setText("");
        orderEditText.setText("");
        fromTextView.setText("");
        toTextView.setText("");
        hideKeyboard();
    }

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void updateStatus(final String status) {

        OrderInfo info = orders.get(selectedOrder);


        showLoader(R.string.empty);

        Application.getServerApi().updateOrderStatus(new UpdateOrderRequest(info.getId(), status)).enqueue(new Callback<BaseResponse>(){

            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(getActivity(), "Error", response.body().getMessage(), null, null);
                } else {
                    orders.get(selectedOrder).setStatus(status);
                    updateOrderList(selectedOrder);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("New Order", t.getLocalizedMessage());
                } else {
                    Log.d("New Order", "Unknown error");
                }
            }
        });

    }

    private void searchOrder(String firstName, String lastName, String order, String fromDate, String toDate) {

        if (!fromDate.isEmpty() && !toDate.isEmpty()) {
        fromDate = fromDate + " 00:00:00";
        toDate = toDate + " 23:59:59";
        }

        showLoader(R.string.empty);

        Application.getServerApi().searchOrders(new SearchOrderRequest(firstName, lastName, order, fromDate, toDate, StorePrefs.getStoreInfo(getActivity()).getId(), "2")).enqueue(new Callback<SearchOrdersResponse>(){

            @Override
            public void onResponse(Call<SearchOrdersResponse> call, Response<SearchOrdersResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(getActivity(), "Error", response.body().getMessage(), null, null);
                } else {
                    hideSearch();
                    orders = response.body().getOrders();
                    isSearched = true;
                    updateOrderList(0);
                }
            }

            @Override
            public void onFailure(Call<SearchOrdersResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("Search Order", t.getLocalizedMessage());
                } else {
                    Log.d("Search Order", "Unknown error");
                }
            }
        });
    }

    @Override
    public void onOrderItemClickListener(OrderInfo info, int position) {
        selectedOrder = position;
        updateOrderList(position);
        updateOrderDetails(position);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refundButton:
                Intent intent = new Intent(getActivity(), refundActivity.class);
                intent.putExtra("orderInfo", orders.get(selectedOrder));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;

            case R.id.customerRelativeLayout:
                intent = new Intent(getActivity(), CustomerInfoActivity.class);
                intent.putExtra("userId", orders.get(selectedOrder).getUserId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;

            case R.id.searchButton:
                searchRelativeLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.closeSearchButton:
                hideSearch();
                break;

            case R.id.searchImageButton:
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String order = orderEditText.getText().toString();
                String newOrder = Integer.toString(Integer.parseInt(order));
                String fromDate = fromTextView.getText().toString();
                String toDate = toTextView.getText().toString();

                if (firstName.isEmpty() && lastName.isEmpty() && newOrder.isEmpty() && fromDate.isEmpty() && toDate.isEmpty()) {
                    DialogUtils.showDialog(getActivity(), "Error", getString(R.string.empty_search_options), null, null);
                    break;
                } else {
                    if (fromDate.isEmpty() && !toDate.isEmpty()) {
                        DialogUtils.showDialog(getActivity(), "Error", getString(R.string.error_date), null, null);
                    }
                    if (!fromDate.isEmpty() && toDate.isEmpty()) {
                        DialogUtils.showDialog(getActivity(), "Error", getString(R.string.error_date), null, null);
                        break;
                    }

                    if (!fromDate.isEmpty() && !toDate.isEmpty()) {
                        try {
                            Date from = formatter.parse(fromDate);
                            Date to = formatter.parse(toDate);
                            if(from.compareTo(to) > 0) {
                                DialogUtils.showDialog(getActivity(), "Error", getString(R.string.error_date), null, null);
                                return;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }

                searchOrder(firstName, lastName, order, fromDate, toDate);

                break;

            case R.id.progressStatusButton:
                updateStatus("1");
                break;

            case R.id.readyStatusButton:
                updateStatus("2");
                break;

            case R.id.completedStatusButton:
                updateStatus("3");
                break;

            case R.id.fromTextView:
                fromDatePickerDialog.show();
                break;

            case R.id.toTextView:
                toDatePickerDialog.show();
                break;

        }
    }

    public void showLoader(int resId) {
        dialog = ProgressDialog.show(getActivity(), "",
                getString(resId), true);
    }

    public void hideLoader() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public String getReceivedDate(String dateString) {

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
}
