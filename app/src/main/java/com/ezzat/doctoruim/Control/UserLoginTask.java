package com.ezzat.doctoruim.Control;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.ezzat.doctoruim.Control.Utils.SharedValues;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Model.User;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.ezzat.doctoruim.Control.Utils.Constants.*;
import static com.ezzat.doctoruim.Control.Utils.Utils.sendVerCode;

public class UserLoginTask extends AsyncTask<String, Void, Boolean> {

    private Activity context;
    private onEvent event;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    public UserLoginTask(final Activity context, final onEvent event) {
        this.context = context;
        this.event = event;
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                String code = credential.getSmsCode();

                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
//                if (code != null) {
//                    event.onEnd(code);
//                    Utils.hideDialog();
//                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("mob", "onVerificationFailed", e);
                Utils.hideDialog();
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                super.onCodeSent(verificationId, token);
                Log.d("mob", "onCodeSent:" + verificationId);
                // Save verification ID and resending token so we can use them later
                SharedValues.saveValue(context.getApplicationContext(), VERF_SP, verificationId);
                event.onEnd(null);
                Utils.hideDialog();
            }
        };
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

        checkUser("+20"+params[0], params[1]);

        return true;
    }

    private String getVal (String val) {
        String x = SharedValues.getValueS(context.getApplicationContext(), val);
        if (x != null)
            return x;
        return "";
    }

    public void checkUser(final String phone, final String password) {
        DatabaseReference base = FirebaseDatabase.getInstance().getReference("Users");
        base.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false, correct = false;
                Log.i("dodo", "start");
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    User u = datas.getValue(User.class);
                    Log.i("dodo", u.getPhone() + " ---- " + phone);
                    if (("+20" + u.getPhone()).equals(phone)) {
                        found = true;
                        Log.i("dodo", "Find");
                        if (u.getPassword().equals(password)) {
                            correct = true;
                            Log.i("dodo", "Correct");
                            break;
                        }else {
                            Log.i("dodo", "Not Her");
                            Toast.makeText(context, "Password is Incorrect", Toast.LENGTH_SHORT).show();
                            Utils.hideDialog();
                        }
                    } else {
                        Log.i("dodo", "Not Her");
                        Toast.makeText(context, "User not Found", Toast.LENGTH_SHORT).show();
                        Utils.hideDialog();
                    }
                }

                if (found && correct) {
                    sendVerCode(phone, mCallbacks);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}