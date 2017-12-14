package com.teaera.teaerastore.net.Request;

import com.teaera.teaerastore.net.Model.OrderItemInfo;

import java.util.ArrayList;

/**
 * Created by admin on 23/10/2017.
 */

public class RefundOrderRequest {

    final String orderId;
    final String subTotal;
    final String rewardsCredit;
    final String tax;
    final String taxAmount;
    final String total;
    final ArrayList<OrderItemInfo> refundItems;


    public RefundOrderRequest(String orderId, String subTotal, String rewardsCredit, String tax, String taxAmount, String total, ArrayList<OrderItemInfo> refundItems) {
        this.orderId = orderId;
        this.subTotal = subTotal;
        this.rewardsCredit = rewardsCredit;
        this.tax = tax;
        this.taxAmount = taxAmount;
        this.total = total;
        this.refundItems = refundItems;
    }

}
