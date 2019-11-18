package com.ryunen344.twikot.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.net.toUri
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.ryunen344.twikot.LoggingLifecycleObserver
import com.ryunen344.twikot.R
import com.ryunen344.twikot.addTweetReply.AddTweetReplyActivity
import com.ryunen344.twikot.databinding.FragmentHomeBinding
import com.ryunen344.twikot.profile.ProfileActivity
import com.ryunen344.twikot.settings.SettingsActivity
import com.ryunen344.twikot.util.LogUtil
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_home_header.view.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel
import twitter4j.User
import java.io.File

class HomeFragment : Fragment() {

    var prevMenuItem : MenuItem? = null
    private lateinit var mSectionsPagerAdapter : HomeSectionsPagerAdapter

    private lateinit var binding : FragmentHomeBinding
    private val homeViewModel : HomeViewModel by viewModel()
    private val disposable = CompositeDisposable()
    private val fragmentLifecycleObserver = LoggingLifecycleObserver()

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
        this@HomeFragment.lifecycle.addObserver(fragmentLifecycleObserver)
        currentScope.getKoin().setProperty(HomeActivity.INTENT_KEY_USER_ID, arguments!!.getLong(HomeActivity.INTENT_KEY_USER_ID))
    }

    override fun onCreateView(
            inflater : LayoutInflater,
            container : ViewGroup?,
            savedInstanceState : Bundle?
    ) : View? {
        LogUtil.d()
        binding = DataBindingUtil.inflate<FragmentHomeBinding>(
                inflater,
                R.layout.fragment_home,
                container,
                false
        ).also {
            it.lifecycleOwner = this@HomeFragment.viewLifecycleOwner
            it.viewModel = homeViewModel
        }

        initFab(binding)
        initDrawerLayout(binding)
        initPagerContainer(binding)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onActivityCreated(savedInstanceState)
        setPagerAdapter()
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
//        presenter.initTwitter(context?.filesDir?.absolutePath)
    }

    override fun onDestroy() {
        super.onDestroy()
        this@HomeFragment.lifecycle.removeObserver(fragmentLifecycleObserver)
    }

    private fun initFab(binding : FragmentHomeBinding) {

        //configure float action button
        binding.fab.setOnLongClickListener { view ->
            Snackbar.make(view, "Long tap action", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
            true
        }
        binding.fab.setOnClickListener {
            showPostTweet()
        }
    }

    private fun initDrawerLayout(binding : FragmentHomeBinding) {
        setHasOptionsMenu(true)

        val toggle = ActionBarDrawerToggle(activity, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.homeNavView.setNavigationItemSelectedListener { item ->
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
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun initPagerContainer(binding : FragmentHomeBinding) {
        //configure timeline_navigation bar
        binding.navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    binding.viewPagerContainer.currentItem = 0
                }
                R.id.navigation_mention -> {
                    binding.viewPagerContainer.currentItem = 1
                }
                R.id.navigation_search -> {
                    binding.viewPagerContainer.currentItem = 2
                }
            }
            false
        }

        binding.viewPagerContainer.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position : Int, positionOffset : Float, positionOffsetPixels : Int) {}

            override fun onPageSelected(position : Int) {

                if (prevMenuItem != null) {
                    prevMenuItem?.isChecked = false
                } else {
                    binding.navigation.menu.getItem(0).isChecked = false
                }

                binding.navigation.menu.getItem(position).isChecked = true
                prevMenuItem = navigation.menu.getItem(position)

            }

            override fun onPageScrollStateChanged(state : Int) {}
        })
    }

    private fun showDrawerProfile(userName : String?, screenName : String, profileImage : String?, profileBannerImage : String?) {

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


    private fun showPostTweet() {
        val intent = Intent(context, AddTweetReplyActivity::class.java)
        startActivityForResult(intent, AddTweetReplyActivity.REQUEST_ADD_TWEET)
    }

    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?) {
        LogUtil.d()
//        presenter.result(requestCode, resultCode)
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