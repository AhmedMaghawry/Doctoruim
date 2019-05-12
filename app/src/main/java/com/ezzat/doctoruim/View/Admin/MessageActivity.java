package com.ezzat.doctoruim.View.Admin;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezzat.doctoruim.Control.DatabaseController;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Control.onEvent;
import com.ezzat.doctoruim.Model.Message;
import com.ezzat.doctoruim.Model.MessageStatus;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.R;

import static  com.ezzat.doctoruim.Control.Utils.Constants.ARG_MESS;
import static com.ezzat.doctoruim.Control.Utils.Constants.PLACEHOLDER_IMG;
import static com.ezzat.doctoruim.Control.Utils.Constants.USER_TABLE;

public class MessageActivity extends AppCompatActivity {

    private Message message;
    private ImageView photo;
    private TextView name, phone,content,reportedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        message = (Message) getIntent().getExtras().getSerializable(ARG_MESS);

        photo = findViewById(R.id.image);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        content = findViewById(R.id.content);
        reportedUser = findViewById(R.id.reportedUserId);

        message.setStatus(MessageStatus.READ);
        message.updateMessage();


        DatabaseController.getElement(USER_TABLE ,message.getPhone(), User.class, new onEvent() {
            @Override
            public void onStart(Object object) {
                Utils.showLoading(MessageActivity.this);
            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                Utils.hideDialog();
                final User owner = (User) object;
                Glide.with(MessageActivity.this).load(owner.getImage()).placeholder(getImage(PLACEHOLDER_IMG)).into(photo);
                name.setText(owner.getName());
                phone.setText(owner.getPhone());
            }
        });
    }

    public int getImage(String imageName) {

        int drawableResourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());

        return drawableResourceId;
    }
}
