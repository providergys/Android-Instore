package com.teaera.teaerastore.net.Request;

import com.teaera.teaerastore.net.Model.OrderItemInfo;

import java.util.ArrayList;

/**
 * Created by admin on 23/10/2017.
 */

public class RefundOrderRequest {

    final String userId;
    final String orderId;
    final String subTotal;
    final String rewardsCredit;
    final String total;
    final String tax;
    final String taxAmount;
    final int rewards;
    final ArrayList<OrderItemInfo> refundItems;


    public RefundOrderRequest(String userId, String orderId, String subTotal, String rewardsCredit, String tax, String taxAmount, String total, int rewards, ArrayList<OrderItemInfo> refundItems) {
        this.userId = userId;
        this.orderId = orderId;
        this.subTotal = subTotal;
        this.rewardsCredit = rewardsCredit;
        this.total = total;
        this.tax = tax;
        this.taxAmount = taxAmount;
        this.rewards = rewards;
        this.refundItems = refundItems;
    }

}
