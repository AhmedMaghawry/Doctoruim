package com.ezzat.doctoruim.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {

    private String name, password, phone, image_url, address, id;
    private Rate rate;
    private Boolean verified;

    public User(){}

    public User(String name, String password, String phone) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.image_url = "mar";
        address = "Eye";
        rate = new Rate();
        verified = false;
        id = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", this.name);
        map.put("password", this.password);
        map.put("phone", this.phone);
        map.put("verified", this.verified);
        map.put("imageUrl", this.image_url);
        map.put("address", this.address);
        map.put("id", this.id);
        map.put("rate", this.rate);
        //TODO:: type remaining.
        return map;
    }
}
