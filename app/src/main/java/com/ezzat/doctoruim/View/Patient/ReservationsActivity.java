package com.ezzat.doctoruim.View.Patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ezzat.doctoruim.Control.DatabaseController;
import com.ezzat.doctoruim.Control.ListReservationAdapter;
import com.ezzat.doctoruim.Control.ListReservationPatientAdapter;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Control.onEvent;
import com.ezzat.doctoruim.Model.Reservation;
import com.ezzat.doctoruim.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ReservationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);
        final RecyclerView listView = findViewById(R.id.listView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        DatabaseController.getAllReservationsPatient(Utils.getPhone(), new onEvent() {
            @Override
            public void onStart(Object object) {
                Utils.showLoading(ReservationsActivity.this);
            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                ArrayList<Reservation> reservations = new ArrayList<>((ArrayList<Reservation>)object);
                ListReservationPatientAdapter adapter = new ListReservationPatientAdapter(reservations, ReservationsActivity.this);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Utils.hideDialog();
            }
        });
    }
}
