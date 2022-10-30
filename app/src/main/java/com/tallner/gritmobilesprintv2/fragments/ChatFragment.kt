package com.tallner.gritmobilesprintv2.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.tallner.gritmobilesprintv2.R
import com.tallner.gritmobilesprintv2.adapters.UserAdapter
import com.tallner.gritmobilesprintv2.models.User
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*

class ChatFragment : Fragment() {

    private var useradapter : UserAdapter?=null
    private var mUsers:List<User>?=null
    private var databaseURL : String = "https://test-8e78e-default-rtdb.europe-west1.firebasedatabase.app/"
    private var recyclerview : RecyclerView?=null
    private var firebaseUser:FirebaseUser?=null
    private lateinit var btn_send:ImageButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_chat, container, false)

        var receiverID = requireArguments().getString("USERID").toString()
        getChatUser(receiverID)


        btn_send = view.findViewById(R.id.btn_sendmessage)
        btn_send.setOnClickListener{
            Log.i("mylog", receiverID.toString())

            var senderID:String = FirebaseAuth.getInstance().currentUser!!.uid
            var message:String = edit_sendmsg.text.toString()

            if (message.isEmpty())
            {
                Toast.makeText(activity, "No message", Toast.LENGTH_SHORT).show()
            }
            else if (senderID.equals("null"))
            {
                Toast.makeText(activity, "Who is sending?", Toast.LENGTH_SHORT).show()
            }
            else if (receiverID.equals("null"))
            {
                Toast.makeText(activity, "Who is the receiver?", Toast.LENGTH_SHORT).show()
            }
            else {
                edit_sendmsg.text.clear()
                sendMessage(
                    senderID,
                    receiverID,
                    message
                )
            }
        }


        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun getChatUser(userID:String){
        if (userID.equals(null)) return

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

    private fun sendMessage(senderID:String,receiverID:String,message:String){
        val refChat = FirebaseDatabase.getInstance(databaseURL).reference.child("Chat")


        var hashMap:HashMap<String,String> = HashMap()
        hashMap.put("senderID",senderID)
        hashMap.put("receiverID",receiverID)
        hashMap.put("message",message)

        refChat.push().setValue(hashMap)

    }

}