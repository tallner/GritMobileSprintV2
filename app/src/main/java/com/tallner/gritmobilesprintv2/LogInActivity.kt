package com.tallner.gritmobilesprintv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    private lateinit var refUsers : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val toolbar: Toolbar = findViewById(R.id.toolbar_login)
        setSupportActionBar(toolbar)

        supportActionBar!!.title = "LogIn"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val i = Intent(this@LogInActivity,StartActivity::class.java)
            startActivity(i)
            finish()
        }

        mAuth = FirebaseAuth.getInstance()

        btn_login.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val password:String = password_login.text.toString()
        val email:String = email_login.text.toString()

        if (email.equals("") || password.equals(""))
        {
            Toast.makeText(this@LogInActivity,"Check empty credentials", Toast.LENGTH_LONG).show()
        }
        else
        {
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener{ task ->
                if (task.isSuccessful){

                    val i = Intent(this@LogInActivity,MainActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)
                    finish()
                }
                else{
                    Toast.makeText(
                        this@LogInActivity,
                        "Error: " + task.exception!!.message.toString(),
                        Toast.LENGTH_LONG).show()
                }

            }
        }
    }
}