package com.teaera.teaeracafe.net.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 01/08/2017.
 */

public class MenuInfo implements Serializable {

    private String id;
    private String name;
    private String image;
    private String price;
    private ArrayList<OptionInfo> options;
    private String promoted;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getMenuName() {
        return name;
    }
    public void setMenuName(String name) {
        this.name= name;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public ArrayList<OptionInfo> getOptions() {
        return options;
    }
    public void setOptions(ArrayList<OptionInfo> options) {
        this.options = options;
    }

    public String getPromoted() {
        return promoted;
    }
    public void setPromoted(String promoted) {
        this.promoted = promoted;
    }
}