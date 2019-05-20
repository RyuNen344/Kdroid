package com.ryunen344.kdroid.home

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
import com.ryunen344.kdroid.R.layout.fragment_home_tweet
import com.ryunen344.kdroid.behavior.EndlessScrollListener
import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.di.provider.UtilProvider
import com.ryunen344.kdroid.mediaViewer.MediaViewerActivity
import com.ryunen344.kdroid.profile.ProfileActivity
import com.ryunen344.kdroid.profile.tweet.HomeTweetContract
import com.ryunen344.kdroid.profile.tweet.HomeTweetPresenter
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.ensureNotNull
import com.ryunen344.kdroid.util.errorLog
import kotlinx.android.synthetic.main.fragment_home_tweet.*
import kotlinx.android.synthetic.main.fragment_home_tweet.view.*
import org.koin.android.ext.android.inject
import twitter4j.Status
import twitter4j.User

class HomeTweetFragment : Fragment(), HomeTweetContract.View {

    val appProvider : AppProvider by inject()
    val apiProvider : ApiProvider by inject()
    val utilProvider : UtilProvider by inject()
    lateinit var mPresenter : HomeTweetContract.Presenter
    lateinit var mainListView : LinearLayout
    lateinit var mLayoutManager : LinearLayoutManager
    lateinit var mRecyclerView : RecyclerView
    var mPagerPosition : Int = 0
    private var mUserId : Long = 0L

    companion object {
        fun newInstance() = HomeTweetFragment()
    }


    private var itemListener : HomeTweetContract.TweetItemListner = object : HomeTweetContract.TweetItemListner {
        override fun onImageClick(mediaUrl : String) {
            debugLog("start")
            mPresenter.openMedia(mediaUrl)
            debugLog("end")
        }

        override fun onTweetClick() {
            //fixme
            debugLog("start")
            mPresenter.openTweetDetail()
            debugLog("end")
        }

        override fun onAccountClick(user : User) {
            //fixme
            debugLog("start")
            mPresenter.openProfile(user)
            debugLog("end")
        }
    }

    private val homeTweetAdapter = HomeTweetAdapter(ArrayList(0), itemListener, appProvider, utilProvider)

    override fun onCreate(savedInstanceState : Bundle?) {
        debugLog("start")
        super.onCreate(savedInstanceState)
        ensureNotNull(activity) {
            mUserId = it.intent.getLongExtra(HomeActivity.INTENT_KEY_USER_ID, 0)
        }
        debugLog("setPresenter")
        HomeTweetPresenter(this, appProvider, apiProvider, mPagerPosition, mUserId)
        homeTweetAdapter.mUserId = mUserId
        debugLog("end")
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var root : View = inflater.inflate(fragment_home_tweet, container, false)

        with(root) {
            mLayoutManager = LinearLayoutManager(context)
            mRecyclerView = home_tweet_list.apply {
                this.layoutManager = mLayoutManager
                this.setHasFixedSize(true)
                this.adapter = homeTweetAdapter
            }
            mainListView = homeTweetLL
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

        return root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        debugLog("start")
        super.onViewCreated(view, savedInstanceState)
        home_tweet_list.adapter = homeTweetAdapter
        mPresenter.start()
        debugLog("end")
    }

    override fun onDestroy() {
        debugLog("start")
        mPresenter.clearDisposable()
        super.onDestroy()
        debugLog("end")
    }

    override fun showTweetList(mainList : List<Status>) {
        homeTweetAdapter.tweetList = mainList
        homeTweetAdapter.notifyDataSetChanged()
    }

    override fun showMediaViewer(mediaUrl : String) {
        val intent = Intent(context, MediaViewerActivity::class.java).apply {
            putExtra(MediaViewerActivity.INTENT_KEY_MEDIA_URL, mediaUrl)
        }
        startActivityForResult(intent, MediaViewerActivity.REQUEST_SHOW_MEDIA,
                ActivityOptions.makeCustomAnimation(context, 0, 0).toBundle())
    }

    override fun showProfile(user : User) {
        debugLog("start")
        val intent = Intent(context, ProfileActivity::class.java).apply {
            putExtra(ProfileActivity.INTENT_KEY_USER_ID, user.id)
        }
        startActivity(intent)
        debugLog("end")
    }

    override fun showTweetDetail() {
        debugLog("start")
        debugLog("end")
    }

    override fun showError(e : Throwable) {
        debugLog("start")
        errorLog(e.localizedMessage)
        Snackbar.make(view!!, e.localizedMessage, Snackbar.LENGTH_LONG).show()
        debugLog("end")
    }

    override fun setPresenter(presenter : HomeTweetContract.Presenter) {
        debugLog("start")
        ensureNotNull(presenter) { p ->
            mPresenter = p
        }
        debugLog("end")
    }


}