package com.tallner.gritmobilesprintv2

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tallner.gritmobilesprintv2.models.User
import kotlinx.android.synthetic.main.activity_main.*

class UserAdapter(
    mContext: Context,
    mUserList:List<User>,
    isChatCheck:Boolean
    ) : RecyclerView.Adapter<UserAdapter.ViewHolder?>()
{
    private val mContext:Context
    private val mUserList:List<User>
    private val isChatCheck:Boolean

    init {
        this.mUserList = mUserList
        this.mContext = mContext
        this.isChatCheck = isChatCheck
    }

    override fun onCreateViewHolder(viewgroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.friend_search_layout,viewgroup,false)
        return UserAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: User=mUserList[position]
        holder.userName.text = user!!.username
        Picasso.get().load(user.profile).placeholder(R.drawable.profile).into(holder.profileImageView)

        holder.userName.setOnClickListener{
            Log.i("mylog",position.toString())
        }
    }

    override fun getItemCount(): Int {
        return mUserList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var userName : TextView
        var profileImageView : ImageView
        var onlineStatus : ImageView
        var offlineStatus : ImageView
        var lastMessage : TextView

        init {
            userName = itemView.findViewById(R.id.tv_username)
            profileImageView = itemView.findViewById(R.id.image_profile)
            onlineStatus = itemView.findViewById(R.id.image_online)
            offlineStatus = itemView.findViewById(R.id.image_offline)
            lastMessage = itemView.findViewById(R.id.tv_last_message)

        }
    }
}