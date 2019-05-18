package com.ryunen344.kdroid.home

import android.app.Activity
import com.ryunen344.kdroid.addTweetReply.AddTweetReplyActivity
import com.ryunen344.kdroid.data.dao.AccountDao
import com.ryunen344.kdroid.data.db.AccountDatabase
import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.mediaViewer.MediaViewerActivity
import com.ryunen344.kdroid.util.debugLog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import twitter4j.Paging
import twitter4j.Status
import twitter4j.Twitter
import twitter4j.User
import twitter4j.auth.AccessToken

class MainPresenter(val mainView : MainContract.View, val appProvider : AppProvider, val apiProvider : ApiProvider, val userId : Long) : MainContract.Presenter {

    lateinit var tweetList : List<Status>
    var twitter : Twitter = appProvider.provideTwitter()
    var mCompositeDisposable : CompositeDisposable = CompositeDisposable()

    init {
        mainView.setPresenter(this)
    }

    override fun start() {
        loadTweetList()
    }

    override fun loadTweetList() {
        AccountDatabase.getInstance()?.let { accountDatabase ->
            val accountDao : AccountDao = accountDatabase.accountDao()

            accountDao.loadAccountById(userId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            {
                                twitter.oAuthAccessToken = AccessToken(it.token, it.tokenSecret)
                                var paging : Paging = Paging(1, 50)
                                val disposable : Disposable = apiProvider.getTimeLine(twitter, paging).subscribe(
                                        { list : List<Status> ->
                                            tweetList = list
                                            mainView.showTweetList(list)
                                        }
                                        , { e ->
                                    mainView.showError(e)
                                }
                                )
                                mCompositeDisposable.add(disposable)

                            },
                            { e ->
                                mainView.showError(e)
                            })
        }

    }

    override fun loadMoreTweetList(currentPage : Int) {
        var paging : Paging = Paging(currentPage + 1, 100)
        val disposable : Disposable = apiProvider.getTimeLine(twitter, paging).subscribe(
                { list : List<Status> ->
                    tweetList = tweetList + list
                    mainView.showTweetList(tweetList)
                }
                , { e ->
            mainView.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
    }

    override fun addNewTweet() {
        mainView.showAddNewTweet()
    }

    override fun openMedia(mediaUrl : String) {
        mainView.showMediaViewer(mediaUrl)
    }

    override fun openProfile(user : User) {
        mainView.showProfile(user)
    }

    override fun openTweetDetail() {
        mainView.showTweetDetail()
    }

    override fun result(requestCode : Int, resultCode : Int) {
        debugLog("start")
        when (requestCode) {
            AddTweetReplyActivity.REQUEST_ADD_TWEET -> {
                when (resultCode) {
                    Activity.RESULT_OK -> mainView.showSuccessfullyTweet()
                    Activity.RESULT_CANCELED -> mainView.showFailTweet()
                }
            }
            MediaViewerActivity.REQUEST_SHOW_MEDIA -> {
                debugLog("media finish()")
            }
        }
        debugLog("end")
    }

    override fun clearDisposable() {
        mCompositeDisposable.clear()
    }

}

