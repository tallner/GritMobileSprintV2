package com.tallner.gritmobilesprintv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar

class NewUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        val toolbar: Toolbar = findViewById(R.id.toolbar_new_user)
        setSupportActionBar(toolbar)

        supportActionBar!!.title = "New user"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val i = Intent(this@NewUserActivity,StartActivity::class.java)
            startActivity(i)
            finish()
        }

    }
}