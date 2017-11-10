package com.teaera.teaeracafe.net.Model;

import java.io.Serializable;

/**
 * Created by admin on 17/09/2017.
 */

public class CartInfo implements Serializable {

    private int cartIndex;
    private String locationId;
    private String menuId;
    private String menuName;
    private String options;
    private String quantity;
    private String price;
    private String drinkable;
    private String redeemed;


    public int getCartIndex() {
        return cartIndex;
    }

    public void setCartIndex(int cartIndex) {
        this.cartIndex = cartIndex;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDrinkable() {
        return drinkable;
    }

    public void setDrinkable(String drinkable) {
        this.drinkable = drinkable;
    }

    public String getRedeemed() {
        return redeemed;
    }

    public void setRedeemed(String redeemed) {
        this.redeemed = redeemed;
    }

}