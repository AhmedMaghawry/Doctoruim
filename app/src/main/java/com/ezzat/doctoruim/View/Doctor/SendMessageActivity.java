package com.ezzat.doctoruim.View.Doctor;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ezzat.doctoruim.Control.DatabaseController;
import com.ezzat.doctoruim.Control.onEvent;
import com.ezzat.doctoruim.Model.Message;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.R;
import com.google.firebase.auth.FirebaseAuth;

import static com.ezzat.doctoruim.Control.Utils.Constants.USER_TABLE;


public class SendMessageActivity extends AppCompatActivity {

    private Message message;
    private TextView content,reportedUser;
    private Button send;
    private  String userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);


        content = findViewById(R.id.message_content);
        send = findViewById(R.id.send);
        reportedUser = findViewById(R.id.reportedUserIdSended);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:Confirm
                attemptSend();
                message = new Message(userPhone,reportedUser.getText().toString(),content.getText().toString());
                message.addMessage();
                onBackPressed();
            }
        });

        String phone1 = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        DatabaseController.getElement(USER_TABLE, phone1.substring(2, phone1.length()), User.class, new onEvent() {
            @Override
            public void onStart(Object object) {

            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                final User owner = (User) object;
                userPhone = (owner.getPhone());

            }
        });
    }

    public int getImage(String imageName) {

        int drawableResourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());

        return drawableResourceId;
    }
    private void attemptSend() {

        String  messageContent= String.valueOf(content.getText());
        String reportedUserid = String.valueOf(reportedUser.getText());


        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(messageContent)) {
            content.setError("Message content is required");
            focusView = content;
            cancel = true;
        }
        if (TextUtils.isEmpty(reportedUserid)) {
            reportedUser.setError("Reported user id is required");
            focusView = reportedUser;
            cancel = true;
        }if (!TextUtils.isDigitsOnly(reportedUserid)) {
            reportedUser.setError("Reported user id is only digits");
            focusView = reportedUser;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
    }
}
