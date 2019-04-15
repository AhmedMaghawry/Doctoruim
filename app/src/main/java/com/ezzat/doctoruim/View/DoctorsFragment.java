package com.ezzat.doctoruim.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ezzat.doctoruim.Control.DatabaseController;
import com.ezzat.doctoruim.Control.ListDoctorAdapter;
import com.ezzat.doctoruim.Control.ListPatientAdapter;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Control.onEvent;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.Model.UserType;
import com.ezzat.doctoruim.R;

import java.util.ArrayList;
import java.util.List;

import static com.ezzat.doctoruim.Control.Utils.Constants.USER_TABLE;

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
        DatabaseController.getAllUsersType(USER_TABLE, UserType.Doctor, new onEvent() {
            @Override
            public void onStart(Object object) {
                Utils.showLoading(getActivity());
            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                ArrayList<User> doctors = new ArrayList<>((ArrayList<User>)object);
                ListDoctorAdapter adapter = new ListDoctorAdapter(doctors, getActivity());
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Utils.hideDialog();
            }
        });
        return v;
    }

}
