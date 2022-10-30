package com.tallner.gritmobilesprintv2.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.protobuf.Value
import com.squareup.picasso.Picasso
import com.tallner.gritmobilesprintv2.R
import com.tallner.gritmobilesprintv2.adapters.UserAdapter
import com.tallner.gritmobilesprintv2.models.User
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment() {

    private var useradapter : UserAdapter?=null
    private var mUsers:List<User>?=null
    private var databaseURL : String = "https://test-8e78e-default-rtdb.europe-west1.firebasedatabase.app/"
    private var recyclerview : RecyclerView?=null
    private var firebaseUser:FirebaseUser?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_chat, container, false)

        var userID = requireArguments().getString("USERID").toString()
        getChatUser(userID)


        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun getChatUser(userID:String){
        if (userID.equals(null)) return

        //firebaseUser = FirebaseAuth.getInstance().currentUser
        val refUser = FirebaseDatabase
            .getInstance(databaseURL)
            .getReference("Users")
            .child(userID)



        refUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val user:User? = snapshot.getValue(User::class.java)

                if (user != null){
                    tv_username.text = user.username
                    if (user.profile == "") {
                        icon_user.setImageResource(R.drawable.profile)
                    } else {
                        if (user != null) {
                            Picasso.get().load(user.profile).placeholder(R.drawable.profile)
                                .into(icon_user)
                        }
                    }
                }
            }


            override fun onCancelled(error: DatabaseError) {
            }


        })

    }

}