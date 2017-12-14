package com.teaera.teaerastore.net.Request;

/**
 * Created by admin on 23/11/2017.
 */

public class UpdateOrderRequest {
    final String orderId;
    final String status;

    public UpdateOrderRequest(String orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }
}
