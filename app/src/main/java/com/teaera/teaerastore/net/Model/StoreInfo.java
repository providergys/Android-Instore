package com.teaera.teaerastore.net.Model;

import java.io.Serializable;

/**
 * Created by admin on 11/10/2017.
 */

public class StoreInfo implements Serializable {

    private String id;
    private String adminUserId;
    private String name;
    private String address;
    private String tax;
    private String waitingTime;
    private String openingHour;
    private String closingHour;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getAdminUserId() {
        return adminUserId;
    }
    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getTax() {
        return tax;
    }
    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getWaitingTime() {
        return waitingTime;
    }
    public void setWaitingTime(String waitingTime) {
        this.waitingTime = waitingTime;
    }

    public String getOpeningHour() {
        return openingHour;
    }
    public void setOpeningHour(String openingHour) {
        this.openingHour = openingHour;
    }

    public String getClosingHour() {
        return closingHour;
    }
    public void setClosingHour(String closingHour) {
        this.closingHour = closingHour;
    }
}