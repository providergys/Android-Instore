package com.teaera.teaerastore.net.Response;


import com.teaera.teaerastore.net.Model.StoreInfo;

/**
 * Created by admin on 01/08/2017.
 */

public class StoreResponse extends BaseResponse {

    private StoreInfo store;

    public StoreInfo getStoreInfo() {
        return store;
    }

    public void setStoreInfo(StoreInfo storeinfo) {
        this.store = storeinfo;
    }

}
