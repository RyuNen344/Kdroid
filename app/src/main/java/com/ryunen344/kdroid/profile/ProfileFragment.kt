package com.ryunen344.kdroid.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.di.provider.UtilProvider
import com.ryunen344.kdroid.util.debugLog
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject
import twitter4j.User

class ProfileFragment : Fragment(), ProfileContract.View {

    private val appProvider : AppProvider by inject()
    private val utilProvider: UtilProvider by inject()
    private lateinit var mPresenter: ProfileContract.Presenter
    private lateinit var mSectionsPagerAdapter: ProfileSectionsPagerAdapter

    private val mPicasso = appProvider.providePiccaso()

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private var itemListner: ProfileContract.ProfileItemListener = object : ProfileContract.ProfileItemListener {
        override fun onAccountClick() {
            debugLog("start")
            debugLog("end")
        }

        override fun onImageClick(mediaUrl: String) {
            debugLog("start")
            debugLog("end")
        }

        override fun onTweetClick() {
            debugLog("start")
            debugLog("end")
        }

    }

    override fun setPresenter(presenter: ProfileContract.Presenter) {
        debugLog("start")
        presenter.let {
            mPresenter = it
        }
        debugLog("end")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        debugLog("start")
        debugLog(container.toString())
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        debugLog("end")
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        debugLog("start")
        super.onActivityCreated(savedInstanceState)
        mSectionsPagerAdapter = ProfileSectionsPagerAdapter(fragmentManager!!)
        view_pager_container.adapter = mSectionsPagerAdapter
        view_pager_container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(profileTabs))
        profileTabs.setupWithViewPager(view_pager_container)
        profileTabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view_pager_container))
        debugLog("end")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        debugLog("start")
        mPresenter.start()
        debugLog("end")
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.clearDisposable()
    }

    override fun showUserInfo(user: User) {
        debugLog("start")
        activity?.let {
            it.profile_screen_name.text = user.screenName
            it.profile_description.text = user.description
            it.profile_place.text = user.name

            mPicasso
                    .load(user.profileBanner1500x500URL)
                    .placeholder(R.drawable.ic_loading_image_24dp)
                    .error(R.drawable.ic_loading_image_24dp)
                    .into(it.profile_banner)

            mPicasso
                    .load(user.originalProfileImageURLHttps)
                    .placeholder(R.drawable.ic_loading_image_24dp)
                    .error(R.drawable.ic_loading_image_24dp)
                    .into(it.profile_icon)
        }
        debugLog("start")
    }


    override fun showError(e: Throwable) {
        debugLog("start")
        Snackbar.make(view!!, e.localizedMessage, Snackbar.LENGTH_LONG).show()
        debugLog("end")
    }

}