package com.ryunen344.kdroid.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.net.toUri
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.R.layout.fragment_home
import com.ryunen344.kdroid.addTweetReply.AddTweetReplyActivity
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.di.provider.UtilProvider
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.splitLastThreeWord
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_home_header.view.*
import org.koin.android.ext.android.inject
import twitter4j.User
import java.io.File


class HomeFragment : Fragment(), HomeContract.View {

    val appProvider : AppProvider by inject()
    val utilProvider : UtilProvider by inject()
    lateinit var mPresenter : HomeContract.Presenter
    private lateinit var mSectionsPagerAdapter : HomeSectionsPagerAdapter

    companion object {
        fun newInstance() = HomeFragment()
    }


    private var itemListener : HomeContract.MainItemListner = object : HomeContract.MainItemListner {
        override fun onImageClick(mediaUrl : String) {
            debugLog("start")
            debugLog("end")
        }

        override fun onTweetClick() {
            //fixme
            debugLog("start")
            debugLog("end")
        }

        override fun onAccountClick(user : User) {
            //fixme
            debugLog("start")
            debugLog("end")
        }
    }

    override fun setPresenter(presenter : HomeContract.Presenter) {
        debugLog("start")
        presenter.let {
            mPresenter = it
        }
        debugLog("end")
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        debugLog("start")
        var root : View = inflater.inflate(fragment_home, container, false)
        setHasOptionsMenu(true)

        //configure float action button
        activity?.fab?.setOnLongClickListener { view ->
            Snackbar.make(view, "Long tap action", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
            true
        }
        activity?.fab?.setOnClickListener {
            showAddNewTweet()
        }

        var toggle : ActionBarDrawerToggle = ActionBarDrawerToggle(activity, activity?.drawer_layout, activity?.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        activity?.drawer_layout?.addDrawerListener(toggle)
        toggle.syncState()

        activity?.home_nav_view?.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_profile -> {
                    debugLog("start")
                    debugLog("end")
                }
                R.id.nav_reload -> {
                    debugLog("start")
                    debugLog("end")
                }
                R.id.nav_setting -> {
                    debugLog("start")
                    debugLog("end")
                }
                R.id.nav_feedback -> {
                    debugLog("start")
                    debugLog("end")
                }
                R.id.nav_help -> {
                    debugLog("start")
                    debugLog("end")
                }
                R.id.nav_about -> {
                    debugLog("start")
                    debugLog("end")
                }
            }
            activity?.drawer_layout?.closeDrawer(GravityCompat.START)
            true
        }

        //configure timeline_navigation bar
        activity?.navigation!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    view_pager_container.currentItem = 0
                }
                R.id.navigation_dashboard -> {
                    view_pager_container.currentItem = 1
                }
                R.id.navigation_notifications -> {
                    view_pager_container.currentItem = 2
                }
            }
            false
        }

        debugLog("end")
        return root
    }

    override fun onActivityCreated(savedInstanceState : Bundle?) {
        debugLog("start")
        super.onActivityCreated(savedInstanceState)
        mSectionsPagerAdapter = HomeSectionsPagerAdapter(fragmentManager!!)
        view_pager_container.adapter = mSectionsPagerAdapter
        view_pager_container.offscreenPageLimit = mSectionsPagerAdapter.count - 1
        debugLog("end")
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        debugLog("start")
        mPresenter.start()
        //mPresenter.initProfile()
        mPresenter.checkImageStatus(context?.filesDir)
        debugLog("end")
    }

    override fun onDestroy() {
        debugLog("start")
        mPresenter.clearDisposable()
        super.onDestroy()
        debugLog("end")
    }

    override fun showDrawerProfile(userName : String?, screenName : String, profileImage : String?, profileBannerImage : String?) {

        activity?.home_nav_view?.header_screen_name?.text = ""
        activity?.home_nav_view?.header_user_name?.text = ""
        activity?.home_nav_view?.getHeaderView(0)?.header_screen_name?.text = "HOGEHOGE"

        activity?.let { activity ->
            profileBannerImage.let {
                debugLog(it!!)
                debugLog(splitLastThreeWord(it))
                if (File(context?.filesDir, splitLastThreeWord(it)).exists()) {
                    debugLog("file exists")
                    activity.home_nav_view?.getHeaderView(0)?.header_profile_banner?.setImageURI(File(context?.filesDir, it).toUri())
                }
            }

            profileImage.let {
                debugLog(it!!)
                debugLog(splitLastThreeWord(it))
                if (File(context?.filesDir, splitLastThreeWord(it)).exists()) {
                    debugLog("file exists")
                    activity.home_nav_view?.getHeaderView(0)?.header_profile_icon?.setImageURI(File(context?.filesDir, it).toUri())
                }
            }

            userName.let {
                debugLog(it!!)
                activity.home_nav_view?.getHeaderView(0)?.header_user_name?.text = it
            }

            screenName.let {
                debugLog(it)
                activity.home_nav_view?.getHeaderView(0)?.header_screen_name?.text = it
            }
        }


    }


    override fun showAddNewTweet() {
        //fixme
        val intent = Intent(context, AddTweetReplyActivity::class.java)
        startActivityForResult(intent, AddTweetReplyActivity.REQUEST_ADD_TWEET)
    }

    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?) {
        debugLog("start")
        mPresenter.result(requestCode, resultCode)
        debugLog("end")
    }

    override fun showSuccessfullyTweet() {
        debugLog("start")
        Snackbar.make(activity?.nestedScrollView!!, "tweet sent", Snackbar.LENGTH_LONG).show()
        debugLog("end")

    }

    override fun showFailTweet() {
        debugLog("start")
        Snackbar.make(activity?.nestedScrollView!!, "tweet fail", Snackbar.LENGTH_LONG).show()
        debugLog("end")
    }

    override fun showSuccessfullyUpdateProfile() {
        debugLog("start")
        Snackbar.make(activity?.nestedScrollView!!, "success update profile", Snackbar.LENGTH_LONG).show()
        debugLog("end")
    }

    override fun showError(e : Throwable) {
        debugLog("start")
        Snackbar.make(activity?.nestedScrollView!!, e.localizedMessage, Snackbar.LENGTH_LONG).show()
        debugLog("end")
    }


    override fun onCreateOptionsMenu(menu : Menu, inflater : MenuInflater) {
        debugLog("start")
        inflater.inflate(R.menu.timeline_navigation, menu)
        debugLog("end")
    }


}