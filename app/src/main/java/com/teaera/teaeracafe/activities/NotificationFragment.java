package com.teaera.teaeracafe.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.teaera.teaeracafe.R;
import com.teaera.teaeracafe.adapter.NotificationListAdapter;
import com.teaera.teaeracafe.app.Application;
import com.teaera.teaeracafe.net.Model.OrderInfo;
import com.teaera.teaeracafe.net.Request.GetOrderRequest;
import com.teaera.teaeracafe.net.Response.GetOrderResponse;
import com.teaera.teaeracafe.preference.UserPrefs;
import com.teaera.teaeracafe.utils.DialogUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {


    private ListView notificationListView;
    private NotificationListAdapter notificationListAdapter;
    private SweetAlertDialog loaderDialog;
    private ArrayList<OrderInfo> orders = new ArrayList<OrderInfo>();

    int currentPage = 1;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        init();
    }

    @Override
    public void onResume() {
        super.onResume();

        currentPage = 1;
        loadOrder(currentPage);
    }

    public void init() {
        notificationListView = (ListView) getActivity().findViewById(R.id.notificationListView);

        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), OrderReceiptActivity.class);
                intent.putExtra("order", orders.get(position));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
    }

    private void loadOrder(int page) {
        showLoader(R.string.empty);

        Application.getServerApi().getOrders(new GetOrderRequest(UserPrefs.getUserInfo(getActivity()).getId(), page)).enqueue(new Callback<GetOrderResponse>(){

            @Override
            public void onResponse(Call<GetOrderResponse> call, Response<GetOrderResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(getActivity(), "Error", response.body().getMessage(), null, null);
                } else {
                    ArrayList<OrderInfo> newOrders = response.body().getOrders();
                    if (newOrders != null) {
                        orders.addAll(newOrders);
                        if (orders.size() > 0) {
                            updateOrderList(response.body().getPageNumber());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetOrderResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("Notification", t.getLocalizedMessage());
                } else {
                    Log.d("Notification", "Unknown error");
                }
            }
        });
    }

    private void updateOrderList(int page) {
        currentPage = page;

        notificationListAdapter = new NotificationListAdapter(this.getActivity(), orders);
        notificationListView.setAdapter(notificationListAdapter);
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
