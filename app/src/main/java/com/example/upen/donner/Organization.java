package com.example.upen.donner;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by upen on 10/17/15.
 */
@ParseClassName("Organization")
public class Organization extends ParseObject {

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getLocation() {
        return getString("location");
    }

    public void setLocation(String location) {
        put("location", location);
    }

    public String getCatogery() {
        return getString("catogery");
    }

    public void setCatogery(String catogery) {
        put("catogery", catogery);
    }

    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        put("email", email);
    }

    public String getPhone() {
        return getString("phone");
    }

    public void setPhone(String phone) {
        put("phone", phone);
    }

    public String getLoginName() {
        return getString("loginname");
    }

    public void setLoginName(String loginName) {
        put("loginname", loginName);
    }

    public String getLoginPassword() {
        return getString("loginpassword");
    }

    public void setLoginPassword(String loginPassword) {
        put("loginpassword", loginPassword);
    }
}
