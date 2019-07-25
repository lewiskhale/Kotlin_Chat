package com.madebyk.android.mdchatapp.UserLogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.tabs.TabLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.madebyk.android.mdchatapp.R
import com.madebyk.android.mdchatapp.Utils.LoginPageAdapter

class LogInActivity : AppCompatActivity() {

    lateinit var layout: TabLayout
    lateinit var viewpager: ViewPager
    lateinit var pagerAdapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        layout = findViewById(R.id.login_tablayout)
        viewpager = findViewById(R.id.login_viewpager)
        pagerAdapter = LoginPageAdapter(supportFragmentManager, layout.tabCount, applicationContext, FirebaseAuth.getInstance(), mGoogleSignInClient)
        viewpager.adapter = pagerAdapter

        layout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab) {}

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewpager.currentItem = tab.position
            }
        })

        viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(layout))
    }
}