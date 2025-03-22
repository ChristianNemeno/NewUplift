package com.android.newuplift.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.newuplift.R

class LogoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_logout)


        val lgBtn = findViewById<Button>(R.id.logout_btn_login)
        val rgBtn = findViewById<Button>(R.id.logout_btn_register)


        lgBtn.background = ContextCompat.getDrawable(this, R.drawable.button_login_background)
        rgBtn.background = ContextCompat.getDrawable(this, R.drawable.button_register_background)

        lgBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))

        }

        rgBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }
}