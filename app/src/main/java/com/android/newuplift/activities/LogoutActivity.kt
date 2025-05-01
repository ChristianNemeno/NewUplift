package com.android.newuplift.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.newuplift.R

class LogoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_logout)


        val lgBtn = findViewById<Button>(R.id.logout_btn_login)
        val rgBtn = findViewById<Button>(R.id.logout_btn_register)




        lgBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()

        }

        rgBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

    }
}