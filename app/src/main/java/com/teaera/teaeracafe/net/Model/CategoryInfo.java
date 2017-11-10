package com.teaera.teaeracafe.net.Model;

import java.io.Serializable;

/**
 * Created by admin on 10/09/2017.
 */

public class CategoryInfo implements Serializable {

    private String id;
    private String name;
    private String locationID;
    private String image;
    private String drinkable;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return name;
    }
    public void setCategoryName(String name) {
        this.name = name;
    }

    public String getLocationID() {
        return locationID;
    }
    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getDrinkable() {
        return drinkable;
    }
    public void setDrinkable(String drinkable) {
        this.drinkable = drinkable;
    }
}
