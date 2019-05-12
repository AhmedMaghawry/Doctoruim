package com.ezzat.doctoruim.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.ezzat.doctoruim.Control.Utils.Constants.DOCTOR_TABLE;
import static com.ezzat.doctoruim.Control.Utils.Constants.REQUEST_TABLE;
import static com.ezzat.doctoruim.Control.Utils.Constants.USER_TABLE;

public class Request implements Serializable {

    private String cover;
    private String association, phone;

    public Request(){

    }

    public Request(String phone, String cover, String association) {
        this.cover = cover;
        this.association = association;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAssociation() {
        return association;
    }

    public void setAssociation(String association) {
        this.association = association;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", this.phone);
        map.put("cover", this.cover);
        map.put("association", this.association);
        return map;
    }

    public void deleteRequest () {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + getPhone(), null);
        mDatabase.getReference(REQUEST_TABLE).updateChildren(childUpdates);
    }

    public boolean addRequest() {
        try {
            DatabaseReference base = FirebaseDatabase.getInstance().getReference(REQUEST_TABLE);
            base.child(phone).setValue(this);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
