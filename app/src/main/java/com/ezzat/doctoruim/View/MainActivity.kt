package com.ezzat.doctoruim.View

import android.app.Activity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.widget.Button
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog
import com.ezzat.doctoruim.Control.CustomPagerAdapter
import com.ezzat.doctoruim.Control.UserLoginTask
import com.ezzat.doctoruim.Control.Utils.Utils
import com.ezzat.doctoruim.Control.onEvent
import com.ezzat.doctoruim.R
import com.google.firebase.auth.FirebaseAuth
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
                            val isSucc : Boolean = `object` as Boolean

                            Utils.hideDialog()

                            if(isSucc)
                                Utils.launchActivity(applicationContext, HomeActivity::class.java, null)
                            else
                                Utils.showError(self, "Login Failed", "Login Failed please try again")
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
                        loginTask.execute(email.toString(), password.toString())
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
}
