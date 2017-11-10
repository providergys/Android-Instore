package com.teaera.teaerastore.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teaera.teaerastore.R;
import com.teaera.teaerastore.adapter.DetailsOrderListAdapter;
import com.teaera.teaerastore.adapter.OrderListAdapter;
import com.teaera.teaerastore.app.Application;
import com.teaera.teaerastore.net.Model.OrderInfo;
import com.teaera.teaerastore.net.Request.GetOrdersRequest;
import com.teaera.teaerastore.net.Response.GetOrdersResponse;
import com.teaera.teaerastore.preference.StorePrefs;
import com.teaera.teaerastore.utils.DialogUtils;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedOrderFragment extends Fragment implements View.OnClickListener, OrderListAdapter.OnOrderItemClickListener {

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

    private RelativeLayout customerRelativeLayout;

    private SweetAlertDialog loaderDialog;
    int pageNumber = 1;
    int selectedOrder = 0;
    private ArrayList<OrderInfo> orders = new ArrayList<OrderInfo>();

    public CompletedOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_completed_order, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        init();
    }

    @Override
    public void onResume() {
        super.onResume();

        pageNumber = 1;
        loadOrders(pageNumber);
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

        customerRelativeLayout = getActivity().findViewById(R.id.customerRelativeLayout);
        customerRelativeLayout.setOnClickListener(this);

        Button refundButton = getActivity().findViewById(R.id.refundButton);
        refundButton.setOnClickListener(this);
    }

    private void loadOrders(int page) {
        showLoader(R.string.empty);

        Application.getServerApi().getCompletedOrders(new GetOrdersRequest(StorePrefs.getStoreInfo(getActivity()).getId(), page)).enqueue(new Callback<GetOrdersResponse>(){

            @Override
            public void onResponse(Call<GetOrdersResponse> call, Response<GetOrdersResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(getActivity(), "Error", response.body().getMessage(), null, null);
                } else {
                    orders = response.body().getOrders();
                    pageNumber = Integer.parseInt(response.body().getPageNumber());

                    updateOrderList(0);
                }
            }

            @Override
            public void onFailure(Call<GetOrdersResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("CompletedOrder", t.getLocalizedMessage());
                } else {
                    Log.d("CompletedOrder", "Unknown error");
                }
            }
        });
    }

    private void updateOrderList(int position) {
        orderListAdapter.updateOrders(orders, position);
        orderListAdapter.notifyDataSetChanged();
        updateOrderDetails(0);
        selectedOrder = 0;
    }

    private void updateOrderDetails(int position) {

        OrderInfo info = orders.get(position);

        orderNumberTextView.setText("ORDER: #" + info.getId());
        dateTextView.setText(info.getTimestamp());
        nameTextView.setText(info.getUserName());
        emailTextView.setText(info.getEmail());
        subtotalTextView.setText("$" + info.getSubTotal());
        creditTextView.setText("-$" + info.getRewardsCredit());
        taxTextView.setText(String.format("$%.3f", (Float.parseFloat(info.getSubTotal()) - Float.parseFloat(info.getRewardsCredit())) * Float.parseFloat(info.getTax())));
        totalTextView.setText("$" + info.getTotalPrice());

        int rewards = 0;
        if (info.getDetails().size() > 0) {
            for (int i = 0; i<info.getDetails().size(); i++) {
                if (info.getDetails().get(i).getDrinkable().equals("1") && info.getDetails().get(i).getRedeemed().equals("0")) {
                    rewards = rewards + 1 * Integer.parseInt(info.getDetails().get(i).getQuantity());
                }
            }
        }
        rewardTextView.setText("+" + rewards);

        detailsOrderListAdapter = new DetailsOrderListAdapter(getActivity(), info.getDetails());
        detailsOrderListView.setAdapter(detailsOrderListAdapter);
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
        }
    }

    public void showLoader(int resId) {
        loaderDialog = new SweetAlertDialog(this.getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        loaderDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.loader_color));
        loaderDialog.setTitleText(getString(resId));
        loaderDialog.setCancelable(false);
        loaderDialog.show();
    }

    public void hideLoader() {
        if (loaderDialog != null) {
            loaderDialog.dismissWithAnimation();
        }
    }
}
