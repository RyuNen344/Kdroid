package com.ryunen344.twikot.settings.preferences.license

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ryunen344.twikot.util.LogUtil

class LicenseSectionsPagerAdapter(fragmentManager : FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position : Int) : Fragment {
        return when (position) {
            0 -> LicenseTextFragment()
            1 -> LicenseWebFragment().also {
                it.mPagerPosition = position
            }
            else -> LicenseWebFragment().also {
                it.mPagerPosition = position
            }

        }
    }

    override fun getCount() : Int {
        return 3
    }

    override fun getPageTitle(position : Int) : CharSequence? {
        return when (position) {
            0 -> "app license"
            1 -> "twitter privacy"
            else -> "twitter tos"
        }
    }

    fun destroyAll(container : ViewGroup) {
        for (position in 0 .. count) {
            LogUtil.d()
            destroyItem(container, position, getItem(position))
        }
    }

}