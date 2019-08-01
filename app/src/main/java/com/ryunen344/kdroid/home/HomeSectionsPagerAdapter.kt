package com.ryunen344.kdroid.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ryunen344.kdroid.home.search.HomeSearchFragment
import com.ryunen344.kdroid.home.tweet.HomeTweetFragment

class HomeSectionsPagerAdapter(fragmentManager : FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position : Int) : Fragment {
        return when (position) {
            0 -> HomeTweetFragment().also {
                it.mPagerPosition = position
            }
            1 -> HomeTweetFragment().also {
                it.mPagerPosition = position
            }
            else -> HomeSearchFragment.newInstance()
        }
    }

    override fun getCount() : Int {
        return 3
    }

    override fun getPageTitle(position : Int) : CharSequence? {
        return when (position) {
            0 -> "timeline"
            1 -> "mention"
            else -> "search"
        }
    }

}