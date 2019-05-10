package com.ezzat.doctoruim.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ezzat.doctoruim.Control.Utils.Constants.CLINIC_TABLE;

public class Clinic implements Serializable {

    private String id, name, location;
    private List<String> phones, doctors, days;
    private String startTime, endTime;

    public Clinic(){}

    public Clinic(String id, String name, String location, List<String> phones, List<String> doctors) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.phones = phones;
        this.doctors = doctors;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public List<String> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<String> doctors) {
        this.doctors = doctors;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.id);
        map.put("name", this.name);
        map.put("location", this.location);
        map.put("phones", this.phones);
        map.put("doctors", this.doctors);
        map.put("startTime", this.startTime);
        map.put("endTime", this.endTime);
        map.put("days", this.days);

        return map;
    }

    public void deleteClinic () {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + id, null);
        mDatabase.getReference(CLINIC_TABLE).updateChildren(childUpdates);
    }

    private void updateClinic() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> userValues = toMap();
        childUpdates.put("/" + id, userValues);
        mDatabase.getReference(CLINIC_TABLE).updateChildren(childUpdates);
    }

    public boolean addClinic() {
        try {
            DatabaseReference base = FirebaseDatabase.getInstance().getReference(CLINIC_TABLE);
            base.child(id).setValue(this);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
