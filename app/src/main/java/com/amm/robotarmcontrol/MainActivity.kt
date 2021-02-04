package com.amm.robotarmcontrol

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import java.util.*
import android.os.Handler;

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed({
            var moveToAuth = Intent(applicationContext,BiometricAuthentication::class.java)
            startActivity(moveToAuth)
            finish()
        }, 3000)
    }

}