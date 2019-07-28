package com.ryunen344.kdroid.tweetDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.di.provider.UtilProvider
import com.ryunen344.kdroid.tweetDetail.TweetDetailActivity.Companion.INTENT_KEY_TWEET_ID
import com.ryunen344.kdroid.util.LogUtil
import kotlinx.android.synthetic.main.fragment_tweet_detail.view.*
import org.koin.android.ext.android.inject
import org.koin.android.scope.currentScope
import twitter4j.Status
import twitter4j.User


class TweetDetailFragment : Fragment(), TweetDetailContract.View {

    private val appProvider : AppProvider by inject()
    private val utilProvider : UtilProvider by inject()
    override val presenter : TweetDetailContract.Presenter by currentScope.inject()

    lateinit var mainListView : LinearLayout
    lateinit var mLayoutManager : LinearLayoutManager
    lateinit var mRecyclerView : RecyclerView
    lateinit var mSwipeRefreshLayout : SwipeRefreshLayout

    companion object {
        fun newInstance() = TweetDetailFragment()
    }

    private var itemListener : TweetDetailContract.TweetItemListener = object : TweetDetailContract.TweetItemListener {

        override fun onImageClick(mediaUrl : String) {
            LogUtil.d()
            //mPresenter.openMedia(mediaUrl)
        }

        override fun onAccountClick(user : User) {
            //fixme
            LogUtil.d()
            //mPresenter.openProfile(user)
        }

        override fun onAccountClick(screenName : String) {
            LogUtil.d()
            //mPresenter.openProfile(screenName)
        }

        override fun onTweetClick(tweet : Status) {
            //fixme
            LogUtil.d()
            //mPresenter.openTweetDetail(tweet)
        }

        override fun onTweetLongClick(position : Int, tweet : Status) {
            LogUtil.d()
            //mPresenter.changeFavorite(position, tweet)
        }

        override fun onContextMenuClick(position : Int, tweet : Status) {
            LogUtil.d()
            //showContextMenu(position, tweet)
        }

    }

    private val tweetDetailAdapter = TweetDetailAdapter(ArrayList(0), itemListener, appProvider, utilProvider)

    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)

        currentScope.getKoin().setProperty(INTENT_KEY_TWEET_ID, arguments!!.getLong(INTENT_KEY_TWEET_ID))
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        LogUtil.d()
        super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(R.layout.fragment_tweet_detail, container, false)

        with(root) {
            mLayoutManager = LinearLayoutManager(context)
            mRecyclerView = tweet_detail_list.apply {
                this.layoutManager = mLayoutManager
                this.setHasFixedSize(true)
                this.adapter = tweetDetailAdapter
            }
//            mSwipeRefreshLayout = swipe_refresh.apply {
//                setOnRefreshListener {
//                    mPresenter.loadLeastList()
//                    isRefreshing = false
//                }
//            }
            registerForContextMenu(this)
            mainListView = tweetDetailLL
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        LogUtil.d()
        presenter.view = this
        presenter.start()
    }

    override fun showTweetDetail(status : Status) {
        LogUtil.d()
        tweetDetailAdapter.tweetList = listOf<Status>(status) as MutableList<Status>
        tweetDetailAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.clearDisposable()
    }

    override fun showError(e : Throwable) {
        LogUtil.w(e)
        Snackbar.make(view!!, e.localizedMessage, Snackbar.LENGTH_LONG).show()
    }

}