package com.ryunen344.kdroid.main

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.R.layout.fragment_main
import com.ryunen344.kdroid.addTweetReply.AddTweetReplyActivity
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.di.provider.UtilProvider
import com.ryunen344.kdroid.mediaViewer.MediaViewerActivity
import com.ryunen344.kdroid.profile.ProfileActivity
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.ensureNotNull
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import org.koin.android.ext.android.inject
import twitter4j.Status
import twitter4j.User

class MainFragment : Fragment(), MainContract.View {

    val appProvider: AppProvider by inject()
    val utilProvider: UtilProvider by inject()
    lateinit var mPresenter: MainContract.Presenter
    lateinit var mainListView: LinearLayout
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var mRecyclerView: RecyclerView

    companion object {
        fun newInstance() = MainFragment()
    }


    private var itemListener: MainContract.MainItemListner = object : MainContract.MainItemListner {
        override fun onImageClick(mediaUrl: String) {
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

        override fun onAccountClick(user: User) {
            //fixme
            debugLog("start")
            mPresenter.openProfile(user)
            debugLog("end")
        }
    }

    private val mainAdapter = MainAdapter(ArrayList(0), itemListener, appProvider, utilProvider)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var root: View = inflater.inflate(fragment_main, container, false)

        with(root) {
            mLayoutManager = LinearLayoutManager(context)
            mRecyclerView = main_list.apply {
                this.layoutManager = mLayoutManager
                this.setHasFixedSize(true)
                this.adapter = mainAdapter
            }
            mainListView = mainLL
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

        //configure float action button
        activity?.fab?.setOnLongClickListener { view ->
            Snackbar.make(view, "Long tap action", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
            true
        }
        activity?.fab?.setOnClickListener {
            showAddNewTweet()
        }

        //configure timeline_navigation bar
        activity?.navigation!!.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        main_list.adapter = mainAdapter
        mPresenter.start()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.clearDisposable()
    }

    override fun showTweetList(mainList: List<Status>) {
        mainAdapter.mainList = mainList
        mainAdapter.notifyDataSetChanged()
    }

    override fun showAddNewTweet() {
        //fixme
        val intent = Intent(context, AddTweetReplyActivity::class.java)
        startActivityForResult(intent, AddTweetReplyActivity.REQUEST_ADD_TWEET)
    }

    override fun showMediaViewer(mediaUrl: String) {
        val intent = Intent(context, MediaViewerActivity::class.java).apply {
            putExtra(MediaViewerActivity.INTENT_KEY_MEDIA_URL, mediaUrl)
        }
        startActivityForResult(intent, MediaViewerActivity.REQUEST_SHOW_MEDIA,
                ActivityOptions.makeCustomAnimation(context, 0, 0).toBundle())
    }

    override fun showTweetDetail() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProfile(user: User) {
        val intent = Intent(context, ProfileActivity::class.java).apply {
            putExtra(ProfileActivity.INTENT_KEY_USER_ID, user.id)
        }
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mPresenter.result(requestCode, resultCode)
    }

    override fun showSuccessfullyTweet() {
        debugLog("start")
        Snackbar.make(view!!, "tweet sent", Snackbar.LENGTH_LONG).show()
        debugLog("end")

    }

    override fun showFailTweet() {
        Snackbar.make(view!!, "tweet fail", Snackbar.LENGTH_LONG).show()
    }

    override fun showError(e: Throwable) {
        Snackbar.make(view!!, e.localizedMessage, Snackbar.LENGTH_LONG).show()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.timeline_navigation, menu)
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        ensureNotNull(presenter) { p ->
            mPresenter = p
        }
    }


}