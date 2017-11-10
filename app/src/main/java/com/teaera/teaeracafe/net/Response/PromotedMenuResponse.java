package com.teaera.teaeracafe.net.Response;

import com.teaera.teaeracafe.net.Model.CategoryInfo;
import com.teaera.teaeracafe.net.Model.MenuInfo;

/**
 * Created by admin on 28/09/2017.
 */

public class PromotedMenuResponse extends BaseResponse {
    private MenuInfo menu;
    private CategoryInfo category;

    public MenuInfo getMenu() {
        return menu;
    }
    public void setMenus(MenuInfo menu) {
        this.menu = menu;
    }


    public CategoryInfo getCategory() {
        return category;
    }
    public void setCategory(CategoryInfo category) {
        this.category = category;
    }
}
