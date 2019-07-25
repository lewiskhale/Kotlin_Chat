package com.madebyk.android.mdchatapp.Utils

import com.madebyk.android.mdchatapp.ChatActivities.OpenChat
import com.madebyk.android.mdchatapp.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.open_chat_to.view.*

class ChatToItem(val message: OpenChat.ChatMessage): Item<ViewHolder>() {

    override fun getLayout():Int = R.layout.open_chat_to

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.open_chat_to_text.text = message.text
    }
}
