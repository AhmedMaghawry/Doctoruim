package com.ezzat.doctoruim.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezzat.doctoruim.Model.Request;
import com.ezzat.doctoruim.R;

import static com.ezzat.doctoruim.Control.Utils.Constants.ARG_REQ;

public class RequestActivity extends AppCompatActivity {

    private Request request;
    private ImageView photo, asso;
    private TextView name, spec, phone, coverletter;
    private Button reject, accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        request = (Request) getIntent().getExtras().getSerializable(ARG_REQ);

        photo = findViewById(R.id.image);
        name = findViewById(R.id.name);
        spec = findViewById(R.id.sp);
        phone = findViewById(R.id.phone);
        coverletter = findViewById(R.id.cover);
        asso = findViewById(R.id.ass);
        reject = findViewById(R.id.reject);
        accept = findViewById(R.id.accept);

        Glide.with(this).load(getImage(request.getOwner().getImage_url())).into(photo);
        name.setText(request.getOwner().getName());
        spec.setText(request.getOwner().getAddress());
        phone.setText(request.getOwner().getPhone());
        coverletter.setText(request.getMessage());
        Glide.with(this).load(getImage(request.getCardURL())).into(asso);

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
