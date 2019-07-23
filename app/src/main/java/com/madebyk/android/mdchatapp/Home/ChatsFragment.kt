package com.madebyk.android.mdchatapp.Home


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore

import com.madebyk.android.mdchatapp.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_chats.*

class ChatsFragment(context: Context) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onStart() {
        super.onStart()

        val adapter = GroupAdapter<ViewHolder>()
        fetchUsers()

        chats_recyclerview.adapter = adapter
    }

    fun fetchUsers(){
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .get()
    }
}

//class UserItem: Item<ViewHolder>(){
//    override fun getLayout(): Int {
//        return R.layout.chats_row
//    }
//
//    override fun bind(viewHolder: ViewHolder, position: Int) {
//
//    }
//
//}
