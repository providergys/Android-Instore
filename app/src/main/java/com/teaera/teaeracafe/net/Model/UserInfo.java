package com.teaera.teaeracafe.net.Model;


/**
 * Created by admin on 01/08/2017.
 */

public class UserInfo {

    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String image;
    private int rewardStar;
    private int balance;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public int getRewardStar() {
        return rewardStar;
    }
    public void setRewardStar(int rewardStar) {
        this.rewardStar = rewardStar;
    }

    public int getBalance() {
        return balance;
    }
    public void setBalance(int balance) {
        this.balance = balance;
    }

}
