package com.ryunen344.kdroid.profile.tweet

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
import com.ryunen344.kdroid.di.provider.UtilProvider
import com.ryunen344.kdroid.main.EndlessScrollListener
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.ensureNotNull
import kotlinx.android.synthetic.main.fragment_profile_tweet.*
import kotlinx.android.synthetic.main.fragment_profile_tweet.view.*
import org.koin.android.ext.android.inject

class ProfileTweetFragment : Fragment(), ProfileTweetContract.View {

    private val utilProvider: UtilProvider by inject()
    private lateinit var mPresenter: ProfileTweetContract.Presenter
    lateinit var profileTweetListView: LinearLayout
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var mRecyclerView: RecyclerView

    companion object {
        fun newInstance() = ProfileTweetFragment()
    }

    private var itemListner: ProfileTweetContract.ProfileItemListner = object : ProfileTweetContract.ProfileItemListner {
        override fun onAccountClick() {
            debugLog("start")
            debugLog("end")
        }

        override fun onImageClick(mediaUrl: String) {
            debugLog("start")
            debugLog("end")
        }

        override fun onTweetClick() {
            debugLog("start")
            debugLog("end")
        }

    }

    private val profileTweetAdapter = ProfileTweetAdapter(ArrayList(0), itemListner, utilProvider)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var root: View = inflater.inflate(fragment_profile_tweet, container, false)

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
            override fun onLoadMore(currentPage: Int) {
                debugLog("current page is " + currentPage)
                mPresenter.loadMoreTweetList(currentPage)
            }
        })

        //divier set
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        mRecyclerView.addItemDecoration(itemDecoration)

        setHasOptionsMenu(true)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        profile_tweet_list.adapter = profileTweetAdapter
    }

    override fun onResume() {
        super.onResume()
        mPresenter.start()
    }

    override fun setPresenter(presenter: ProfileTweetContract.Presenter) {
        debugLog("start")
        ensureNotNull(presenter) { p ->
            mPresenter = p
        }
        debugLog("end")
    }

    override fun showError(e: Throwable) {
        Snackbar.make(view!!, e.localizedMessage, Snackbar.LENGTH_LONG).show()
    }

}