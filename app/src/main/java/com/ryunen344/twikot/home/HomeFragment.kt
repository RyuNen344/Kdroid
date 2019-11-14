package com.ryunen344.twikot.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.net.toUri
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.ryunen344.twikot.R
import com.ryunen344.twikot.R.layout.fragment_home
import com.ryunen344.twikot.addTweetReply.AddTweetReplyActivity
import com.ryunen344.twikot.databinding.FragmentHomeBinding
import com.ryunen344.twikot.profile.ProfileActivity
import com.ryunen344.twikot.settings.SettingsActivity
import com.ryunen344.twikot.util.LogUtil
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.nav_home_header.view.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel
import twitter4j.User
import java.io.File


class HomeFragment : Fragment(), HomeContract.View {

    override val presenter : HomeContract.Presenter by currentScope.inject()
    var prevMenuItem : MenuItem? = null
    private lateinit var mSectionsPagerAdapter : HomeSectionsPagerAdapter

    private lateinit var binding : FragmentHomeBinding
    private val homeViewModel : HomeViewModel by viewModel()

    private var itemListener : HomeContract.MainItemListener = object : HomeContract.MainItemListener {
        override fun onImageClick(mediaUrl : String) {
            LogUtil.d()
        }

        override fun onTweetClick() {
            //fixme
            LogUtil.d()
        }

        override fun onAccountClick(user : User) {
            //fixme
            LogUtil.d()
        }
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)
        currentScope.getKoin().setProperty(HomeActivity.INTENT_KEY_USER_ID, arguments!!.getLong(HomeActivity.INTENT_KEY_USER_ID))
        presenter.view = this
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        LogUtil.d()
        var root : View = inflater.inflate(fragment_home, container, false)
        setHasOptionsMenu(true)

        //configure float action button
        root.fab.setOnLongClickListener { view ->
            Snackbar.make(view, "Long tap action", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
            true
        }
        root.fab.setOnClickListener {
            showAddNewTweet()
        }

        var toggle : ActionBarDrawerToggle = ActionBarDrawerToggle(activity, root.drawer_layout, root.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        root.drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        root.home_nav_view.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_profile -> {
                    LogUtil.d()
                    val intent = Intent(context, ProfileActivity::class.java).apply {
                        putExtra(ProfileActivity.INTENT_KEY_USER_ID, arguments!!.getLong(HomeActivity.INTENT_KEY_USER_ID))
                    }
                    startActivity(intent)
                }
                R.id.nav_reload -> {
                    LogUtil.d()
                    mSectionsPagerAdapter.destroyAll(view_pager_container)
                    setPagerAdapter()
                    mSectionsPagerAdapter.notifyDataSetChanged()
                }
                R.id.nav_setting -> {
                    LogUtil.d()
                    val intent = Intent(context, SettingsActivity::class.java)
                    startActivityForResult(intent, SettingsActivity.REQUEST_SETTING)
                }
                R.id.nav_feedback -> {
                    LogUtil.d()
                }
                R.id.nav_help -> {
                    LogUtil.d()
                }
                R.id.nav_about -> {
                    LogUtil.d()
                }
            }
            root.drawer_layout.closeDrawer(GravityCompat.START)
            true
        }

        //configure timeline_navigation bar
        root.navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    root.view_pager_container.currentItem = 0
                }
                R.id.navigation_mention -> {
                    root.view_pager_container.currentItem = 1
                }
                R.id.navigation_search -> {
                    root.view_pager_container.currentItem = 2
                }
            }
            false
        }

        root.view_pager_container.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position : Int, positionOffset : Float, positionOffsetPixels : Int) {

            }

            override fun onPageSelected(position : Int) {

                if (prevMenuItem != null) {
                    prevMenuItem?.setChecked(false)
                } else {
                    root.navigation.menu.getItem(0).isChecked = false
                }

                root.navigation.menu.getItem(position).isChecked = true
                prevMenuItem = navigation.menu.getItem(position)

            }

            override fun onPageScrollStateChanged(state : Int) {
            }

        })

        return root
    }

    override fun onActivityCreated(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onActivityCreated(savedInstanceState)
        setPagerAdapter()
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        LogUtil.d()
        presenter.start()
        presenter.initTwitter(context?.filesDir?.absolutePath)
    }

    override fun onDestroy() {
        LogUtil.d()
        presenter.clearDisposable()
        super.onDestroy()
    }

    override fun showDrawerProfile(userName : String?, screenName : String, profileImage : String?, profileBannerImage : String?) {

        profileBannerImage.let {
            LogUtil.d(it!!)
            if (File(it).exists()) {
                LogUtil.d("file exists")
                home_nav_view.getHeaderView(0).header_profile_banner?.setImageURI(File(it).toUri())
            } else {
                LogUtil.d(File(context?.filesDir, it).absolutePath)
            }
        }

        profileImage.let {
            LogUtil.d(it!!)
            if (File(it).exists()) {
                LogUtil.d("file exists")
                home_nav_view.getHeaderView(0).header_profile_icon?.setImageURI(File(it).toUri())
            } else {
                LogUtil.d(File(context?.filesDir, it).absolutePath)
            }
        }

        userName.let {
            LogUtil.d(it!!)
            home_nav_view.getHeaderView(0).header_user_name?.text = it
        }

        screenName.let {
            LogUtil.d(it)
            home_nav_view.getHeaderView(0).header_screen_name?.text = it
        }

    }


    override fun showAddNewTweet() {
        //fixme
        val intent = Intent(context, AddTweetReplyActivity::class.java)
        startActivityForResult(intent, AddTweetReplyActivity.REQUEST_ADD_TWEET)
    }

    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?) {
        LogUtil.d()
        presenter.result(requestCode, resultCode)
    }

    override fun showSuccessfullyTweet() {
        LogUtil.d()
        Toast.makeText(context, "tweet sent", Toast.LENGTH_SHORT).show()
    }

    override fun showFailTweet() {
        LogUtil.d()
        Toast.makeText(context, "tweet fail", Toast.LENGTH_SHORT).show()
    }

    override fun showSuccessfullyUpdateProfile() {
        LogUtil.d()
        Toast.makeText(context, "success update profile", Toast.LENGTH_SHORT).show()
        presenter.checkImageStatus(context?.filesDir)
    }

    override fun showError(e : Throwable) {
        LogUtil.d()
        Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
    }


    override fun onCreateOptionsMenu(menu : Menu, inflater : MenuInflater) {
        LogUtil.d()
        inflater.inflate(R.menu.timeline_navigation, menu)
    }

    private fun setPagerAdapter() {
        LogUtil.d()
        mSectionsPagerAdapter = HomeSectionsPagerAdapter(fragmentManager!!)
        view_pager_container.adapter = mSectionsPagerAdapter
        view_pager_container.offscreenPageLimit = mSectionsPagerAdapter.count - 1
    }

}