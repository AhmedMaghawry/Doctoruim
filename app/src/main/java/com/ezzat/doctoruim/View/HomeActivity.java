package com.ezzat.doctoruim.View;

import android.app.Activity;
import android.os.Bundle;

import com.ezzat.doctoruim.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FirebaseAuth.getInstance().signOut();
    }
}
