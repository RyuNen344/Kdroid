package com.ryunen344.kdroid.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ryunen344.kdroid.profile.tweet.ProfileTweetFragment
import com.ryunen344.kdroid.profile.user.ProfileUserFragment

class ProfileSectionsPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ProfileTweetFragment.newInstance().also {
                it.mPagerPosition = position
            }
            1 -> ProfileTweetFragment.newInstance().also {
                it.mPagerPosition = position
            }
            2 -> ProfileUserFragment.newInstance().also {
                it.mPagerPosition = position
            }
            else -> ProfileUserFragment.newInstance().also {
                it.mPagerPosition = position
            }
        }
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "tweet"
            1 -> "favorite"
            2 -> "follow"
            else -> "follower"
        }
    }

}