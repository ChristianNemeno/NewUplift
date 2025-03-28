package com.android.newuplift.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.android.newuplift.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashIcon = findViewById<ImageView>(R.id.splash_icon)

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val scale = AnimationUtils.loadAnimation(this, R.anim.scale)

        splashIcon.startAnimation(fadeIn)
        splashIcon.startAnimation(scale)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}