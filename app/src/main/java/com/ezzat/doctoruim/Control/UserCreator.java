package com.ezzat.doctoruim.Control;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.ezzat.doctoruim.Control.Utils.SharedValues;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import static com.ezzat.doctoruim.Control.Utils.Constants.CODE_SP;
import static com.ezzat.doctoruim.Control.Utils.Constants.VERF_SP;
import static com.ezzat.doctoruim.Control.Utils.Utils.sendVerCode;
import static com.ezzat.doctoruim.Control.Utils.Utils.signInWithPhoneAuthCredential;

public class UserCreator extends AsyncTask<String, Void, Void> {

    private onEvent callback;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mobile;

    public UserCreator(final onEvent callback, final Activity context) {
        this.callback = callback;
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
//                    callback.onEnd(code);
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
                callback.onEnd(null);
                Utils.hideDialog();
            }
        };
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.onStart(null);
    }

    @Override
    protected Void doInBackground(String... strings) {
        mobile = strings[2];

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.useAppLanguage();
        Log.i("dodo", mobile);
        sendVerCode("+20" + mobile, mCallbacks);
        /*final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Tag", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            User userNew = new User(first, password, mobile);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference userTable = database.getReference(USER_TABLE);
                            DatabaseReference rateTable = database.getReference(RATE_TABLE);
                            DatabaseReference balanceTable = database.getReference(BALANCE_TABLE);
                            userTable.child(user.getUid()).setValue(userNew);
                            rateTable.child(user.getUid()).setValue(userNew.getRate());
                            balanceTable.child(user.getUid()).setValue(userNew.getBalance());
                            callback.onEnd(true);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("tag", "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            callback.onEnd(false);
                        }
                    }
                });*/
        return null;
    }
}
