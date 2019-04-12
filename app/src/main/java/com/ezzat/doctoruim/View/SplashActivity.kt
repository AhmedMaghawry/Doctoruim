package com.ezzat.doctoruim.View

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import com.ezzat.doctoruim.Control.Utils.Constants.SPLASH_TIME_OUT
import com.ezzat.doctoruim.Control.Utils.Utils
import com.ezzat.doctoruim.R

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            Utils.launchActivity(applicationContext, MainActivity::class.java, null)
            finish()
        }, SPLASH_TIME_OUT)
    }
}
