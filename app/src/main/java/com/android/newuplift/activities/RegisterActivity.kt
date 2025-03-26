package com.android.newuplift.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.newuplift.R
import com.android.newuplift.utility.UserAccount
import com.android.newuplift.database.DatabaseHelper
import com.android.newuplift.database.QuoteDao
import com.android.newuplift.utility.isBlank
import com.android.newuplift.utility.stringed

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
            if( name.isBlank()
                || username.isBlank()
                || pass.isBlank()
                || email.isBlank()
                || adress.isBlank()
                || number.isBlank()) {

                Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else if(quoteDao.isUsernameExists(name.stringed())){
                Toast.makeText(this, "Username taken", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val user_acc  = UserAccount(name.stringed(),
                                        username.stringed(),
                                        pass.stringed(),
                                        email.stringed(),
                                        adress.stringed(),
                                        number.stringed())

            val res = quoteDao.insertAccount(user_acc)

            if(res > 0){
                Toast.makeText(this, "Registration successful! : $res", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Registration FAILED! : $res", Toast.LENGTH_SHORT).show()
            }


            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
}