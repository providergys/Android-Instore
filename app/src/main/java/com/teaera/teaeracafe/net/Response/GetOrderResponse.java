package com.teaera.teaeracafe.net.Response;

import com.teaera.teaeracafe.net.Model.OrderInfo;

import java.util.ArrayList;

/**
 * Created by admin on 01/10/2017.
 */

public class GetOrderResponse extends BaseResponse {
    private ArrayList<OrderInfo> orders;
    private int pageNumber;

    public ArrayList<OrderInfo> getOrders() {
        return orders;
    }
    public void setOrders(ArrayList<OrderInfo> orders) {
        this.orders = orders;
    }

    public int getPageNumber() {
        return pageNumber;
    }
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
