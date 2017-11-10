package com.teaera.teaeracafe.net.Request;

/**
 * Created by admin on 26/10/2017.
 */

public class UpdateUserInfoRequest {
    final String userId;
    final String username;
    final String email;

    public UpdateUserInfoRequest(String userId, String username, String email) {
        this.userId     = userId;
        this.username  = username;
        this.email      = email;
    }
}
