package com.ryunen344.kdroid.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ryunen344.kdroid.profile.tweet.ProfileTweetFragment
import com.ryunen344.kdroid.profile.user.ProfileUserFragment

class ProfileSectionsPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0, 1 -> ProfileTweetFragment.newInstance()
            else -> ProfileUserFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 4
    }

}