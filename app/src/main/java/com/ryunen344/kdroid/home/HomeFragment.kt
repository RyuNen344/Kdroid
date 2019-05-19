package com.ryunen344.kdroid.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.R.layout.fragment_home
import com.ryunen344.kdroid.addTweetReply.AddTweetReplyActivity
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.di.provider.UtilProvider
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.ensureNotNull
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.inject
import twitter4j.User

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
        ensureNotNull(presenter) { p ->
            mPresenter = p
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


        //configure timeline_navigation bar
        activity?.navigation!!.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
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
        })

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
        debugLog("end")
    }

    override fun onDestroy() {
        debugLog("start")
        mPresenter.clearDisposable()
        super.onDestroy()
        debugLog("end")
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