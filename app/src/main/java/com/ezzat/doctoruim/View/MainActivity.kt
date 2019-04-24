package com.ezzat.doctoruim.View

import android.app.Activity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.afollestad.materialdialogs.MaterialDialog
import com.ezzat.doctoruim.Control.CustomPagerAdapter
import com.ezzat.doctoruim.Control.UserLoginTask
import com.ezzat.doctoruim.Control.Utils.Constants.VERF_SP
import com.ezzat.doctoruim.Control.Utils.SharedValues
import com.ezzat.doctoruim.Control.Utils.Utils
import com.ezzat.doctoruim.Control.Utils.Utils.signInWithPhoneAuthCredential
import com.ezzat.doctoruim.Control.onEvent
import com.ezzat.doctoruim.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import me.relex.circleindicator.CircleIndicator



class MainActivity : Activity() {

    var self : MainActivity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        self = this@MainActivity
        val viewPager = findViewById<ViewPager>(R.id.viewpager)
        viewPager.adapter = CustomPagerAdapter(this)

        val indicator = findViewById<CircleIndicator>(R.id.indicator)
        indicator.setViewPager(viewPager)

        val dialog = MaterialDialog.Builder(this)
                .customView(R.layout.view_login, true)
                .positiveText(R.string.login)
                .onPositive { dialog, _ ->
                    val email : EditText = dialog.customView?.findViewById(R.id.email)!!
                    val password : EditText = dialog.customView?.findViewById(R.id.password)!!
                    var event : onEvent = object : onEvent{
                        override fun onStart(`object`: Any?) {
                            Utils.showLoading(self)
                        }

                        override fun onProgress(`object`: Any?) {}

                        override fun onEnd(`object`: Any?) {
                            Utils.hideDialog()
                            val res = `object` as String?

                            showCodeEnter(res)
                        }

                    }

                    var good = true

                    if (email.toString().equals("")) {
                        email.setError("please add Phone number")
                        good = false
                    }

                    if (password.toString().equals("")) {
                        password.setError("please add password")
                        good = false
                    }

                    if (good) {
                        var loginTask = UserLoginTask(self, event)
                        loginTask.execute(email.text.toString(), password.text.toString())
                    }
                }

        val login = findViewById<Button>(R.id.login)
        login.setOnClickListener {
            dialog.show()
        }

        val sigup = findViewById<Button>(R.id.signup)
        sigup.setOnClickListener {
            Utils.launchActivity(applicationContext, SignUpActivity::class.java, null)
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null)
            Utils.launchActivity(applicationContext, HomeActivity::class.java, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        Utils.hideDialog()
    }

    private fun showCodeEnter(codeNum: String?) {
        com.pepperonas.materialdialog.MaterialDialog.Builder(this)
                .customView(R.layout.view_code)
                .positiveText("Confirm")
                .negativeText("Cancel")
                .buttonCallback(object : com.pepperonas.materialdialog.MaterialDialog.ButtonCallback() {
                    override fun onPositive(dialog: com.pepperonas.materialdialog.MaterialDialog?) {
                        val code = dialog!!.findViewById<EditText>(R.id.code)
                        if (codeNum != null)
                            code.setText(codeNum)
                        if (TextUtils.isEmpty(code.text.toString())) {
                            Utils.showError(application, "Invalid Code", "The Code Entered is invalid")
                            Utils.hideDialog()
                        } else {
                            Toast.makeText(application, "Login Succ", Toast.LENGTH_SHORT).show()
                            val credential = PhoneAuthProvider.getCredential(SharedValues.getValueS(applicationContext, VERF_SP), code.text.toString())

                            //signing the user
                            signInWithPhoneAuthCredential(self, credential)
                        }
                    }
                }).show()
    }
}
