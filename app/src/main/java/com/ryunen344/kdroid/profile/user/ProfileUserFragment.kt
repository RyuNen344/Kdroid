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
import com.ryunen344.kdroid.mediaViewer.MediaViewerActivity
import com.ryunen344.kdroid.profile.ProfileActivity
import com.ryunen344.kdroid.util.LogUtil
import com.ryunen344.kdroid.util.ensureNotNull
import kotlinx.android.synthetic.main.fragment_profile_user.*
import kotlinx.android.synthetic.main.fragment_profile_user.view.*
import org.koin.android.scope.currentScope
import twitter4j.User

class ProfileUserFragment : Fragment(), ProfileUserContract.View {

    override val presenter : ProfileUserContract.Presenter by currentScope.inject()

    lateinit var profileUserListView : LinearLayout
    lateinit var mLayoutManager : LinearLayoutManager
    lateinit var mRecyclerView : RecyclerView
    var mPagerPosition : Int = 0
    private var mUserId : Long = 0L
    private var mScreenName : String = ""

    companion object {
        const val INTENT_KEY_PAGER_POSITION : String = "key_pager_position"
    }

    private var itemListner : ProfileUserContract.ProfileItemListener = object : ProfileUserContract.ProfileItemListener {
        override fun onAccountClick() {
            LogUtil.d()
        }

        override fun onImageClick(mediaUrl : String) {
            LogUtil.d()
        }

        override fun onTweetClick() {
            LogUtil.d()
        }

    }

    private val profileUserAdapter = ProfileUserAdapter(ArrayList(0), itemListner)

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
        presenter.view = this
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var root : View = inflater.inflate(fragment_profile_user, container, false)

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
        profile_user_list.adapter = profileUserAdapter
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
        super.onDestroy()
        presenter.clearDisposable()
    }

    override fun showUserList(userList : List<User>) {
        profileUserAdapter.profileUserList = userList
        profileUserAdapter.notifyDataSetChanged()
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

    override fun showProfile() {
        LogUtil.d()
    }

    override fun showError(e : Throwable) {
        Snackbar.make(view!!, e.localizedMessage, Snackbar.LENGTH_LONG).show()
    }

}