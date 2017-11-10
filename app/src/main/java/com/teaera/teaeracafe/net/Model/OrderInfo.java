package com.teaera.teaeracafe.net.Model;

import java.io.Serializable;

/**
 * Created by admin on 01/10/2017.
 */

public class OrderInfo implements Serializable {

    private String id;
    private String locationId;
    private String totalPrice;
    private String subTotal;
    private String rewardsCredit;
    private String tax;
    private String status;
    private String timestamp;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getLocationId() {
        return locationId;
    }
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
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

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeStamp() {
        return timestamp;
    }
    public void setTimeStamp(String timeStamp) {
        this.timestamp = timestamp;
    }
}
