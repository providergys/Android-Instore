package com.teaera.teaerastore.net.Response;

import com.teaera.teaerastore.net.Model.OrderInfo;

import java.util.ArrayList;

/**
 * Created by admin on 13/10/2017.
 */

public class GetOrdersResponse extends BaseResponse {

    private ArrayList<OrderInfo> orders;
    private String pageNumber ;

    public ArrayList<OrderInfo> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<OrderInfo> orders) {
        this.orders = orders;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

}