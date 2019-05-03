package com.ryunen344.kdroid.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ryunen344.kdroid.R.layout.fragment_profile_tweet
import com.ryunen344.kdroid.di.provider.UtilProvider
import com.ryunen344.kdroid.main.EndlessScrollListener
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.ensureNotNull
import kotlinx.android.synthetic.main.fragment_profile_tweet.view.*
import org.koin.android.ext.android.inject

class ProfileTweetFragment : Fragment(), ProfileContract.View {

    private val utilProvider: UtilProvider by inject()
    private lateinit var mPresenter: ProfileContract.Presenter
    lateinit var profileTweetListView: LinearLayout
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var mRecyclerView: RecyclerView

    companion object {
        fun newInstance() = ProfileTweetFragment()
    }

    private var itemListner: ProfileContract.ProfileItemListner = object : ProfileContract.ProfileItemListner {
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

    //private val mainAdapter = MainAdapter(ArrayList(0), itemListner, utilProvider)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var root: View = inflater.inflate(fragment_profile_tweet, container, false)

        with(root) {
            mLayoutManager = LinearLayoutManager(context)
            mRecyclerView = profile_tweet_list.apply {
                this.layoutManager = mLayoutManager
                this.setHasFixedSize(true)
                //this.adapter =
            }
        }

        mRecyclerView.addOnScrollListener(object : EndlessScrollListener(mLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
                debugLog("current page is " + currentPage)
                //mPresenter.loadMoreTweetList(currentPage)
            }
        })

        //divier set
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        mRecyclerView.addItemDecoration(itemDecoration)

        return root
    }

    override fun setPresenter(presenter: ProfileContract.Presenter) {
        debugLog("start")
        ensureNotNull(presenter) { p ->
            mPresenter = p
        }
        debugLog("end")
    }

    override fun showError(e: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}