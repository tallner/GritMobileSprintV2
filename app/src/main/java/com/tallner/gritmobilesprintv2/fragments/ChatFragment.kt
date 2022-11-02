package com.tallner.gritmobilesprintv2.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso
import com.tallner.gritmobilesprintv2.R
import com.tallner.gritmobilesprintv2.adapters.ChatAdapter
import com.tallner.gritmobilesprintv2.adapters.UserAdapter
import com.tallner.gritmobilesprintv2.models.Chat
import com.tallner.gritmobilesprintv2.models.User
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import org.checkerframework.checker.units.qual.C

class ChatFragment : Fragment() {

    private var chatadapter : ChatAdapter?=null
    private lateinit var myChats:List<Chat>
    private var databaseURL : String = "https://test-8e78e-default-rtdb.europe-west1.firebasedatabase.app/"
    private var recyclerview : RecyclerView?=null
    private lateinit var btn_send:ImageButton
    private lateinit var sharedPreferences: SharedPreferences
    private var USERID_KEY = "username"
    private var PREFS_KEY = "prefs"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        myChats = ArrayList()

        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_chat, container, false)

        recyclerview = view.findViewById(R.id.recycler_chat)
        recyclerview!!.layoutManager = LinearLayoutManager(context)
        sharedPreferences = context?.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)!!


        var senderID:String = FirebaseAuth.getInstance().currentUser!!.uid
        var receiverID = requireArguments().getString("USERID").toString()
        if (receiverID=="null") {
            receiverID = sharedPreferences.getString(USERID_KEY, null)!!
        }
        Log.i("mytag", receiverID.toString())


        btn_send = view.findViewById(R.id.btn_sendmessage)
        btn_send.setOnClickListener{

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

        readMessages(senderID,receiverID)

      //  Log.i("mytag", "nr chats " + (myChats.size-1).toString())


        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
/*
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
*/
    private fun sendMessage(senderID:String,receiverID:String,message:String){
        val refChat = FirebaseDatabase.getInstance(databaseURL).reference.child("Chat")


        var hashMap:HashMap<String,String> = HashMap()
        hashMap.put("senderID",senderID)
        hashMap.put("receiverID",receiverID)
        hashMap.put("message",message)

        refChat.push().setValue(hashMap)

    }

    private fun scrollToBottom(){
        //val refChat = FirebaseDatabase.getInstance(databaseURL).getReference("Chat")
        recyclerview?.scrollToPosition(myChats.size-1)
        Log.i("mytag", "nr chats " + (myChats.size-1).toString())
/*
        refChat.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

 */
    }

    private fun readMessages(senderID:String,receiverID:String){
        val refChat = FirebaseDatabase.getInstance(databaseURL).getReference("Chat")

        refChat.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (myChats as ArrayList<Chat>).clear()

                for (item in snapshot.children)
                {
                    val chat = item.getValue(Chat::class.java)
                    if (chat!!.senderID.equals(senderID) && chat.receiverID.equals(receiverID) ||
                        chat!!.senderID.equals(receiverID) && chat.receiverID.equals(senderID))
                    {
                        (myChats as ArrayList<Chat>).add(chat)
                    }

                }
                chatadapter = context?.let { ChatAdapter(it, myChats as ArrayList<Chat>,false) }

                recyclerview!!.adapter = chatadapter
                scrollToBottom()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}