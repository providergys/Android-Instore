package com.teaera.teaerastore.net.Request;

/**
 * Created by admin on 17/10/2017.
 */

public class UpdateStoreRequest {

    final String storeId;
    final String waiting;
    final String opening;
    final String closing;

    public UpdateStoreRequest(String storeId, String waiting, String opening, String closing) {
        this.storeId = storeId;
        this.waiting = waiting;
        this.opening = opening;
        this.closing = closing;
    }
}
