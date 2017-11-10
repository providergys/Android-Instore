package com.teaera.teaeracafe.net.Request;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by admin on 29/08/2017.
 */

public class SocialLoginRequest {

    final String name;
    final String email;
    final String fb_token;
    final String device;
    final String token;

    public SocialLoginRequest(String name, String email, String fbToken) {

        this.name = name;
        this.email = email;
        this.fb_token = fbToken;
        this.device = "android";
//        this.token = "token";
        this.token = FirebaseInstanceId.getInstance().getToken();
    }

}
