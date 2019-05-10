package com.ezzat.doctoruim.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ezzat.doctoruim.Model.Clinic;
import com.ezzat.doctoruim.Model.Doctor;
import com.ezzat.doctoruim.Model.Message;
import com.ezzat.doctoruim.Model.Request;
import com.ezzat.doctoruim.Model.Reservation;
import com.ezzat.doctoruim.Model.ReservationStatus;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.Model.UserType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.ezzat.doctoruim.Control.Utils.Constants.CLINIC_TABLE;
import static com.ezzat.doctoruim.Control.Utils.Constants.RESERVATION_TABLE;

public class DatabaseController {

    public static void getElement(String tableName, String id, final Class className, final onEvent event) {
        event.onStart(null);
        DatabaseReference base = FirebaseDatabase.getInstance().getReference(tableName).child(id);
        Log.i("MAr", id);
        base.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object c = null;
                if (className.equals(User.class)) {
                    c = dataSnapshot.getValue(User.class);
                } else if (className.equals(Request.class)) {
                    c = dataSnapshot.getValue(Request.class);
                } else if (className.equals(Doctor.class)) {
                    c = dataSnapshot.getValue(Doctor.class);
                } else if (className.equals(Message.class)) {
                    c = dataSnapshot.getValue(Message.class);
                } else if (className.equals(Clinic.class)) {
                    c = dataSnapshot.getValue(Clinic.class);
                }
                event.onEnd(c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getAllElements(String tableName, final Class className, final onEvent event) {
        event.onStart(null);
        DatabaseReference base = FirebaseDatabase.getInstance().getReference(tableName);
        base.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Object> res = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    res.add(data.getValue(className));
                }
                event.onEnd(res);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getAllReservationsDoctor(String doctorID, final onEvent event) {
        event.onStart(null);
        Log.i("Ar", doctorID);
        DatabaseReference base = FirebaseDatabase.getInstance().getReference(RESERVATION_TABLE).child(doctorID);
        base.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Object> res = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Reservation r = data.getValue(Reservation.class);
                    if (r.getStatus() == ReservationStatus.INPROGRESS)
                        res.add(r);
                }
                event.onEnd(res);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getAllClinicsDoctor(final String doctorID, final onEvent event) {
        event.onStart(null);
        DatabaseReference base = FirebaseDatabase.getInstance().getReference(CLINIC_TABLE);
        base.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Object> res = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Clinic c = data.getValue(Clinic.class);
                    if (c.getDoctors().contains(doctorID))
                        res.add(c);
                }
                event.onEnd(res);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public static void getAllUsersType(String tableName, final UserType type, final onEvent event) {
        event.onStart(null);
        DatabaseReference base = FirebaseDatabase.getInstance().getReference(tableName);
        base.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Object> res = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User c = data.getValue(User.class);
                    if (c.getType() == type)
                        res.add(c);
                }
                event.onEnd(res);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
