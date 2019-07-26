package com.madebyk.android.mdchatapp.ChatActivities

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
    lateinit var message_id : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_chat)
        window.enterTransition

        user = intent.getParcelableExtra<UserInfo>(NewChat.USER_KEY)
        checkStrings()

        //Setting up user toolbar info
        open_chat_username.text = user.username
        if (user.avatar == null)
            Glide.with(applicationContext).load(R.drawable.avatar).into(open_chat_userImage)
        else
            Glide.with(applicationContext).load(user.avatar).into(open_chat_userImage)

        listeners()
        listenForMessages()
    }

    fun checkStrings(){
        val result = user.id.compareTo(FirebaseAuth.getInstance().uid.toString())
        if(result > 0)
            message_id = user.id.substring(0,7)+FirebaseAuth.getInstance().uid.toString().substring(0,7)
        else
            message_id = FirebaseAuth.getInstance().uid.toString().substring(0,7)+user.id.substring(0,7)
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
            window.exitTransition
            supportFinishAfterTransition()
            finishAfterTransition()

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
    }

    fun listenForMessages(){
        adapter.clear()
        val db = FirebaseFirestore.getInstance().collection("messages").document(message_id).collection(message_id).orderBy("time",Query.Direction.ASCENDING)
        db.addSnapshotListener{ querySnapshot, firebaseFirestoreException->
            if(firebaseFirestoreException != null){
                return@addSnapshotListener
            }
            for(doc in querySnapshot!!.documentChanges){
                val message = doc.document.toObject(ChatMessage::class.java)
                if(message == null){
                    return@addSnapshotListener
                }
                if(message!!.getSender() == user.id)
                    adapter.add(ChatToItem(message))
                else
                    adapter.add(ChatFromItem(message))
            }
        }
        open_chat_recyclerview.adapter = adapter
    }

    class ChatMessage(val to:String, val from:String, val text: String, val time:Long){
        constructor(): this("","", "", -1)

        fun getSender():String{
            return to
        }
    }

    private fun sendMessage() {
        val message = open_chat_textinput.text.toString()
        val sender = FirebaseAuth.getInstance().uid.toString()
        val receiver = user.id
        val time = System.currentTimeMillis()

        val messageData = ChatMessage(receiver, sender, message, time)
        val db = FirebaseFirestore.getInstance().collection("messages").document(message_id).collection(message_id)
        db.add(messageData).addOnCompleteListener {
            if (it.isSuccessful) {
                val view = this.currentFocus
                if(view != null){
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                open_chat_textinput.setText("")
            } else {
                Snackbar.make(
                    (applicationContext as Activity).window.decorView, "There was an error", Snackbar.LENGTH_SHORT).show()
            }
        }
        listenForMessages()
    }
}