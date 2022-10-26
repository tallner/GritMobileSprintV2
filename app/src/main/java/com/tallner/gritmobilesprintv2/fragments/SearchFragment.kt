package com.tallner.gritmobilesprintv2.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tallner.gritmobilesprintv2.R
import com.tallner.gritmobilesprintv2.UserAdapter
import com.tallner.gritmobilesprintv2.models.User
import kotlinx.android.synthetic.main.fragment_search.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {

    private var useradapter : UserAdapter?=null
    private var mUsers:List<User>?=null
    private var databaseURL : String = "https://test-8e78e-default-rtdb.europe-west1.firebasedatabase.app/"
    private var recyclerview : RecyclerView?=null
    private var searchtext : EditText?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_search, container, false)

        recyclerview = view.findViewById(R.id.searchlist)
        recyclerview!!.setHasFixedSize(true)
        recyclerview!!.layoutManager = LinearLayoutManager(context)
        searchtext = view.findViewById(R.id.searchFriends)

        mUsers = ArrayList()
        getAllUsers()

        searchtext!!.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(cs: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchForUsers(cs.toString().lowercase())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        return view
    }

    private fun getAllUsers(){
        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val refUser = FirebaseDatabase.getInstance(databaseURL).reference.child("Users")
        refUser.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (mUsers as ArrayList<User>).clear()

                if (searchtext!!.text.toString().equals(""))
                {
                    for (item in snapshot.children){
                        val user:User?=item.getValue(User::class.java)
                        if (!(user!!.uid.equals(firebaseUserID))){
                            (mUsers as ArrayList<User>).add(user)
                        }
                    }
                    useradapter = UserAdapter(context!!,mUsers!!,false)
                    recyclerview!!.adapter = useradapter
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        )
    }


    private fun searchForUsers (str: String){
        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val queryUsers = FirebaseDatabase.getInstance(databaseURL).reference
            .child("Users")
            .orderByChild("search")
            .startAt(str)
            .endAt(str+"\uf8ff")

        queryUsers.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (mUsers as ArrayList<User>).clear()
                for (item in snapshot.children){
                    val user:User?=item.getValue(User::class.java)
                    if (!(user!!.uid.equals(firebaseUserID))){
                        (mUsers as ArrayList<User>).add(user)
                    }
                }
                useradapter = UserAdapter(context!!,mUsers!!,false)
                recyclerview!!.adapter = useradapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        )

    }

}