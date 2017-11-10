package com.teaera.teaeracafe.net.Response;

import com.teaera.teaeracafe.net.Model.CategoryInfo;
import com.teaera.teaeracafe.net.Model.LocationInfo;
import com.teaera.teaeracafe.net.Model.PromotedMenuInfo;

import java.util.ArrayList;

/**
 * Created by admin on 11/09/2017.
 */

public class GetCategoryResponse extends BaseResponse {

    private ArrayList<LocationInfo> locations;
    private ArrayList<CategoryInfo> categories;
    private ArrayList<PromotedMenuInfo> promoted;

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
