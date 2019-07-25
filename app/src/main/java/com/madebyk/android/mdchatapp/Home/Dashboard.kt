package com.madebyk.android.mdchatapp.Home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.madebyk.android.mdchatapp.ChatActivities.NewChat
import com.madebyk.android.mdchatapp.R
import com.madebyk.android.mdchatapp.UserLogin.LogInActivity
import com.madebyk.android.mdchatapp.Utils.DashboardPageAdapter
import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard: AppCompatActivity(){

    lateinit var currentUser: FirebaseUser
    lateinit var layout: TabLayout
    lateinit var viewPager: ViewPager
    lateinit var pagerAdapter: PagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val user = FirebaseAuth.getInstance().currentUser
        val uid = FirebaseAuth.getInstance().uid
        verifyUserLoggedIn(user, uid)

        dashboard_newChat.setIconResource(R.drawable.ic_chat)
        dashboard_newChat.setIconTintResource(R.color.White)
        setSupportActionBar(dashboard_toolbar)
        layout = findViewById(R.id.dashboard_tablayout)
        viewPager = findViewById(R.id.dashboard_viewpager)
        pagerAdapter = DashboardPageAdapter(supportFragmentManager, layout.tabCount, applicationContext)
        viewPager.adapter = pagerAdapter

        layout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab) {}

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabSelected(tab: TabLayout.Tab) {
                if(tab.position == 0)
                    dashboard_newChat.show()
                else
                    dashboard_newChat.hide()
                viewPager.currentItem = tab.position

                //Change the color of toolbar and tablayout
            }
        })
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(layout))
        newChats()
    }

    fun verifyUserLoggedIn(user: FirebaseUser?, uid: String?){
        if(user == null || uid == null || !user.isEmailVerified){
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
            finish()
            //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or (Intent.FLAG_ACTIVITY_NEW_TASK) or (Intent.FLAG_ACTIVITY_CLEAR_TOP) or (Intent.FLAG_ACTIVITY_SINGLE_TOP)
            //intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        }
        else{
            currentUser = user
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.signout->sign_out()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.dashboard_menu_items, menu)
        return true
    }

    fun newChats(){
        dashboard_newChat.setOnClickListener {
            val intent = Intent(this, NewChat::class.java)
            startActivity(intent)
        }
    }

    fun sign_out(){
        FirebaseAuth.getInstance().signOut()
        verifyUserLoggedIn(null, null)
    }
}