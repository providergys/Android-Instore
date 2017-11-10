package com.teaera.teaeracafe.net.Request;

/**
 * Created by admin on 27/10/2017.
 */

public class UpdatePasswordRequest {
    final String userId;
    final String oldPass;
    final String newPass;

    public UpdatePasswordRequest(String userId, String oldPass, String newPass) {
        this.userId = userId;
        this.oldPass = oldPass;
        this.newPass = newPass;
    }
}
