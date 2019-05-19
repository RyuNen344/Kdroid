package com.ryunen344.kdroid.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ryunen344.kdroid.home.search.HomeSearchFragment

class HomeSectionsPagerAdapter(fragmentManager : FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position : Int) : Fragment {
        return when (position) {
            0 -> HomeTweetFragment.newInstance().also {
                it.mPagerPosition = position
            }
            1 -> HomeTweetFragment.newInstance().also {
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