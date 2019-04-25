package com.ezzat.doctoruim.View;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ezzat.doctoruim.Control.UserCreator;
import com.ezzat.doctoruim.Control.Utils.SharedValues;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Control.onEvent;
import com.ezzat.doctoruim.Model.User;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.List;

import com.ezzat.doctoruim.R;
import com.pepperonas.materialdialog.MaterialDialog;
import com.pepperonas.materialdialog.model.LicenseInfo;

import static com.ezzat.doctoruim.Control.Utils.Constants.VERF_SP;
import static com.ezzat.doctoruim.Control.Utils.Utils.signInWithPhoneAuthCredential;

/**
 * A login screen that offers login via email/password.
 */
public class SignUpActivity extends AppCompatActivity {

    private EditText nameView, passwordView, cPasswordView, phoneView;
    private Button signupView;
    private SignUpActivity self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameView = findViewById(R.id.name);
        passwordView = findViewById(R.id.password);
        cPasswordView = findViewById(R.id.c_password);
        phoneView = findViewById(R.id.phone);
        signupView = findViewById(R.id.signup);

        self = this;

        signupView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptReg();
            }
        });

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptReg() {

        // Store values at the time of the login attempt.
        final String password = passwordView.getText().toString();
        String cPassword = cPasswordView.getText().toString();
        final String name = nameView.getText().toString();
        final String phone = phoneView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(cPassword) && !password.equals(cPassword)) {
            cPasswordView.setError("Passwords are different");
            focusView = cPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
            nameView.setError("Name is required");
            focusView = nameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(phone)) {
            phoneView.setError("Phone is required");
            focusView = phoneView;
            cancel = true;
        }
        if (phone.length() != 11) {
            phoneView.setError("Phone is Incorrect");
            focusView = phoneView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            new MaterialDialog.Builder(self)
                    .title("Doctoriom licenses :")
                    .licenseDialog(getLicenseInfos())
                    .positiveText("Agree")
                    .buttonCallback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            startRegProcess(name, password, phone);
                        }
                    })
                    .show();
        }
    }

    private void startRegProcess(final String name, final String password, final String phone) {
        UserCreator mAuthTask = new UserCreator(new onEvent() {
            @Override
            public void onStart(Object object) {
                Utils.showLoading(self);
            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                Utils.hideDialog();
                String res = (String) object;

                Toast.makeText(SignUpActivity.this, "The Code Comp : " + res, Toast.LENGTH_SHORT).show();

                showCodeEnter(res, name, password, phone);
            }
        }, this);
        mAuthTask.execute(name, password, phone);
    }

    private void showCodeEnter(final String codeNum, final String name, final String password, final String phone) {
        new MaterialDialog.Builder(this)
                .customView(R.layout.view_code)
                .positiveText("Confirm")
                .negativeText("Cancel")
                .buttonCallback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        EditText code = dialog.findViewById(R.id.code);
                        if (codeNum != null)
                            code.setText(codeNum);
                        if (TextUtils.isEmpty(code.getText().toString())) {
                            Utils.showError(SignUpActivity.this, "Invalid Code", "The Code Entered is invalid");
                            Utils.hideDialog();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Login Succ", Toast.LENGTH_SHORT).show();
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(SharedValues.getValueS(getApplicationContext(), VERF_SP), code.getText().toString());

                            //signing the user
                            signInWithPhoneAuthCredential(SignUpActivity.this, credential);
                            User user = new User(name, password, phone);
                            Boolean b = user.addUser();
                            if (b)
                                Toast.makeText(SignUpActivity.this, "User Added Successfully", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(SignUpActivity.this, "User Add Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }

    private List<LicenseInfo> getLicenseInfos() {
        List<LicenseInfo> licenseInfos = new ArrayList<>();

        licenseInfos.add(new LicenseInfo(
                "Standard Library",
                "Copyright (c) 2016 John Doe",
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                        "you may not use this file except in compliance with the License.\n" +
                        "You may obtain a copy of the License at\n" +
                        "\n" +
                        "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "\n" +
                        "Unless required by applicable law or agreed to in writing, software\n" +
                        "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                        "See the License for the specific language governing permissions and\n" +
                        "limitations under the License."));

        licenseInfos.add(new LicenseInfo(
                "Default Library",
                "Copyright (c) 2015 Jane Doe",
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                        "you may not use this file except in compliance with the License.\n" +
                        "You may obtain a copy of the License at\n" +
                        "\n" +
                        "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "\n" +
                        "Unless required by applicable law or agreed to in writing, software\n" +
                        "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                        "See the License for the specific language governing permissions and\n" +
                        "limitations under the License."));

        licenseInfos.add(new LicenseInfo(
                "Common Library",
                "Copyright (c) 2014 James Doe",
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                        "you may not use this file except in compliance with the License.\n" +
                        "You may obtain a copy of the License at\n" +
                        "\n" +
                        "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "\n" +
                        "Unless required by applicable law or agreed to in writing, software\n" +
                        "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                        "See the License for the specific language governing permissions and\n" +
                        "limitations under the License."));

        return licenseInfos;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.hideDialog();
    }

}

