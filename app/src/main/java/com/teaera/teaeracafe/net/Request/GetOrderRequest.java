package com.teaera.teaeracafe.net.Request;

/**
 * Created by admin on 01/10/2017.
 */

public class GetOrderRequest {
    final String userId;
    final int pageNumber;

    public GetOrderRequest(String userId, int pageNumber) {
        this.userId = userId;
        this.pageNumber = pageNumber;
    }
}
