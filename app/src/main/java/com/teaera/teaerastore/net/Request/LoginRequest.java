package com.teaera.teaerastore.net.Request;

/**
 * Created by admin on 24/08/2017.
 */

public class LoginRequest {

    final String locationId;
    final String password;

    public LoginRequest(String locationId, String password) {
        this.locationId = locationId;
        this.password = password;
    }

}
