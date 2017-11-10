package com.teaera.teaeracafe.net.Response;

/**
 * Created by admin on 01/08/2017.
 */

public class BaseResponse {

    private String error;
    private String message;

    private static String success = "false";
    private static String failed = "true";

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Boolean isError() {
        if (this.error == success) {
            return false;
        }
        return true;
    }
}
