package com.ezzat.doctoruim.View;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ezzat.doctoruim.Control.DatabaseController;
import com.ezzat.doctoruim.Control.ListDoctorAdapter;
import com.ezzat.doctoruim.Control.ListReservationAdapter;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Control.onEvent;
import com.ezzat.doctoruim.Model.Reservation;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.Model.UserType;
import com.ezzat.doctoruim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;

import java.util.ArrayList;

import static com.ezzat.doctoruim.Control.Utils.Constants.USER_TABLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReservationsFragment extends Fragment {


    public ReservationsFragment() {
        // Required empty public constructor
    }


    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reservations, container, false);
        final RecyclerView listView = v.findViewById(R.id.listView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        DatabaseController.getAllReservationsDoctor(phone.substring(2, phone.length()), new onEvent() {
            @Override
            public void onStart(Object object) {
                Utils.showLoading(getActivity());
            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                ArrayList<Reservation> reservations = new ArrayList<>((ArrayList<Reservation>)object);
                ListReservationAdapter adapter = new ListReservationAdapter(reservations, getActivity());
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Utils.hideDialog();
            }
        });
        return v;
    }

}
