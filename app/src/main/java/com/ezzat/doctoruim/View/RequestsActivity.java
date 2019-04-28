package com.ezzat.doctoruim.View;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ezzat.doctoruim.Control.DatabaseController;
import com.ezzat.doctoruim.Control.ListDoctorRequestAdapter;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Control.onEvent;
import com.ezzat.doctoruim.Model.Request;
import com.ezzat.doctoruim.R;

import java.util.ArrayList;

import static com.ezzat.doctoruim.Control.Utils.Constants.REQUEST_TABLE;

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
        DatabaseController.getAllElements(REQUEST_TABLE, Request.class, new onEvent() {
            @Override
            public void onStart(Object object) {
                Utils.showLoading(RequestsActivity.this);
            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                ArrayList<Request> requests = new ArrayList<>((ArrayList<Request>)object);
                ListDoctorRequestAdapter adapter = new ListDoctorRequestAdapter(requests, RequestsActivity.this);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Utils.hideDialog();
            }
        });
    }
}
