package com.ryunen344.twikot.profile.tweet

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ryunen344.twikot.R
import com.ryunen344.twikot.R.layout.fragment_profile_tweet
import com.ryunen344.twikot.behavior.EndlessScrollListener
import com.ryunen344.twikot.mediaViewer.MediaViewerActivity
import com.ryunen344.twikot.profile.ProfileActivity
import com.ryunen344.twikot.util.LogUtil
import com.ryunen344.twikot.util.ensureNotNull
import kotlinx.android.synthetic.main.fragment_profile_tweet.*
import kotlinx.android.synthetic.main.fragment_profile_tweet.view.*
import org.koin.android.scope.currentScope
import twitter4j.Status
import twitter4j.User

class ProfileTweetFragment : Fragment(), ProfileTweetContract.View {

    override val presenter : ProfileTweetContract.Presenter by currentScope.inject()

    lateinit var profileTweetListView : LinearLayout
    lateinit var mLayoutManager : LinearLayoutManager
    lateinit var mRecyclerView : RecyclerView
    var mPagerPosition : Int = 0
    private var mUserId : Long = 0L
    private var mScreenName: String = ""

    companion object {
        const val INTENT_KEY_PAGER_POSITION : String = "key_pager_position"
    }

    private var itemListener: ProfileTweetContract.ProfileItemListener = object : ProfileTweetContract.ProfileItemListener {
        override fun onImageClick(mediaUrl: String) {
            LogUtil.d()
            presenter.openMedia(mediaUrl)
        }

        override fun onAccountClick(user : User) {
            //fixme
            LogUtil.d()
            presenter.openProfile(user)
        }

        override fun onAccountClick(screenName: String) {
            LogUtil.d()
            presenter.openProfile(screenName)
        }

        override fun onTweetClick() {
            //fixme
            LogUtil.d()
            presenter.openTweetDetail()
        }

        override fun onTweetLongClick(position: Int, tweet: Status) {
            LogUtil.d()
            presenter.changeFavorite(position, tweet)
        }

        override fun onContextMenuClick(position: Int, tweet: Status) {
            LogUtil.d()
            showContextMenu(position, tweet)
        }

    }

    private val profileTweetAdapter = ProfileTweetAdapter(ArrayList(0), itemListener)

    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)
        ensureNotNull(activity) {
            mUserId = it.intent.getLongExtra(ProfileActivity.INTENT_KEY_USER_ID, 0)
            mScreenName = it.intent.getStringExtra(ProfileActivity.INTENT_KEY_SCREEN_NAME) ?: ""
        }

        currentScope.getKoin().setProperty(ProfileActivity.INTENT_KEY_USER_ID, mUserId)
        currentScope.getKoin().setProperty(ProfileActivity.INTENT_KEY_SCREEN_NAME, mScreenName)
        currentScope.getKoin().setProperty(INTENT_KEY_PAGER_POSITION, mPagerPosition)
        profileTweetAdapter.mUserId = mUserId
        presenter.view = this
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
                LogUtil.d("current page is " + currentPage)
                presenter.loadMoreList(currentPage)
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
        LogUtil.d()
        super.onViewCreated(view, savedInstanceState)
        profile_tweet_list.adapter = profileTweetAdapter
        presenter.start()
    }

    override fun onActivityCreated(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        LogUtil.d()
        super.onResume()
    }

    override fun onDestroy() {
        LogUtil.d()
        presenter.clearDisposable()
        super.onDestroy()
    }

    override fun showTweetList(tweetList: MutableList<Status>) {
        profileTweetAdapter.profileTweetList = tweetList
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
        LogUtil.d()
    }

    override fun showProfile(user: User) {
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

    override fun showContextMenu(position: Int, tweet: Status) {
        LogUtil.d()
        mRecyclerView.showContextMenu()
    }

    override fun notifyStatusChange(position: Int, tweet: Status) {
        LogUtil.d()
        profileTweetAdapter.notifyStatusChange(position, tweet)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        LogUtil.d()
        super.onCreateContextMenu(menu, v, menuInfo)
        activity?.menuInflater?.inflate(R.menu.timeline_navigation, menu)
    }

    override fun showError(e : Throwable) {
        Snackbar.make(activity?.profileTweetLL!!, e.localizedMessage, Snackbar.LENGTH_LONG).show()
    }

}