package com.teaera.teaerastore.net.Request;

/**
 * Created by admin on 24/10/2017.
 */

public class AddRewardsRequest {

    final String storeId;
    final String userId;
    final String password;
    final String rewards;

    public AddRewardsRequest(String storeId, String userId, String password, String rewards) {
        this.storeId = storeId;
        this.userId = userId;
        this.password = password;
        this.rewards = rewards;
    }

}
