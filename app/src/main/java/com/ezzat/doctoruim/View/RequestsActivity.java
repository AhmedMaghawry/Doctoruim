package com.ezzat.doctoruim.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ezzat.doctoruim.Control.ListDoctorAdapter;
import com.ezzat.doctoruim.Control.ListDoctorRequestAdapter;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.R;

import java.util.ArrayList;
import java.util.List;

import static com.ezzat.doctoruim.Control.Utils.Constants.ARG_REQS;

public class RequestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        getSupportActionBar().setTitle("Requests");
        final RecyclerView listView = findViewById(R.id.listView);
        LinearLayoutManager llm = new LinearLayoutManager(RequestsActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        ArrayList<User> doctors = (ArrayList<User>) getIntent().getExtras().getSerializable(ARG_REQS);
        listView.setAdapter(new ListDoctorRequestAdapter(doctors, this));
    }
}
