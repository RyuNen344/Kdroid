package com.ryunen344.twikot.home

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ryunen344.twikot.home.search.HomeSearchFragment
import com.ryunen344.twikot.home.tweet.HomeTweetFragment
import com.ryunen344.twikot.util.LogUtil

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

    fun destroyAll(container : ViewGroup) {
        for (position in 0 .. count) {
            LogUtil.d()
            destroyItem(container, position, getItem(position))
        }
    }

}