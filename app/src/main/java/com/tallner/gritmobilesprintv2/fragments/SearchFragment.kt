package com.tallner.gritmobilesprintv2.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tallner.gritmobilesprintv2.R
import com.tallner.gritmobilesprintv2.adapters.UserAdapter
import com.tallner.gritmobilesprintv2.models.User
import kotlin.collections.ArrayList

class SearchFragment : Fragment(), UserAdapter.OnItemClickListener {

    private var useradapter : UserAdapter?=null
    private var myUsers:List<User>?=null
    private var databaseURL : String = "https://test-8e78e-default-rtdb.europe-west1.firebasedatabase.app/"
    private var recyclerview : RecyclerView?=null
    private var searchtext : EditText?=null
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
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_search, container, false)

        recyclerview = view.findViewById(R.id.searchlist)
        recyclerview!!.setHasFixedSize(true)
        recyclerview!!.layoutManager = LinearLayoutManager(context)
        searchtext = view.findViewById(R.id.searchFriends)

        myUsers = ArrayList()
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

    override fun onItemClick(position: Int, userID: String) {

        // changes fragment and the bottom navigation
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)?.selectedItemId = R.id.chatFragment
        sharedPreferences = context?.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)!!


        // change fragment but not the bottom navigation
        // this might be needed to pass parameters?
        val bundle = Bundle()
        bundle.putString("USERID", userID)
        val fragmentChat = ChatFragment()
        fragmentChat.arguments = bundle
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragmentChat)
            commit()}

        // create variable for editor of shared prefs
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        // adding our username and pwd to shared prefs
        editor.putString(USERID_KEY, userID)

        // apply changes to our shared prefs.
        editor.apply()

    }

    private fun getAllUsers(){
        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val refUser = FirebaseDatabase.getInstance(databaseURL).reference.child("Users")

        refUser.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (myUsers as ArrayList<User>).clear()

                if (searchtext!!.text.toString().equals(""))
                {
                    for (item in snapshot.children){
                        val user:User?=item.getValue(User::class.java)
                        if (!(user!!.uid.equals(firebaseUserID))){
                            (myUsers as ArrayList<User>).add(user)
                        }
                    }
                    useradapter = UserAdapter(context!!,myUsers!!,false,this@SearchFragment)
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
                (myUsers as ArrayList<User>).clear()
                for (item in snapshot.children){
                    val user:User?=item.getValue(User::class.java)
                    if (!(user!!.uid.equals(firebaseUserID))){
                        (myUsers as ArrayList<User>).add(user)
                    }
                }
                useradapter = UserAdapter(context!!,myUsers!!,false,this@SearchFragment)
                recyclerview!!.adapter = useradapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        )

    }
}