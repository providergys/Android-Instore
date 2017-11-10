package com.teaera.teaerastore.net.Request;

/**
 * Created by admin on 13/10/2017.
 */

public class GetOrdersRequest {

    final String storeId;
    final int pageNumber;

    public GetOrdersRequest(String storeId, int page) {
        this.storeId = storeId;
        this.pageNumber = page;
    }
}
