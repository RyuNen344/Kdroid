package com.ryunen344.twikot.settings.preferences.license

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.ryunen344.twikot.R.layout.fragment_license
import com.ryunen344.twikot.util.LogUtil
import kotlinx.android.synthetic.main.fragment_license.*
import kotlinx.android.synthetic.main.fragment_license.view.*

class LicenseFragment : Fragment() {

    private lateinit var mSectionsPagerAdapter : LicenseSectionsPagerAdapter

    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        LogUtil.d()
        var root : View = inflater.inflate(fragment_license, container, false)

        root.view_pager_container.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state : Int) {
                LogUtil.d("state is $state")
            }

            override fun onPageScrolled(position : Int, positionOffset : Float, positionOffsetPixels : Int) {
                LogUtil.d("position is $position")
                LogUtil.d("positionOffset is $positionOffset")
                LogUtil.d("positionOffsetPixels is $positionOffsetPixels")
            }

            override fun onPageSelected(position : Int) {
                LogUtil.d("position is $position")
                activity?.title = mSectionsPagerAdapter.getPageTitle(position)
            }

        })
        return root
    }


    override fun onActivityCreated(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onActivityCreated(savedInstanceState)
        setPagerAdapter()
    }

    private fun setPagerAdapter() {
        LogUtil.d()
        mSectionsPagerAdapter = LicenseSectionsPagerAdapter(fragmentManager!!)
        view_pager_container.adapter = mSectionsPagerAdapter
        view_pager_container.offscreenPageLimit = mSectionsPagerAdapter.count - 1
        activity?.title = mSectionsPagerAdapter.getPageTitle(0)
    }
}