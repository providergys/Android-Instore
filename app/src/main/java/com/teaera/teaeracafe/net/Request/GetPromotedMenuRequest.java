package com.teaera.teaeracafe.net.Request;

/**
 * Created by admin on 28/09/2017.
 */

public class GetPromotedMenuRequest {

    final String menuId;
    final String categoryId;

    public GetPromotedMenuRequest(String menuId, String categoryId) {
        this.menuId = menuId;
        this.categoryId = categoryId;
    }

}
