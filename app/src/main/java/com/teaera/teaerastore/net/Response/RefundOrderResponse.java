package com.teaera.teaerastore.net.Response;

import com.teaera.teaerastore.net.Model.OrderInfo;
import com.teaera.teaerastore.net.Model.OrderItemInfo;

import java.util.ArrayList;

/**
 * Created by admin on 23/10/2017.
 */

public class RefundOrderResponse extends BaseResponse {

    private OrderInfo order;


    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo orderInfo) {
        this.order = order;
    }
}