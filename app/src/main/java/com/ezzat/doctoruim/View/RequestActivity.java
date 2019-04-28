package com.ezzat.doctoruim.View;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezzat.doctoruim.Control.DatabaseController;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Control.onEvent;
import com.ezzat.doctoruim.Model.Doctor;
import com.ezzat.doctoruim.Model.Request;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.Model.UserType;
import com.ezzat.doctoruim.R;

import java.util.ArrayList;

import static com.ezzat.doctoruim.Control.Utils.Constants.ARG_REQ;
import static com.ezzat.doctoruim.Control.Utils.Constants.PLACEHOLDER_IMG;
import static com.ezzat.doctoruim.Control.Utils.Constants.USER_TABLE;

public class RequestActivity extends AppCompatActivity {

    private Request request;
    private ImageView photo, asso;
    private TextView name, phone, coverletter;
    private Button reject, accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        request = (Request) getIntent().getExtras().getSerializable(ARG_REQ);

        photo = findViewById(R.id.image);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        coverletter = findViewById(R.id.cover);
        asso = findViewById(R.id.ass);
        reject = findViewById(R.id.reject);
        accept = findViewById(R.id.accept);

        DatabaseController.getElement(USER_TABLE ,request.getPhone(), User.class, new onEvent() {
            @Override
            public void onStart(Object object) {
                Utils.showLoading(RequestActivity.this);
            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                Utils.hideDialog();
                final User owner = (User) object;
                Glide.with(RequestActivity.this).load(owner.getImage()).placeholder(getImage(PLACEHOLDER_IMG)).into(photo);
                name.setText(owner.getName());
                phone.setText(owner.getPhone());

                coverletter.setText(request.getCover());
                Glide.with(RequestActivity.this).load(request.getAssociation()).placeholder(getImage(PLACEHOLDER_IMG)).into(asso);

                reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        request.deleteRequest();
                        Utils.launchActivity(RequestActivity.this, RequestsActivity.class, null);
                    }
                });

                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Doctor doctor = new Doctor(owner.getPhone(), new ArrayList<String>());
                        owner.setType(UserType.Doctor);
                        doctor.addDoctor();
                        request.deleteRequest();
                        Utils.launchActivity(RequestActivity.this, RequestsActivity.class, null);
                    }
                });
            }
        });

    }

    public int getImage(String imageName) {

        int drawableResourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());

        return drawableResourceId;
    }
}
