package com.teaera.teaerastore.net.Model;

import java.io.Serializable;

/**
 * Created by admin on 28/12/2017.
 */

public class PrintInfo implements Serializable {

    private String id;
    private String userName;
    private int rewards;
    private float totalRefund;
    private String totalPrice;
    private String status;
    private String timestamp;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRewards() {
        return rewards;
    }
    public void setRewards(int rewards) {
        this.rewards = rewards;
    }

    public float getTotalRefund() {
        return totalRefund;
    }
    public void setTotalRefund(float totalRefund) {
        this.totalRefund = totalRefund;
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

}
