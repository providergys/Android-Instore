package com.teaera.teaerastore.net.Response;

import com.teaera.teaerastore.net.Model.UserData;

/**
 * Created by admin on 01/08/2017.
 */

public class UserResponse extends BaseResponse {

    private UserData data;

    public UserData getData() {
        return data;
    }
    public void setData(UserData data) {
        this.data = data;
    }
}
