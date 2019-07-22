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
import com.ryunen344.kdroid.util.debugLog
import kotlinx.android.synthetic.main.fragment_tweet_detail.view.*
import org.koin.android.ext.android.inject
import twitter4j.Status
import twitter4j.User


class TweetDetailFragment : Fragment(), TweetDetailContract.View {


    private val appProvider : AppProvider by inject()
    private val utilProvider : UtilProvider by inject()
    private lateinit var mPresenter : TweetDetailContract.Presenter
    lateinit var mainListView : LinearLayout
    lateinit var mLayoutManager : LinearLayoutManager
    lateinit var mRecyclerView : RecyclerView
    lateinit var mSwipeRefreshLayout : SwipeRefreshLayout

    companion object {
        fun newInstance() = TweetDetailFragment()
    }

    private var itemListener : TweetDetailContract.TweetItemListener = object : TweetDetailContract.TweetItemListener {

        override fun onImageClick(mediaUrl : String) {
            debugLog("start")
            //mPresenter.openMedia(mediaUrl)
            debugLog("end")
        }

        override fun onAccountClick(user : User) {
            //fixme
            debugLog("start")
            //mPresenter.openProfile(user)
            debugLog("end")
        }

        override fun onAccountClick(screenName : String) {
            debugLog("start")
            //mPresenter.openProfile(screenName)
            debugLog("end")
        }

        override fun onTweetClick(tweet : Status) {
            //fixme
            debugLog("start")
            //mPresenter.openTweetDetail(tweet)
            debugLog("end")
        }

        override fun onTweetLongClick(position : Int, tweet : Status) {
            debugLog("start")
            //mPresenter.changeFavorite(position, tweet)
            debugLog("end")
        }

        override fun onContextMenuClick(position : Int, tweet : Status) {
            debugLog("start")
            //showContextMenu(position, tweet)
            debugLog("end")
        }

    }

    private val tweetDetailAdapter = TweetDetailAdapter(ArrayList(0), itemListener, appProvider, utilProvider)

    override fun setPresenter(presenter : TweetDetailContract.Presenter) {
        debugLog("start")
        presenter.let {
            mPresenter = it
        }
        debugLog("end")
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        debugLog("start")
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
        debugLog("end")
        return root
    }

    override fun onActivityCreated(savedInstanceState : Bundle?) {
        debugLog("start")
        super.onActivityCreated(savedInstanceState)
        debugLog("end")
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        debugLog("start")
        mPresenter.start()
        debugLog("end")
    }

    override fun showTweetDetail(status : Status) {
        debugLog("start")
        tweetDetailAdapter.tweetList = listOf<Status>(status) as MutableList<Status>
        tweetDetailAdapter.notifyDataSetChanged()
        debugLog("end")
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.clearDisposable()
    }

    override fun showError(e : Throwable) {
        debugLog("start")
        Snackbar.make(view!!, e.localizedMessage, Snackbar.LENGTH_LONG).show()
        debugLog("end")
    }

}