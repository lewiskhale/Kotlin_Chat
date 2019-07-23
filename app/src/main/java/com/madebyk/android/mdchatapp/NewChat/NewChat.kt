package com.madebyk.android.mdchatapp.NewChat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.firebase.ui.auth.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.madebyk.android.mdchatapp.DataModel.UserInfo
import com.madebyk.android.mdchatapp.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_chat.*
import kotlinx.android.synthetic.main.chats_row.view.*
import kotlinx.android.synthetic.main.fragment_chats.*

class NewChat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat)
        fetchUsers()
    }

    fun fetchUsers(){
        val adapter = GroupAdapter<ViewHolder>()
        val db = FirebaseFirestore.getInstance().collection("users")
            db.get()
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        for(document in it.result!!){
                            if(document != null){
                                val user = document.toObject(UserInfo::class.java)
                                it.getResult()
                                adapter.add(UserItem(user))
                            }
                        }
        newchat_recyclerview.adapter = adapter
                    }
                }
        }
    }

class UserItem(val user: UserInfo): Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chats_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.newchat_username.text = user.username
        Glide
            .with(viewHolder.itemView.context)
            .load(R.drawable.ic_person_outline_black_24dp)
            .into(viewHolder.itemView.newchat_imageView)

    }

}