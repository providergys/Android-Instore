package com.teaera.teaeracafe.net.Response;

import com.teaera.teaeracafe.net.Model.UserInfo;

/**
 * Created by admin on 25/10/2017.
 */

public class UserProfileResponse extends BaseResponse {
    private UserInfo user;

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo userinfo) {
        this.user = userinfo;
    }
}
