package com.teaera.teaerastore.net.Model;

import java.io.Serializable;

/**
 * Created by admin on 12/10/2017.
 */

public class OrderItemInfo implements Serializable {

    private String id;
    private String orderId;
    private String menuId;
    private String menuName;
    private String options;
    private String price;
    private String rewards;
    private String quantity;
    private String refund_quantity;
    private String redeemed;
    private String drinkable;
    private String refunded;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMenuId() {
        return menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getOptions() {
        return options;
    }
    public void setOptions(String options) {
        this.options = options;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }

    public String getQuantity() {
        return quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRefundQuantity() {
        return refund_quantity;
    }
    public void setRefundQuantity(String quantity) {
        this.refund_quantity = quantity;
    }

    public String getRedeemed() {
        return redeemed;
    }
    public void setRedeemed(String redeemed) {
        this.redeemed = redeemed;
    }

    public String getDrinkable() {
        return drinkable;
    }
    public void setDrinkable(String drinkable) {
        this.drinkable = drinkable;
    }

    public String getRefunded() {
        return refunded;
    }
    public void setRefunded(String refunded) {
        this.refunded = refunded;
    }
}

