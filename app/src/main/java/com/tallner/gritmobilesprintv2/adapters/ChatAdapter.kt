package com.tallner.gritmobilesprintv2.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import com.tallner.gritmobilesprintv2.R
import com.tallner.gritmobilesprintv2.models.Chat
import com.tallner.gritmobilesprintv2.models.User

class ChatAdapter(
    myContext: Context,
    myChatList:List<Chat>,
    isChatCheck:Boolean
    ) : RecyclerView.Adapter<ChatAdapter.ViewHolder?>()
{
    private val myContext:Context
    private val myChatList:List<Chat>
    private val isChatCheck:Boolean


    init {
        this.myChatList = myChatList
        this.myContext = myContext
        this.isChatCheck = isChatCheck
    }

    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1
    private var firebaseUser:FirebaseUser?=null

    override fun onCreateViewHolder(viewgroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            if (viewType == MESSAGE_TYPE_LEFT) {
                LayoutInflater.from(myContext).inflate(R.layout.chat_left_layout,viewgroup,false)
            } else {
                LayoutInflater.from(myContext).inflate(R.layout.chat_right_layout,viewgroup,false)
            }
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = myChatList[position]
        holder.message.text = chat.message

    }

    override fun getItemCount(): Int {
        return myChatList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var message : TextView
        var profileImageView : ImageView

        init {
            message = itemView.findViewById(R.id.tv_chatmessage)
            profileImageView = itemView.findViewById(R.id.image_profile)
        }
    }

    // decides what view is showing the message, right is you and left is the other user
    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        return if (myChatList[position].senderID == firebaseUser!!.uid) {
            MESSAGE_TYPE_RIGHT
        } else {
            MESSAGE_TYPE_LEFT
        }

    }
}