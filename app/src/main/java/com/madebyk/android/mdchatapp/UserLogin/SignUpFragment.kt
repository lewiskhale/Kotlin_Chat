package com.madebyk.android.mdchatapp.UserLogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.madebyk.android.mdchatapp.DataModel.UserInfo
import com.madebyk.android.mdchatapp.Home.Dashboard

import com.madebyk.android.mdchatapp.R
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment(context: Context, mAuth: FirebaseAuth) : Fragment() {

    var mContext: Context
    val auth: FirebaseAuth
    var currentUser: FirebaseUser?
    lateinit var userInfo: UserInfo

    init {
        mContext = context
        auth = mAuth
        currentUser = auth.currentUser
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onResume() {
        val email = signup_email.getText().toString()
        val password = signup_password.getText().toString()

        super.onResume()
        buttonListener()
        //isVerified(currentUser)
        if(email != "" && password != "")
            LogIn(email, password)
    }

    fun buttonListener(){
        signup_button.setOnClickListener {
            val username = signup_username.getText().toString()
            val email = signup_email.getText().toString()
            val password = signup_password.getText().toString()

            if(isVerified(currentUser)){
                LogInVerified()
            }
            else if(password.length < 6)
                Toast.makeText(mContext, resources.getString(R.string.password_not_long_enough), Toast.LENGTH_SHORT).show()
            else if(!username.isEmpty() || !email.isEmpty() || !password.isEmpty()){
                newUser(email,password, username)
            }
            else{
                Toast.makeText(mContext, resources.getString(R.string.no_entered_email_password), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun LogIn(email: String, password: String){
        if(currentUser != null) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            if(isVerified(currentUser)){
                                LogInVerified()
                            }
                            else{
                                Toast.makeText(
                                    mContext,
                                    resources.getString(R.string.email_not_verified),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        else {
                            Toast.makeText(mContext, resources.getString(R.string.Login_failed_ep), Toast.LENGTH_SHORT)
                                .show()
//                            updateUI(null)
                        }
                    }
        }
    }

    fun updateUI(user: FirebaseUser?){
        if(user != null){
            currentUser = user
        }
    }

    fun newUser(email: String, password: String, username: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    updateUI(auth.currentUser)
                    if(currentUser != null){
                        currentUser?.sendEmailVerification()
                        Toast.makeText(mContext, resources.getString(R.string.email_verified_sent), Toast.LENGTH_SHORT).show()
                        userInfo = UserInfo(username, currentUser?.uid, email, System.currentTimeMillis())
                        LogIn(email, password)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(mContext, resources.getString(R.string.failed_authentication),
                        Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                }
            }
    }

    fun saveUsertoDB(): Boolean{
        var success: Boolean = false
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(currentUser!!.uid)
            .set(userInfo)
            .addOnCompleteListener {
                if(it.isSuccessful) {
//                    Toast.makeText(mContext, resources.getString(R.string.registered_to_DB), Toast.LENGTH_SHORT).show()
                    success = true
                }
            }
        if(success == true)
            return true
        return false
    }

    fun isVerified(user: FirebaseUser?): Boolean{
        if(user != null){
            if(user.isEmailVerified){
                return true
            }
        }
        return false
    }

    fun LogInVerified() {
        saveUsertoDB()
        Toast.makeText(
                mContext,
                resources.getString(R.string.Login_successful_ep),
                Toast.LENGTH_SHORT).show()

        val intent = Intent(mContext, Dashboard::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        }
}
