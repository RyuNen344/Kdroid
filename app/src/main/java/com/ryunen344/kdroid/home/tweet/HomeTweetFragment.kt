package com.ryunen344.kdroid.home.tweet

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.R.layout.fragment_home_tweet
import com.ryunen344.kdroid.behavior.EndlessScrollListener
import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.di.provider.UtilProvider
import com.ryunen344.kdroid.home.HomeActivity
import com.ryunen344.kdroid.mediaViewer.MediaViewerActivity
import com.ryunen344.kdroid.profile.ProfileActivity
import com.ryunen344.kdroid.tweetDetail.TweetDetailActivity
import com.ryunen344.kdroid.util.LogUtil
import com.ryunen344.kdroid.util.ensureNotNull
import kotlinx.android.synthetic.main.fragment_home_tweet.*
import kotlinx.android.synthetic.main.fragment_home_tweet.view.*
import org.koin.android.ext.android.inject
import twitter4j.Status
import twitter4j.User

class HomeTweetFragment : Fragment(), HomeTweetContract.View {

    val appProvider : AppProvider by inject()
    val apiProvider : ApiProvider by inject()
    private val utilProvider : UtilProvider by inject()
    lateinit var mPresenter : HomeTweetContract.Presenter
    lateinit var mainListView : LinearLayout
    lateinit var mLayoutManager : LinearLayoutManager
    lateinit var mRecyclerView : RecyclerView
    lateinit var mSwipeRefreshLayout : SwipeRefreshLayout
    var mPagerPosition : Int = 0
    private var mUserId : Long = 0L

    companion object {
        fun newInstance() = HomeTweetFragment()
    }


    private var itemListener : HomeTweetContract.TweetItemListener = object : HomeTweetContract.TweetItemListener {

        override fun onImageClick(mediaUrl : String) {
            LogUtil.d()
            mPresenter.openMedia(mediaUrl)
        }

        override fun onAccountClick(user : User) {
            //fixme
            LogUtil.d()
            mPresenter.openProfile(user)
        }

        override fun onAccountClick(screenName: String) {
            LogUtil.d()
            mPresenter.openProfile(screenName)
        }

        override fun onTweetClick(tweet : Status) {
            //fixme
            LogUtil.d()
            mPresenter.openTweetDetail(tweet)
        }

        override fun onTweetLongClick(position : Int, tweet : Status) {
            LogUtil.d()
            mPresenter.changeFavorite(position, tweet)
        }

        override fun onContextMenuClick(position : Int, tweet : Status) {
            LogUtil.d()
            showContextMenu(position, tweet)
        }

    }

    private val homeTweetAdapter = HomeTweetAdapter(ArrayList(0), itemListener, appProvider, utilProvider)

    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)
        ensureNotNull(activity) {
            mUserId = it.intent.getLongExtra(HomeActivity.INTENT_KEY_USER_ID, 0)
        }
        LogUtil.d("setPresenter")
        HomeTweetPresenter(this, appProvider, apiProvider, mPagerPosition, mUserId)
        homeTweetAdapter.mUserId = mUserId
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
            mSwipeRefreshLayout = swipe_refresh.apply {
                setOnRefreshListener {
                    mPresenter.loadLeastList()
                    isRefreshing = false
                }
            }
            registerForContextMenu(this)
            mainListView = homeTweetLL
        }

        //disable change set animation
        (mRecyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mRecyclerView.addOnScrollListener(object : EndlessScrollListener(mLayoutManager) {
            override fun onLoadMore(currentPage : Int) {
                LogUtil.d("current page is " + currentPage)
                mPresenter.loadMoreList(currentPage)
            }
        })


        //divider set
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        mRecyclerView.addItemDecoration(itemDecoration)

        return root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onViewCreated(view, savedInstanceState)
        home_tweet_list.adapter = homeTweetAdapter
        mPresenter.start()
    }

    override fun onDestroy() {
        LogUtil.d()
        mPresenter.clearDisposable()
        super.onDestroy()
    }

    override fun showTweetList(tweetList : MutableList<Status>) {
        homeTweetAdapter.tweetList = tweetList
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
        LogUtil.d()
        val intent = Intent(context, ProfileActivity::class.java).apply {
            putExtra(ProfileActivity.INTENT_KEY_USER_ID, user.id)
        }
        startActivity(intent)
    }

    override fun showProfile(screenName: String) {
        LogUtil.d()
        val intent = Intent(context, ProfileActivity::class.java).apply {
            putExtra(ProfileActivity.INTENT_KEY_SCREEN_NAME, screenName)
        }
        startActivity(intent)
    }

    override fun showContextMenu(position : Int, tweet : Status) {
        LogUtil.d()
        mRecyclerView.showContextMenu()
    }


    override fun notifyStatusChange(position : Int, tweet : Status) {
        LogUtil.d()
        homeTweetAdapter.notifyStatusChange(position, tweet)
    }

    override fun showTweetDetail(tweet : Status) {
        LogUtil.d()
        val intent = Intent(context, TweetDetailActivity::class.java).apply {
            putExtra(TweetDetailActivity.INTENT_KEY_TWEET_ID, tweet.id)
        }
        startActivity(intent)
    }

    override fun showError(e : Throwable) {
        LogUtil.d()
        LogUtil.e(e)
        Snackbar.make(view!!, e.localizedMessage, Snackbar.LENGTH_LONG).show()
    }

    override fun setPresenter(presenter : HomeTweetContract.Presenter) {
        LogUtil.d()
        ensureNotNull(presenter) { p ->
            mPresenter = p
        }
    }


    override fun onCreateContextMenu(menu : ContextMenu, v : View, menuInfo : ContextMenu.ContextMenuInfo?) {
        LogUtil.d()
        super.onCreateContextMenu(menu, v, menuInfo)
        activity?.menuInflater?.inflate(R.menu.timeline_navigation, menu)
    }

    override fun onContextItemSelected(item : MenuItem) : Boolean {
        val position = homeTweetAdapter.position

        LogUtil.d(" adapter position is " + position)
        if (position < 0) return false
        when (item.itemId) {
            R.id.navigation_home -> {
                LogUtil.d("0 clicked navigation_home")
                mPresenter.changeRetweet(position, homeTweetAdapter.tweetList[position])
            }
            R.id.navigation_mention -> {
                LogUtil.d("0 clicked navigation_mention")
            }
            R.id.navigation_search -> {
                LogUtil.d("0 clicked navigation_search")
            }
        }
        return super.onContextItemSelected(item)
    }


}