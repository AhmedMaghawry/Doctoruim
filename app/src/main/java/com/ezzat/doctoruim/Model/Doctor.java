package com.ezzat.doctoruim.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ezzat.doctoruim.Control.Utils.Constants.DOCTOR_TABLE;
import static com.ezzat.doctoruim.Control.Utils.Constants.USER_TABLE;

public class Doctor implements Serializable {

    private String phone;
    private ArrayList<String> specializations;

    public Doctor (){}

    public Doctor(String phone, ArrayList<String> specializations) {
        this.phone = phone;
        this.specializations = specializations;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public ArrayList<String> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(ArrayList<String> specializations) {
        this.specializations = specializations;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", this.phone);
        map.put("specializations", specializations);
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
