package com.teaera.teaerastore.net.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 12/10/2017.
 */

public class OrderInfo implements Serializable {

    private String id;
    private String userId;
    private String email;
    private String userName;
    private int rewardStar;
    private String subTotal;
    private String rewardsCredit;
    private String tax;
    private String totalPrice;
    private String status;
    private String timestamp;
    private ArrayList<OrderItemInfo> details;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRewardStar() {
        return rewardStar;
    }
    public void setRewardStar(int rewardStar) {
        this.rewardStar = rewardStar;
    }

    public String getSubTotal() {
        return subTotal;
    }
    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getRewardsCredit() {
        return rewardsCredit;
    }
    public void setRewardsCredit(String rewardsCredit) {
        this.rewardsCredit = rewardsCredit;
    }

    public String getTax() {
        return tax;
    }
    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<OrderItemInfo> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<OrderItemInfo> details) {
        this.details = details;
    }
}
