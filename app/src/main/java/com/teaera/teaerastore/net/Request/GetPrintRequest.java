package com.teaera.teaerastore.net.Request;

/**
 * Created by admin on 20/12/2017.
 */

public class GetPrintRequest {
    final String fromDate;
    final String toDate;
    final String storeId;


    public GetPrintRequest(String fromDate, String toDate, String storeId) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.storeId = storeId;
    }
}
