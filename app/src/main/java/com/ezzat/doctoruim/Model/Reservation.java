package com.ezzat.doctoruim.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.ezzat.doctoruim.Control.Utils.Constants.RESERVATION_TABLE;

public class Reservation implements Serializable {

    private String patientID, doctorID,id;
    private String date;
    private ReservationStatus status;

    public Reservation(){}

    public Reservation(String patientID, String doctorID, String date) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = date;
        status = ReservationStatus.INPROGRESS;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("patientID", this.patientID);
        map.put("doctorID", this.doctorID);
        map.put("date", this.date);
        map.put("status", this.status);
        map.put("id", id);
        return map;
    }

    public void deleteReservation () {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + doctorID, null);
        mDatabase.getReference(RESERVATION_TABLE).updateChildren(childUpdates);
    }

    public void updateReservation() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> userValues = toMap();
        childUpdates.put("/"+id, userValues);
        mDatabase.getReference(RESERVATION_TABLE).child(doctorID).updateChildren(childUpdates);
    }

    public boolean addReservation() {
        try {
            DatabaseReference base = FirebaseDatabase.getInstance().getReference(RESERVATION_TABLE);
            DatabaseReference d = base.child(doctorID).push();
            this.id = d.getKey();
            d.setValue(this);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
