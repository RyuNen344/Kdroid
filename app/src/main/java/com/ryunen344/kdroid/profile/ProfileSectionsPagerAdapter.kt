package com.ryunen344.kdroid.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ProfileSectionsPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0, 1 -> return ProfileTweetFragment.newInstance()
            2, 3 -> return ProfileUserFragment.newInstance()
            else -> return ProfileFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 4
    }

}