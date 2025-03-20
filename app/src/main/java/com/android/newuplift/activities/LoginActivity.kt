package com.android.newuplift.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.newuplift.R
import com.android.newuplift.database.DatabaseHelper
import com.android.newuplift.database.QuoteDao
import com.android.newuplift.utility.AuthManager

import com.android.newuplift.utility.stringed

class LoginActivity : AppCompatActivity() {
    private lateinit var quoteDao: QuoteDao
    private lateinit var quoteDatabase : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        //db setupp
        quoteDatabase = DatabaseHelper(this)
        quoteDao = QuoteDao(quoteDatabase.writableDatabase)

        val username = findViewById<EditText>(R.id.login_et_user)
        val password = findViewById<EditText>(R.id.login_et_password)
        val regis = findViewById<TextView>(R.id.registerTextView)
        val btnLogin = findViewById<Button>(R.id.login_btn)


        btnLogin.setOnClickListener {

            val username = username.stringed().trim()
            val password = password.stringed().trim()

            if (username.isEmpty()) {
                Toast.makeText(this, "Username cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val userId = quoteDao.authenticateUser(username,password)
            if(userId != null){
                AuthManager.login(userId, this)
                Toast.makeText(this,"Logging in...", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(this, "Invalid username or password.", Toast.LENGTH_SHORT).show()
            }
        }
        regis.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }


    }
}