package com.madebyk.android.mdchatapp.ChatActivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import com.madebyk.android.mdchatapp.DataModel.UserInfo
import com.madebyk.android.mdchatapp.R
import com.madebyk.android.mdchatapp.Utils.UserItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_chat.*
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query


class NewChat : AppCompatActivity() {

    companion object{
        val USER_KEY = "USER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat)
        fetchUsers()
        go_back()
    }

    fun fetchUsers(){
        val adapter = GroupAdapter<ViewHolder>()
        adapter.clear()
        val db = FirebaseFirestore.getInstance().collection("users").orderBy("username", Query.Direction.ASCENDING)
        db.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if(querySnapshot != null && !querySnapshot?.isEmpty){
                for(it in querySnapshot!!.documentChanges) {
                    val user = it.document.toObject(UserInfo::class.java)
                    val id = it.document.id
                    val oldIndex = it.oldIndex
                    val newIndex = it.newIndex
                    //TODO fix the updating without duplicating
                    if(id != FirebaseAuth.getInstance().uid){
                        if(oldIndex != newIndex){
                            adapter.add(UserItem(user))
                        }
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val intent = Intent(view.context, OpenChat::class.java)
                    val user = item as UserItem

                    intent.putExtra(USER_KEY, user.user)
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        val image = Pair.create<View, String>(user.image, "user_image")
                        val name = Pair.create<View, String>(user.name, "user_name")
                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, image, name)
                        startActivity(intent, options.toBundle())

                    }
                    else {

                        startActivity(intent)
                    }
                }
//                                    newchat_recyclerview.adapter = adapter
            }
        }
        newchat_recyclerview.adapter = adapter
    }

    fun go_back(){
        newchat_back.setOnClickListener {
            finish()
        }
    }

}