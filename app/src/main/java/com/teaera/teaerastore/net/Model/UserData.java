package com.teaera.teaerastore.net.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 01/08/2017.
 */

public class UserData {

    private ArrayList<UserInfo> User;

    public class UserInfo implements Serializable {

        private String id;
        private String firstname;
        private String lastname;
        private String email;
        private String zipcode;
        private String is_deleted;

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

        public String getZipcode() {
            return zipcode;
        }
        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getIsDeleted() {
            return is_deleted;
        }
        public void setIsDeleted(String deleted) {
            this.is_deleted = deleted;
        }

    }

    public ArrayList<UserInfo> getUser() {
        return User;
    }

    public void setUser(ArrayList<UserInfo> user) {
        this.User = user;
    }
}
