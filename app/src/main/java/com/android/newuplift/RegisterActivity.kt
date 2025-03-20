package com.android.newuplift

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var quoteDatabase : DatabaseHelper
    private lateinit var quoteDao: QuoteDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)
        // instantiation of db
        quoteDatabase = DatabaseHelper(this)
        quoteDao = QuoteDao(quoteDatabase.writableDatabase)

        val name = findViewById<EditText>(R.id.etName)
        val username = findViewById<EditText>(R.id.etUsername)
        val pass = findViewById<EditText>(R.id.etPassword)
        val adress = findViewById<EditText>(R.id.etAddress)
        val email = findViewById<EditText>(R.id.etEmail)
        val number = findViewById<EditText>(R.id.etNumber)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            if( name.text.toString().isBlank()
                || username.text.toString().isBlank()
                || pass.text.toString().isBlank()
                || email.text.toString().isBlank()
                ||adress.text.toString().isBlank()
                ||number.text.toString().isBlank()) {

                Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else if(quoteDao.isUsernameExists(name.text.toString())){
                Toast.makeText(this, "Username taken", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // if successful

            // s here is a extension function in util basically just gets the text and toString()
            val user_acc  = UserAccount(name.s(), username.s(), pass.s(), email.s(),adress.s(),number.s())

            quoteDao.insertAccount(user_acc)


            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()


        }

        // continue error guarding and authentication instantiate db and make in DAO search using WHERE CLASUE OK?


    }
}