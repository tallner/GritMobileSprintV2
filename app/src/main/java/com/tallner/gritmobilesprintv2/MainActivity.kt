package com.tallner.gritmobilesprintv2


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.tallner.gritmobilesprintv2.models.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var refUser:DatabaseReference?=null
    private var firebaseuser:FirebaseUser?=null
    private var databaseURL : String = "https://test-8e78e-default-rtdb.europe-west1.firebasedatabase.app/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        supportActionBar!!.title = ""

        //initiate the bottom navigation component
        val btmNavView = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        //setup the fragment manager
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController

        //setup the bar on top of the app to get the labels from the fragments
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.chatFragment,
                R.id.searchFragment,
                R.id.settingsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        //setup the navigation bar to be used with the navigation controller
        btmNavView.setupWithNavController(navController)
        btmNavView.selectedItemId


        firebaseuser = FirebaseAuth.getInstance().currentUser
        refUser = FirebaseDatabase.getInstance(databaseURL).reference.child("Users").child(firebaseuser!!.uid)

        // display username and picture on bar
        refUser!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user: User?=snapshot.getValue(User::class.java)
                    tv_username.text = user!!.username
                    Picasso.get().load(user.profile).placeholder(R.drawable.profile).into(icon_user)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

}