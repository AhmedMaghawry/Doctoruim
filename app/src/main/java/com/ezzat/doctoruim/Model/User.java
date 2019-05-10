package com.ezzat.doctoruim.Model;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.ezzat.doctoruim.Control.Utils.Constants.DOCTOR_TABLE;
import static com.ezzat.doctoruim.Control.Utils.Constants.USER_TABLE;

public class User implements Serializable {

    private String name, password, phone, image, address;
    private UserType type;

    public User(){}

    public User(String name, String password, String phone) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        image = "";
        address = "";
        type = UserType.Patient;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", this.name);
        map.put("password", this.password);
        map.put("phone", this.phone);
        map.put("image", this.image);
        map.put("address", this.address);
        map.put("type", this.type);
        return map;
    }

    public void deleteUser () {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + getPhone(), null);
        mDatabase.getReference(USER_TABLE).updateChildren(childUpdates);
        if (getType() == UserType.Doctor) {
            Map<String, Object> childUpdatesDoc = new HashMap<>();
            childUpdatesDoc.put("/" + getPhone(), null);
            mDatabase.getReference(DOCTOR_TABLE).updateChildren(childUpdatesDoc);
            //TODO : Delete from Auth
        }
    }

    public void updateUser() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> userValues = toMap();
        childUpdates.put("/" + getPhone(), userValues);
        mDatabase.getReference(USER_TABLE).updateChildren(childUpdates);
    }

    public boolean addUser() {
        try {
            DatabaseReference base = FirebaseDatabase.getInstance().getReference(USER_TABLE);
            base.child(phone).setValue(this);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
