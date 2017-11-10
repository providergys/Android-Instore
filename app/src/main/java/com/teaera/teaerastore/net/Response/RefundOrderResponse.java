package com.teaera.teaerastore.net.Response;

import com.teaera.teaerastore.net.Model.OrderInfo;

/**
 * Created by admin on 23/10/2017.
 */

public class RefundOrderResponse extends BaseResponse {

    private OrderInfo orderInfo;

    public OrderInfo getOrder() {
        return orderInfo;
    }

    public void setOrder(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}