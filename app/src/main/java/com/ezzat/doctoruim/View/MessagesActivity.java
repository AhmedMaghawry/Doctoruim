package com.ezzat.doctoruim.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ezzat.doctoruim.Control.ListMessageAdapter;
import com.ezzat.doctoruim.Model.Message;
import com.ezzat.doctoruim.R;

import java.util.ArrayList;

import static com.ezzat.doctoruim.Control.Utils.Constants.ARG_REQS;

public class MessagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        getSupportActionBar().setTitle("Messages");
        final RecyclerView listView = findViewById(R.id.listView);
        LinearLayoutManager llm = new LinearLayoutManager(MessagesActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        ArrayList<Message> users = (ArrayList<Message>) getIntent().getExtras().getSerializable(ARG_REQS);
        listView.setAdapter(new ListMessageAdapter(users, this));
    }
}
