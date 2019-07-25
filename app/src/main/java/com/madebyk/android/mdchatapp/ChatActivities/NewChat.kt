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
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair


class NewChat : AppCompatActivity() {

    companion object{
        val USER_KEY = "USER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat)
        fetchUsers()
    }

    fun fetchUsers(){
        val adapter = GroupAdapter<ViewHolder>()
        val db = FirebaseFirestore.getInstance().collection("users")
        db.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if(querySnapshot != null && !querySnapshot?.isEmpty){
                querySnapshot.forEach {
                    val user = it.toObject(UserInfo::class.java)
                    adapter.add(UserItem(user))
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
                newchat_recyclerview.adapter = adapter
            }
        }
    }
}