package com.teaera.teaeracafe.net.Model;

import java.io.Serializable;

/**
 * Created by admin on 27/09/2017.
 */

public class PromotedMenuInfo implements Serializable {

    private String id;
    private String name;
    private String image;
    private String categoryId;

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

    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
