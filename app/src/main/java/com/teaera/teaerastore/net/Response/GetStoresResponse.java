package com.teaera.teaerastore.net.Response;

import com.teaera.teaerastore.net.Model.StoreInfo;

import java.util.ArrayList;

/**
 * Created by admin on 12/10/2017.
 */

public class GetStoresResponse extends BaseResponse {

    private ArrayList<StoreInfo> stores;

    public ArrayList<StoreInfo> getStores() {
        return stores;
    }

    public void setStores(ArrayList<StoreInfo> stores) {
        this.stores = stores;
    }

}
