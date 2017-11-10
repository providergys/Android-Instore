package com.teaera.teaerastore.net.Request;

/**
 * Created by admin on 24/10/2017.
 */

public class SearchOrderRequest {
    final String firstName;
    final String lastName;
    final String order;
    final String fromDate;
    final String toDate;
    final String storeId;
    final String kind;

    public SearchOrderRequest(String firstName, String lastName, String order, String fromDate, String toDate, String storeId, String kind) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.order = order;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.storeId = storeId;
        this.kind = kind;
    }
}
