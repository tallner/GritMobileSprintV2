package com.tallner.gritmobilesprintv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    var firebaseUser : FirebaseUser?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        btn_login.setOnClickListener {
            val i = Intent(this@StartActivity,LogInActivity::class.java)
            startActivity(i)
      //      finish()
        }
        btn_register.setOnClickListener {
            val i = Intent(this@StartActivity,NewUserActivity::class.java)
            startActivity(i)
      //      finish()
        }


    }

    override fun onStart() {
        super.onStart()

        firebaseUser = FirebaseAuth.getInstance().currentUser

        //if there is a user logged in then go to mainactivity
        if (firebaseUser != null) {
            val i = Intent(this@StartActivity,MainActivity::class.java)
            startActivity(i)
           // finish()
        }
    }
}