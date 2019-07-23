package com.madebyk.android.mdchatapp.Utils

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.madebyk.android.mdchatapp.UserLogin.SignInFragment
import com.madebyk.android.mdchatapp.UserLogin.SignUpFragment

class LoginPageAdapter(fm: FragmentManager, numTabs: Int, context: Context, auth: FirebaseAuth,val mGoogleSignInClient: GoogleSignInClient): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private var numOfTabs = 0
    var mContext: Context
    val mAuth: FirebaseAuth
    init {
        numOfTabs = numTabs
        mContext = context
        mAuth = auth
    }

    override fun getItem(position: Int): Fragment {

        when(position){
            0-> return SignInFragment(mContext, mAuth, mGoogleSignInClient)
            else-> return SignUpFragment(mContext, mAuth)
        }
    }

    override fun getCount(): Int {
        return numOfTabs
    }

}