package com.ryunen344.kdroid.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ryunen344.kdroid.di.provider.UtilProvider

class ProfileSectionsPagerAdapter(val profileItemListner: ProfileContract.ProfileItemListner, val utilProvider: UtilProvider, var fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return ProfileFragment.newInstance(position + 1)
    }

    override fun getCount(): Int {
        return 4
    }

}