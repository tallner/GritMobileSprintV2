package com.tallner.gritmobilesprintv2.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.tallner.gritmobilesprintv2.R
import com.tallner.gritmobilesprintv2.StartActivity
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onStart() {
        super.onStart()
        btn_signout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            requireActivity().run{
                startActivity(Intent(this, StartActivity::class.java))
                finish()
            }
        }
    }

}