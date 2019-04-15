package com.ezzat.doctoruim.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezzat.doctoruim.Model.Message;
import com.ezzat.doctoruim.R;

import static  com.ezzat.doctoruim.Control.Utils.Constants.ARG_REQ;

public class MessageActivity extends AppCompatActivity {

    private Message message;
    private ImageView photo;
    private TextView name, phone,content,reportedUser;
    private Button reject, accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        message = (Message) getIntent().getExtras().getSerializable(ARG_REQ);

        photo = findViewById(R.id.image);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        content = findViewById(R.id.content);
        reject = findViewById(R.id.reject);
        accept = findViewById(R.id.accept);
        reportedUser = findViewById(R.id.reportedUserId);
        Glide.with(this).load(getImage(message.getOwner().getImage_url())).into(photo);
        name.setText(message.getOwner().getName());
        phone.setText(message.getOwner().getPhone());

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:Remove from list
                onBackPressed();
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:Confirm
                onBackPressed();
            }
        });

    }

    public int getImage(String imageName) {

        int drawableResourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());

        return drawableResourceId;
    }
}
