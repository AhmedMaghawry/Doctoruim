package com.ezzat.doctoruim.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ezzat.doctoruim.Control.ListDoctorAdapter;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorsFragment extends Fragment {


    public DoctorsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_patients, container, false);
        final RecyclerView listView = v.findViewById(R.id.listView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        final List<User> doctors = new ArrayList<>();
        User mar = new User("Marwan Morsy", "AASDASD", "0154589960");
        mar.setImage_url("mar");
        mar.setAddress("Eyes");
        doctors.add(mar);
        listView.setAdapter(new ListDoctorAdapter(doctors, getActivity()));
        return v;
    }

}
