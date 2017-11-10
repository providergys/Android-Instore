package com.teaera.teaeracafe.net.Model;

import java.io.Serializable;

/**
 * Created by admin on 10/09/2017.
 */

public class LocationInfo implements Serializable {

    private String id;
    private String name;
    private String tax;
    private String waitingTime;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getLocationName() {
        return name;
    }
    public void setLocationName(String name) {
        this.name = name;
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
}
