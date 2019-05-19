package com.ryunen344.kdroid.profile.tweet

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ryunen344.kdroid.R.layout.fragment_profile_tweet
import com.ryunen344.kdroid.behavior.EndlessScrollListener
import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.di.provider.UtilProvider
import com.ryunen344.kdroid.mediaViewer.MediaViewerActivity
import com.ryunen344.kdroid.profile.ProfileActivity
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.ensureNotNull
import kotlinx.android.synthetic.main.fragment_profile_tweet.*
import kotlinx.android.synthetic.main.fragment_profile_tweet.view.*
import org.koin.android.ext.android.inject
import twitter4j.Status
import twitter4j.User

class ProfileTweetFragment : Fragment(), ProfileTweetContract.View {

    private val appProvider : AppProvider by inject()
    private val apiProvider : ApiProvider by inject()
    private val utilProvider : UtilProvider by inject()
    private lateinit var mPresenter : ProfileTweetContract.Presenter
    lateinit var profileTweetListView : LinearLayout
    lateinit var mLayoutManager : LinearLayoutManager
    lateinit var mRecyclerView : RecyclerView
    var mPagerPosition : Int = 0
    private var mUserId : Long = 0L

    companion object {
        fun newInstance() = ProfileTweetFragment()
    }

    private var itemListner : ProfileTweetContract.ProfileItemListner = object : ProfileTweetContract.ProfileItemListner {
        override fun onAccountClick(user : User) {
            debugLog("start")
            debugLog("end")
        }

        override fun onImageClick(mediaUrl : String) {
            debugLog("start")
            debugLog("end")
        }

        override fun onTweetClick() {
            debugLog("start")
            debugLog("end")
        }

    }

    private val profileTweetAdapter = ProfileTweetAdapter(ArrayList(0), itemListner, appProvider, utilProvider)

    override fun onCreate(savedInstanceState : Bundle?) {
        debugLog("start")
        super.onCreate(savedInstanceState)
        ensureNotNull(activity) {
            mUserId = it.intent.getLongExtra(ProfileActivity.INTENT_KEY_USER_ID, 0)
        }
        debugLog("setPresenter")
        ProfileTweetPresenter(this, appProvider, apiProvider, mPagerPosition, mUserId)
        debugLog("end")
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var root : View = inflater.inflate(fragment_profile_tweet, container, false)

        with(root) {
            mLayoutManager = LinearLayoutManager(context)
            mRecyclerView = profile_tweet_list.apply {
                this.layoutManager = mLayoutManager
                this.setHasFixedSize(true)
                this.adapter = profileTweetAdapter
            }
            profileTweetListView = profileTweetLL
        }

        mRecyclerView.addOnScrollListener(object : EndlessScrollListener(mLayoutManager) {
            override fun onLoadMore(currentPage : Int) {
                debugLog("current page is " + currentPage)
                mPresenter.loadMoreList(currentPage)
            }
        })

        //divier set
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        mRecyclerView.addItemDecoration(itemDecoration)

        //fixme
        //setHasOptionsMenu(true)

        return root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        debugLog("start")
        super.onViewCreated(view, savedInstanceState)
        profile_tweet_list.adapter = profileTweetAdapter
        mPresenter.start()
        debugLog("end")
    }

    override fun onActivityCreated(savedInstanceState : Bundle?) {
        debugLog("start")
        super.onActivityCreated(savedInstanceState)
        debugLog("end")
    }

    override fun onResume() {
        debugLog("start")
        super.onResume()
        debugLog("end")
    }

    override fun onDestroy() {
        debugLog("start")
        mPresenter.clearDisposable()
        super.onDestroy()
        debugLog("end")
    }

    override fun showTweetList(mainList : List<Status>) {
        profileTweetAdapter.profileTweetList = mainList
        profileTweetAdapter.notifyDataSetChanged()
    }

    override fun showMediaViewer(mediaUrl : String) {
        val intent = Intent(context, MediaViewerActivity::class.java).apply {
            putExtra(MediaViewerActivity.INTENT_KEY_MEDIA_URL, mediaUrl)
        }
        startActivityForResult(intent, MediaViewerActivity.REQUEST_SHOW_MEDIA,
                ActivityOptions.makeCustomAnimation(context, 0, 0).toBundle())
    }

    override fun showTweetDetail() {
        debugLog("start")
        debugLog("end")
    }

    override fun showProfile() {
        debugLog("start")
        debugLog("end")
    }

    override fun setPresenter(presenter : ProfileTweetContract.Presenter) {
        debugLog("start")
        ensureNotNull(presenter) { p ->
            mPresenter = p
        }
        debugLog("end")
    }

    override fun showError(e : Throwable) {
        Snackbar.make(activity?.profileTweetLL!!, e.localizedMessage, Snackbar.LENGTH_LONG).show()
    }

}