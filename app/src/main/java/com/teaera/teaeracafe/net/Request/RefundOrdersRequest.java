package com.teaera.teaeracafe.net.Request;

import com.teaera.teaeracafe.net.Model.OrderDetailsInfo;

import java.util.ArrayList;

/**
 * Created by admin on 26/10/2017.
 */

public class RefundOrdersRequest {

    final String orderId;
    final String subTotal;
    final String rewardsCredit;
    final String total;
    final String tax;
    final ArrayList<OrderDetailsInfo> refundItems;

    public RefundOrdersRequest(String orderId, String subTotal, String rewardsCredit, String total, String tax, ArrayList<OrderDetailsInfo> refundItems) {
        this.orderId = orderId;
        this.subTotal = subTotal;
        this.rewardsCredit = rewardsCredit;
        this.total = total;
        this.tax = tax;
        this.refundItems = refundItems;
    }
}
