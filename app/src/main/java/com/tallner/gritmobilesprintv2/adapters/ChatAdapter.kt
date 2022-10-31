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
import com.squareup.picasso.Picasso
import com.tallner.gritmobilesprintv2.R
import com.tallner.gritmobilesprintv2.models.Chat
import com.tallner.gritmobilesprintv2.models.User

class ChatAdapter(
    myContext: Context,
    myChatList:List<Chat>,
    isChatCheck:Boolean,
    listener: OnItemClickListener
    ) : RecyclerView.Adapter<ChatAdapter.ViewHolder?>()
{
    private val myContext:Context
    private val myChatList:List<Chat>
    private val isChatCheck:Boolean
    private val listener:OnItemClickListener


    init {
        this.myChatList = myChatList
        this.myContext = myContext
        this.isChatCheck = isChatCheck
        this.listener = listener
    }

    override fun onCreateViewHolder(viewgroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(myContext).inflate(R.layout.friend_search_layout,viewgroup,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: User=myUserList[position]
        holder.userName.text = user!!.username
        Picasso.get().load(user.profile).placeholder(R.drawable.profile).into(holder.profileImageView)
    }

    override fun getItemCount(): Int {
        return myUserList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnClickListener{
        var userName : TextView
        var profileImageView : ImageView
        var onlineStatus : ImageView
        var offlineStatus : ImageView
        var lastMessage : TextView
        var layoutFriend : ConstraintLayout

        init {
            userName = itemView.findViewById(R.id.tv_username)
            profileImageView = itemView.findViewById(R.id.image_profile)
            onlineStatus = itemView.findViewById(R.id.image_online)
            offlineStatus = itemView.findViewById(R.id.image_offline)
            lastMessage = itemView.findViewById(R.id.tv_last_message)
            layoutFriend = itemView.findViewById(R.id.layoutFriends)

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position:Int = adapterPosition
            val user: User=myUserList[position]
            val userID:String = user.uid
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position,userID)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int, userID:String)
    }
}