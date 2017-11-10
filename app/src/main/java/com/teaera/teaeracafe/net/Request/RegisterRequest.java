package com.teaera.teaeracafe.net.Request;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by admin on 01/08/2017.
 */

public class RegisterRequest {

    final String firstname;
    final String lastname;
    final String email;
    final String password;
    final String device;
    final String token;

    public RegisterRequest(String firstname, String lastname, String email, String password) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.device = "android";
//        this.token = "token";
        this.token = FirebaseInstanceId.getInstance().getToken();
    }
}
