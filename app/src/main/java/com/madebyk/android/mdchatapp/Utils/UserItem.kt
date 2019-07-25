package com.madebyk.android.mdchatapp.Utils

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.madebyk.android.mdchatapp.DataModel.UserInfo
import com.madebyk.android.mdchatapp.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chats_row.view.*

class UserItem(val user: UserInfo): Item<ViewHolder>(){

    lateinit var image: ImageView
    lateinit var name: TextView

    override fun getLayout() = R.layout.chats_row

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.newchat_username.text = user.username

        image = viewHolder.itemView.findViewById(R.id.newchat_imageView)
        name = viewHolder.itemView.findViewById(R.id.newchat_username)

        Glide
            .with(viewHolder.itemView.context)
            .load(R.drawable.ic_person_outline_black_24dp)
            .into(image)

    }
}