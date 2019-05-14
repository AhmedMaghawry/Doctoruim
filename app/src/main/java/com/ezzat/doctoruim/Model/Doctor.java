package com.ezzat.doctoruim.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ezzat.doctoruim.Control.Utils.Constants.DOCTOR_TABLE;
import static com.ezzat.doctoruim.Control.Utils.Constants.USER_TABLE;

public class Doctor implements Serializable {

    private String phone;
    private List<String> sps;
    private String rate;
    private int num;

    public Doctor (){}

    public Doctor(String phone, List<String> sps) {
        this.phone = phone;
        this.rate = "0";
        this.num = 0;
        this.sps = sps;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public List<String> getSps() {
        return sps;
    }

    public void setSps(List<String> sps) {
        this.sps = sps;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", this.phone);
        map.put("rate", this.rate);
        map.put("sps", this.sps);
        map.put("num", this.num);
        return map;
    }

    public void deleteDoctor () {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + getPhone(), null);
        mDatabase.getReference(DOCTOR_TABLE).updateChildren(childUpdates);
    }

    public void updateDoctor() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> userValues = toMap();
        childUpdates.put("/" + getPhone(), userValues);
        mDatabase.getReference(DOCTOR_TABLE).updateChildren(childUpdates);
    }

    public boolean addDoctor() {
        try {
            DatabaseReference base = FirebaseDatabase.getInstance().getReference(DOCTOR_TABLE);
            base.child(phone).setValue(this);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
