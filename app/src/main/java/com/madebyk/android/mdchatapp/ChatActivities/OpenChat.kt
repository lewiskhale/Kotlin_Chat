package com.madebyk.android.mdchatapp.ChatActivities

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.madebyk.android.mdchatapp.DataModel.*
import com.madebyk.android.mdchatapp.R
import com.madebyk.android.mdchatapp.Utils.ChatFromItem
import com.madebyk.android.mdchatapp.Utils.ChatToItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_open_chat.*

class OpenChat : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()
    var hasText: Boolean = false
    lateinit var user: UserInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_chat)
        window.enterTransition
        user = intent.getParcelableExtra<UserInfo>(NewChat.USER_KEY)

        open_chat_username.text = user.username

        if (user.avatar == null)
            Glide.with(applicationContext).load(R.drawable.avatar).into(open_chat_userImage)
        else
            Glide.with(applicationContext).load(user.avatar).into(open_chat_userImage)

        //open_chat_recyclerview.adapter = adapter

        listeners()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        window.exitTransition
        supportFinishAfterTransition()
    }

    fun listeners() {
        open_chat_sendButton.setOnClickListener {
            sendMessage()
        }

        open_chat_back.setOnClickListener {
            supportFinishAfterTransition()
            finish()
        }

        open_chat_emoji_button.setOnClickListener {
            //TODO select an emoji
        }

        open_chat_attach.setOnClickListener {
            //TODO selects a file to attach
        }

        open_chat_textinput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (open_chat_textinput.text.toString() == "") {
                    hasText = false
                    open_chat_sendButton.setImageResource(R.drawable.voice)
                } else if (hasText == false && open_chat_textinput.text.toString() != "") {
                    hasText = true
                    open_chat_sendButton.setImageResource(R.drawable.send)
                }
            }
        })

        listenForMessages()
    }

    fun listenForMessages(){
        val db = FirebaseFirestore.getInstance().collection("messages")
        db.addSnapshotListener{ querySnapshot, firebaseFirestoreException->
            if(firebaseFirestoreException == null){
                return@addSnapshotListener
            }
            if(querySnapshot != null && !querySnapshot.isEmpty) {
                for (dc in querySnapshot.documents) {
                    val message = dc.toObject(ChatMessage::class.java)
                    if (message == null) {
                        Toast.makeText(applicationContext, "message is null", Toast.LENGTH_SHORT).show()
                    }
                    if (message!!.getSender().equals(FirebaseAuth.getInstance().uid.toString()))
                        adapter.add(ChatToItem(message))
                    else
                        adapter.add(ChatFromItem(message))
                }
            }
        }
        open_chat_recyclerview.adapter = adapter
    }

    class ChatMessage(val to:String, val from:String, val text: String, val time:Long){
        constructor(): this("","", "", -1)

        fun getSender():String{
            return from
        }
    }

    private fun sendMessage() {
        val message = open_chat_textinput.text.toString()
        val sender = FirebaseAuth.getInstance().uid.toString()
        val receiver = user.id
        val time = System.currentTimeMillis()

        val messageData = ChatMessage(receiver, sender, message, time)
        val db = FirebaseFirestore.getInstance().collection("messages")
        db.add(messageData).addOnCompleteListener {
            if (it.isSuccessful) {
                open_chat_textinput.setText("")
                val view = this.currentFocus
                if(view != null){
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            } else {
                Snackbar.make(
                    (applicationContext as Activity).window.decorView, "There was an error", Snackbar.LENGTH_SHORT).show()
            }
        }
        listenForMessages()
    }
}
