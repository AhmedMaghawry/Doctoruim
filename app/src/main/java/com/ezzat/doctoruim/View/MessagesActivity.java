package com.ezzat.doctoruim.View;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ezzat.doctoruim.Control.DatabaseController;
import com.ezzat.doctoruim.Control.ListDoctorRequestAdapter;
import com.ezzat.doctoruim.Control.ListMessageAdapter;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Control.onEvent;
import com.ezzat.doctoruim.Model.Message;
import com.ezzat.doctoruim.Model.Request;
import com.ezzat.doctoruim.R;

import java.util.ArrayList;

import static com.ezzat.doctoruim.Control.Utils.Constants.ARG_MESSES;
import static com.ezzat.doctoruim.Control.Utils.Constants.MESSAGE_TABLE;
import static com.ezzat.doctoruim.Control.Utils.Constants.REQUEST_TABLE;

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
        DatabaseController.getAllElements(MESSAGE_TABLE, Message.class, new onEvent() {
            @Override
            public void onStart(Object object) {
                Utils.showLoading(MessagesActivity.this);
            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                ArrayList<Message> messages = new ArrayList<>((ArrayList<Message>)object);
                ListMessageAdapter adapter = new ListMessageAdapter(messages, MessagesActivity.this);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Utils.hideDialog();
            }
        });
    }
}
