package com.teaera.teaeracafe.net.Response;


import com.teaera.teaeracafe.net.Model.MenuInfo;
import java.util.ArrayList;

/**
 * Created by admin on 01/08/2017.
 */

public class MenuResponse  extends BaseResponse {

    private ArrayList<MenuInfo> menus;

    public ArrayList<MenuInfo> getMenus() {
        return menus;
    }

    public void setMenus(ArrayList<MenuInfo> menus) {
        this.menus = menus;
    }
}

