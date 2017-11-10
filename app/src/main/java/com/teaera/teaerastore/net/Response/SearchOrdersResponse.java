package com.teaera.teaerastore.net.Response;

import com.teaera.teaerastore.net.Model.OrderInfo;

import java.util.ArrayList;

/**
 * Created by admin on 24/10/2017.
 */

public class SearchOrdersResponse extends BaseResponse {

    private ArrayList<OrderInfo> orders;

    public ArrayList<OrderInfo> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<OrderInfo> orders) {
        this.orders = orders;
    }
}
