package com.ryunen344.kdroid.profile.user

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
import com.ryunen344.kdroid.R.layout.fragment_profile_user
import com.ryunen344.kdroid.behavior.EndlessScrollListener
import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.di.provider.UtilProvider
import com.ryunen344.kdroid.mediaViewer.MediaViewerActivity
import com.ryunen344.kdroid.profile.ProfileActivity
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.ensureNotNull
import kotlinx.android.synthetic.main.fragment_profile_user.*
import kotlinx.android.synthetic.main.fragment_profile_user.view.*
import org.koin.android.ext.android.inject
import twitter4j.User

class ProfileUserFragment : Fragment(), ProfileUserContract.View {


    private val appProvider: AppProvider by inject()
    private val apiProvider: ApiProvider by inject()
    private val utilProvider: UtilProvider by inject()
    private lateinit var mPresenter: ProfileUserContract.Presenter
    lateinit var profileUserListView: LinearLayout
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var mRecyclerView: RecyclerView
    var mPagerPosition: Int = 0
    private var mUserId: Long = 0L
    private var mScreenName: String = ""

    companion object {
        fun newInstance() = ProfileUserFragment()
    }

    private var itemListner: ProfileUserContract.ProfileItemListner = object : ProfileUserContract.ProfileItemListner {
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

    private val profileUserAdapter = ProfileUserAdapter(ArrayList(0), itemListner, appProvider, utilProvider)

    override fun onCreate(savedInstanceState: Bundle?) {
        debugLog("start")
        super.onCreate(savedInstanceState)
        ensureNotNull(activity) {
            mUserId = it.intent.getLongExtra(ProfileActivity.INTENT_KEY_USER_ID, 0)
            ensureNotNull(it.intent.getStringExtra(ProfileActivity.INTENT_KEY_SCREEN_NAME)) {
                mScreenName = it
            }
        }
        debugLog("setPresenter")
        ProfileUserPresenter(this, appProvider, apiProvider, mPagerPosition, mUserId, mScreenName)
        debugLog("end")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var root: View = inflater.inflate(fragment_profile_user, container, false)

        with(root) {
            mLayoutManager = LinearLayoutManager(context)
            mRecyclerView = profile_user_list.apply {
                this.layoutManager = mLayoutManager
                this.setHasFixedSize(true)
                this.adapter = profileUserAdapter
            }
            profileUserListView = profileUserLL
        }

        mRecyclerView.addOnScrollListener(object : EndlessScrollListener(mLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        debugLog("start")
        super.onViewCreated(view, savedInstanceState)
        profile_user_list.adapter = profileUserAdapter
        mPresenter.start()
        debugLog("end")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
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
        super.onDestroy()
        mPresenter.clearDisposable()
    }

    override fun showUserList(userList: List<User>) {
        profileUserAdapter.profileUserList = userList
        profileUserAdapter.notifyDataSetChanged()
    }

    override fun showMediaViewer(mediaUrl: String) {
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

    override fun setPresenter(presenter: ProfileUserContract.Presenter) {
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