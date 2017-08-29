package com.teaera.teaerastore.net.Response;

/**
 * Created by admin on 24/08/2017.
 */

public class BaseResponse {

    private String status;
    private String message;


    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
