package com.teaera.teaerastore.net.Response;

import com.teaera.teaerastore.net.Model.OrderInfo;
import com.teaera.teaerastore.net.Model.PrintInfo;

import java.util.ArrayList;

/**
 * Created by admin on 20/12/2017.
 */

public class GetPrintResponse  extends BaseResponse {

    private ArrayList<PrintInfo> print;

    public ArrayList<PrintInfo> getPrintInfo() {
        return print;
    }

    public void setOrders(ArrayList<PrintInfo> infos) {
        this.print = infos;
    }

}
