package com.teaera.teaeracafe.net.Request;

import com.teaera.teaeracafe.net.Model.CartInfo;

import java.util.ArrayList;

/**
 * Created by admin on 28/09/2017.
 */

public class OrderRequest {
    final String userId;
    final String locationId;
    final String subTotal;
    final String rewardsCredit;
    final String tax;
    final String totalPrice;
    final String rewards;
    final ArrayList<CartInfo> cartInfos;

    public OrderRequest(String userId, String locationId, String subTotal, String rewardsCredit, String tax, String totalPrice, String rewards, ArrayList<CartInfo> cartInfos) {
        this.userId = userId;
        this.locationId = locationId;
        this.subTotal = subTotal;
        this.rewardsCredit = rewardsCredit;
        this.tax = tax;
        this.totalPrice = totalPrice;
        this.rewards = rewards;
        this.cartInfos = cartInfos;
    }
}
