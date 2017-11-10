package com.teaera.teaeracafe.net.Response;

import com.teaera.teaeracafe.net.Model.OrderDetailsInfo;

import java.util.ArrayList;

/**
 * Created by admin on 02/10/2017.
 */

public class OrderDetailsResponse extends BaseResponse {

    private ArrayList<OrderDetailsInfo> orders;

    public ArrayList<OrderDetailsInfo> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<OrderDetailsInfo> orders) {
        this.orders = orders;
    }
}

