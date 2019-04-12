package com.ezzat.doctoruim.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ezzat.doctoruim.Control.ListDoctorAdapter;
import com.ezzat.doctoruim.Control.ListPatientAdapter;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientsFragment extends Fragment {


    public PatientsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_patients, container, false);
        final RecyclerView listView = v.findViewById(R.id.listView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        final List<User> patients = new ArrayList<>();
        User ezzat = new User("Ahmed Ezzat", "AASDASD", "01129689960");
        ezzat.setImage_url("ezz");
        User ars = new User("Arsenous Essa", "AASDASD", "012564689960");
        ars.setImage_url("ars");
        User def = new User("Mohamed Deif", "AASDASD", "0154559960");
        def.setImage_url("def");
        patients.add(ezzat);
        patients.add(ars);
        patients.add(def);
        listView.setAdapter(new ListPatientAdapter(patients, getActivity()));
        return v;
    }

}
