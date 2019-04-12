package com.ezzat.doctoruim.Control;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.ezzat.doctoruim.Control.Utils.SharedValues;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import static com.ezzat.doctoruim.Control.Utils.Constants.*;
import static com.ezzat.doctoruim.Control.Utils.Utils.signInWithPhoneAuthCredential;

public class UserLoginTask extends AsyncTask<String, Void, Boolean> {

    private FirebaseAuth mAuth;
    private Activity context;
    private onEvent event;

    public UserLoginTask(Activity context, onEvent event) {
        this.context = context;
        this.event = event;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        event.onStart(null);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            // Simulate network access.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return false;
        }

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("+20"+params[0], params[1])
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Login", "signInWithEmail:success");
                            event.onEnd(true);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Login", "signInWithEmail:failure", task.getException());
                            event.onEnd(false);
                        }
                    }
                });
        return true;
    }

    private String getVal (String val) {
        String x = SharedValues.getValueS(context.getApplicationContext(), val);
        if (x != null)
            return x;
        return "";
    }
}