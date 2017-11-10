package com.teaera.teaerastore.net.Request;

/**
 * Created by admin on 24/10/2017.
 */

public class GetCustomerRequest {

    final String userId;
    final String storeId;
    final int pageNumber;

    public GetCustomerRequest(String userId, String storeId, int page) {
        this.userId = userId;
        this.storeId = storeId;
        this.pageNumber = page;
    }

}
