package com.ezzat.doctoruim.Control.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ezzat.doctoruim.Control.DatabaseController;
import com.ezzat.doctoruim.Control.onEvent;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.View.Admin.HomeActivity;
import com.ezzat.doctoruim.View.Doctor.DoctorProfileActivity;
import com.ezzat.doctoruim.View.Doctor.HomeDoctorActivity;
import com.ezzat.doctoruim.View.Patient.HomePatientActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.ezzat.doctoruim.Control.Utils.Constants.CODE_SP;
import static com.ezzat.doctoruim.Control.Utils.Constants.PLACEHOLDER_IMG;
import static com.ezzat.doctoruim.Control.Utils.Constants.USER_TABLE;

public class Utils {

    public final static String TAG = "Utils";
    public static SweetAlertDialog pDialog;

    public static boolean isInternetConnected(Context ctx) {
        if (ctx != null) {
            ConnectivityManager connectivityMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityMgr != null) {
                NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void launchActivity(Context context, Class classToGo, Bundle bundle) {
        Intent intent = new Intent(context, classToGo);
        if (bundle != null)
            intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void showDialog(Context context,String title, String message) {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        pDialog.setTitleText(title);
        pDialog.setContentText(message);
        pDialog.setConfirmText("OK");
        pDialog.show();
    }

    public static void showError(Context context,String title, String message) {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText(title);
        pDialog.setContentText(message);
        pDialog.setConfirmText("OK");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                pDialog.dismiss();
            }
        });
        pDialog.show();
    }

    public static void showLoading(Activity c) {
        hideDialog();
        pDialog = new SweetAlertDialog(c, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#0EAE95"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public static void hideDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            try {
                pDialog.dismiss();
                pDialog = null;
            } catch (Exception e) {
                pDialog = null;
                Log.e(TAG, "Utils:: hideProgressDialog: ", e);
            }
        }
    }

    public static void sendVerCode(String phoneNumber, PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacl) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                callbacl);        // OnVerificationStateChangedCallbacks
    }

    public static void signInWithPhoneAuthCredential(final Activity context, final PhoneAuthCredential credential) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("mob", "signInWithCredential:success");
                            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show();

                            //FirebaseUser user = task.getResult().getUser();

                            SharedValues.saveValue(context, CODE_SP, credential.getSmsCode());
                            startUser(context);
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("mob", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }

                            Utils.showError(context, "Invalid Code", "The Code Entered is Invalid");
                        }
                    }
                });
    }

    public static void startUser(final Activity context) {
        String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        DatabaseController.getElement(USER_TABLE, phone.substring(2, phone.length()), User.class, new onEvent() {
            @Override
            public void onStart(Object object) {
                Utils.showLoading(context);
            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                Utils.hideDialog();
                User u = (User) object;
                switch (u.getType()) {
                    case Admin:
                        Toast.makeText(context, "Admin User", Toast.LENGTH_SHORT).show();
                        Utils.launchActivity(context, HomeActivity.class, null);
                        context.finish();
                        break;
                    case Doctor:
                        Toast.makeText(context, "Doctor User", Toast.LENGTH_SHORT).show();
                        Utils.launchActivity(context, HomeDoctorActivity.class, null);
                        context.finish();
                        break;
                    case Patient:
                        Toast.makeText(context, "Patient User", Toast.LENGTH_SHORT).show();
                        Utils.launchActivity(context, HomePatientActivity.class, null);
                        context.finish();
                        break;
                    default:
                        Toast.makeText(context, "Unknown User", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static String getStrings(List<String> days) {
        String res = "";
        for (int i = 0; i < days.size() - 1; i++) {
            res += days.get(i) + ", ";
        }
        res += days.get(days.size() - 1);
        return res;
    }

    public static ArrayList<String> getList(String s) {
        String[] from = s.split(",");
        ArrayList<String> res = new ArrayList<>();
        for (String x : from) {
            res.add(x);
        }
        return res;
    }

    public static String getPhone() {
        String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        return phone.substring(2, phone.length());
    }

    public static void setPhoto(Activity context, String url, ImageView photo) {
        try {
            Glide.with(context).load(url).placeholder(getImage(context, PLACEHOLDER_IMG)).into(photo);

        } catch (Exception e) {
            Glide.with(context).load(getImage(context, PLACEHOLDER_IMG)).into(photo);
            e.printStackTrace();
        }
    }

    public static int getImage(Activity activity, String imageName) {

        int drawableResourceId = activity.getResources().getIdentifier(imageName, "drawable", activity.getPackageName());

        return drawableResourceId;
    }

}