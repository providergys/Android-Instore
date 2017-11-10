package com.teaera.teaeracafe.net.Response;

import com.teaera.teaeracafe.net.Model.OrderDetailsInfo;
import com.teaera.teaeracafe.net.Model.OrderInfo;

import java.util.ArrayList;

/**
 * Created by admin on 26/10/2017.
 */

public class RefundOrdersResponse extends BaseResponse {

    private OrderInfo order;
    private ArrayList<OrderDetailsInfo> orderDetails;

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }

    public ArrayList<OrderDetailsInfo> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(ArrayList<OrderDetailsInfo> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
