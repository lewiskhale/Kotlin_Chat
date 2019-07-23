package com.madebyk.android.mdchatapp.Utils

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.madebyk.android.mdchatapp.Home.ChatsFragment
import com.madebyk.android.mdchatapp.Home.ProfileFragment
import com.madebyk.android.mdchatapp.Home.UploadsFragment

class DashboardPageAdapter(fm: FragmentManager, numTabs: Int, context: Context): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private var numOfTabs = 0
    var mContext: Context

    init {
        numOfTabs = numTabs
        mContext = context
    }

    override fun getItem(position: Int): Fragment {

        when(position){
            0-> return ChatsFragment(mContext)
            1-> return UploadsFragment(mContext)
            else->return ProfileFragment(mContext)
        }
    }

    override fun getCount(): Int {
        return numOfTabs
    }

}