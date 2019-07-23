package com.madebyk.android.mdchatapp.UserLogin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.madebyk.android.mdchatapp.Home.Dashboard

import com.madebyk.android.mdchatapp.R
import kotlinx.android.synthetic.main.fragment_sign_in.*
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task




class SignInFragment(context: Context, mAuth: FirebaseAuth, val mGoogleSignInClient: GoogleSignInClient) : Fragment() {

    var mContext: Context
    var auth: FirebaseAuth
    var currentUser: FirebaseUser?
    val RC_SIGN_IN = 2

    init {
        mContext = context
        auth = mAuth
        currentUser = auth.currentUser
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onStart() {
        super.onStart()
        signin_google_button.setColorScheme(R.color.colorPrimary)
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(mContext)
        buttonListener()
        isVerified(currentUser)
    }


    fun buttonListener(){
        signin_button.setOnClickListener {
            val email = signin_email.getText().toString()
            val password = signin_password.getText().toString()

            if(!(email.isEmpty() || password.isEmpty())){
                LogIn(email,password)
            }
            else{
                Toast.makeText(mContext, resources.getString(R.string.no_entered_email_password), Toast.LENGTH_SHORT).show()
            }
        }
        
        signin_forgot_password.setOnClickListener {
            val intent = Intent(mContext, ResetPasswordActivity:: class.java)
            startActivity(intent)
        }

        signin_google_button.setOnClickListener {
            //signIn()
        }

    }

    fun LogIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    val currentUser = auth.currentUser
                    updateUI(currentUser)
                    isVerified(currentUser)
                }
                else{
                    Toast.makeText(mContext, resources.getString(R.string.Login_failed_ep), Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    fun updateUI(user: FirebaseUser?){
        if(user != null){
            currentUser = user
        }
    }

    fun isVerified(user: FirebaseUser?){
        if(user != null){
            if(user.isEmailVerified){
                val intent = Intent(mContext, Dashboard::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                Toast.makeText(mContext, resources.getString(R.string.Login_successful_ep), Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
            else{
                Toast.makeText(mContext, resources.getString(R.string.email_not_verified), Toast.LENGTH_SHORT).show()
            }
        }
    }

    //____________________Google Sign In________________________________
/*
    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            //handleSignInResult(task)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                //firebaseAuthWithGoogle(account!!)
                updateUI(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Snackbar.make((mContext as Activity).window.decorView, R.string.google_signin_failed, Snackbar.LENGTH_SHORT).show()
                // Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }
    }
*/

}
