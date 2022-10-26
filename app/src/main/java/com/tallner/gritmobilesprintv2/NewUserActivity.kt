package com.tallner.gritmobilesprintv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_new_user.*

class NewUserActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var refUsers : DatabaseReference
    private var firebaseUserID : String = ""
    private var databaseURL : String = "https://test-8e78e-default-rtdb.europe-west1.firebasedatabase.app/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        val toolbar: Toolbar = findViewById(R.id.toolbar_new_user)
        setSupportActionBar(toolbar)

        supportActionBar!!.title = "Register new user"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            val i = Intent(this@NewUserActivity,StartActivity::class.java)
            startActivity(i)
            finish()
        }

        mAuth = FirebaseAuth.getInstance()
        btn_register.setOnClickListener {
            registerUser()
        }


    }

    private fun registerUser() {
        val username:String = username_register.text.toString()
        val email:String = email_register.text.toString()
        val password:String = password_register.text.toString()

        if (username.equals("") || email.equals("") || password.equals("")){
            Toast.makeText(this@NewUserActivity,"Check credentials",Toast.LENGTH_LONG).show()
        }else{
            mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{ task1 ->
                    if (task1.isSuccessful){

                        firebaseUserID = mAuth.currentUser!!.uid
                        refUsers=FirebaseDatabase.getInstance(databaseURL).reference.child("Users").child(firebaseUserID)

                        Log.i("mylog","Firebase User ID" + firebaseUserID)
                        Log.i("mylog","Firebase ref user" + refUsers.toString())

                        val userHashMap = HashMap<String, Any>()
                        userHashMap["uid"] = firebaseUserID
                        userHashMap["username"] = username
                        userHashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/test-8e78e.appspot.com/o/profile.png?alt=media&token=8b21bd41-84f5-4090-ae89-977f8d58f5c2"
                        userHashMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/test-8e78e.appspot.com/o/cover.jpg?alt=media&token=3b248715-20c4-4d1b-a7e6-e394734d03c8"
                        userHashMap["status"] = "offline"
                        userHashMap["search"] = username.lowercase()
                        userHashMap["facebook"] = "https://m.facebook.com"
                        userHashMap["instagram"] = "https://m.instagram.com"
                        userHashMap["website"] = "https://www.google.com"

                        Log.i("mylog","before ")

                        refUsers.updateChildren(userHashMap).addOnCompleteListener { task2 ->
                            Log.i("mylog", "taask2 result " + task2.result.toString())
                            if (task2.isSuccessful) {
                                Log.i("mylog", "success")
                                val i = Intent(this@NewUserActivity, MainActivity::class.java)
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(i)
                                finish()
                            } else {
                                Log.i("mylog","Exception " + task2.exception!!.message.toString())
                                Toast.makeText(
                                    this@NewUserActivity,
                                    "Error: " + task2.exception!!.message.toString(),
                                    Toast.LENGTH_LONG).show()
                            }
                        }
                        Log.i("mylog","after ")

                }else{
                    Toast.makeText(
                        this@NewUserActivity,
                        "Error: " + task1.exception!!.message.toString(),
                        Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}