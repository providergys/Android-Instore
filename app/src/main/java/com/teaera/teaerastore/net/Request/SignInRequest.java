package com.teaera.teaerastore.net.Request;

/**
 * Created by admin on 24/08/2017.
 */

public class SignInRequest {

    final String email;
    final String password;
    final String device;
    final String token;

    public SignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
        this.device = "android";
        this.token = "token";
    }

}
