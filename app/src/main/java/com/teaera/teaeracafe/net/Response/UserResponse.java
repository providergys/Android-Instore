package com.teaera.teaeracafe.net.Response;


import com.teaera.teaeracafe.net.Model.CategoryInfo;
import com.teaera.teaeracafe.net.Model.LocationInfo;
import com.teaera.teaeracafe.net.Model.MenuInfo;
import com.teaera.teaeracafe.net.Model.PromotedMenuInfo;
import com.teaera.teaeracafe.net.Model.UserInfo;

import java.util.ArrayList;

/**
 * Created by admin on 01/08/2017.
 */

public class UserResponse extends BaseResponse {

    private UserInfo user;
    private ArrayList<LocationInfo> locations;
    private ArrayList<CategoryInfo> categories;
    private ArrayList<PromotedMenuInfo> promoted;

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo userinfo) {
        this.user = userinfo;
    }

    public ArrayList<LocationInfo> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<LocationInfo> locations) {
        this.locations = locations;
    }

    public ArrayList<CategoryInfo> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CategoryInfo> categories) {
        this.categories = categories;
    }

    public ArrayList<PromotedMenuInfo> getPromoted() {
        return promoted;
    }

    public void setPromoted(ArrayList<PromotedMenuInfo> promoted) {
        this.promoted = promoted;
    }
}
