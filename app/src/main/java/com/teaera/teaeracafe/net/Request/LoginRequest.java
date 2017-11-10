package com.teaera.teaeracafe.net.Request;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by admin on 01/08/2017.
 */

public class LoginRequest {

    final String email;
    final String password;
    final String device;
    final String token;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
        this.device = "android";
//        this.token = "token";
        this.token = FirebaseInstanceId.getInstance().getToken();
    }

}
