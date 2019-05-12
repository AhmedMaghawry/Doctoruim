package com.ezzat.doctoruim.View.Doctor;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ezzat.doctoruim.Control.DatabaseController;
import com.ezzat.doctoruim.Control.ListClinicAdapter;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Control.onEvent;
import com.ezzat.doctoruim.Model.Clinic;
import com.ezzat.doctoruim.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClinicsFragment extends Fragment {


    public ClinicsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_clinics, container, false);
        final RecyclerView listView = v.findViewById(R.id.listView);
        Button add = v.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.launchActivity(getContext(), AddClinicActivity.class, null);
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        DatabaseController.getAllClinicsDoctor(phone.substring(2, phone.length()), new onEvent() {
            @Override
            public void onStart(Object object) {
                Utils.showLoading(getActivity());
            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                ArrayList<Clinic> clinics = new ArrayList<>((ArrayList<Clinic>)object);
                ListClinicAdapter adapter = new ListClinicAdapter(clinics, getActivity());
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Utils.hideDialog();
            }
        });
        return v;
    }

}
