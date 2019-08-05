package com.ryunen344.kdroid.profile.user

import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.domain.repository.TwitterRepositoryImpl
import com.ryunen344.kdroid.util.LogUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.koin.core.KoinComponent
import org.koin.core.inject
import twitter4j.Paging
import twitter4j.Twitter
import twitter4j.User

class ProfileUserPresenter(private val pagerPosition : Int, private val userId : Long, private val screenName : String) : ProfileUserContract.Presenter, KoinComponent {

    lateinit var userList : List<User>
    private val appProvider : AppProvider by inject()
    private var twitter : Twitter = appProvider.provideTwitter()
    private var mCompositeDisposable : CompositeDisposable = CompositeDisposable()
    private val twitterRepositoryImpl : TwitterRepositoryImpl by inject()

    override lateinit var view : ProfileUserContract.View

    override fun start() {
        LogUtil.d()
        when (pagerPosition) {
            2 -> loadFollowList()
            3 -> loadFollowerList()
        }
    }

    override fun loadFollowList() {
        LogUtil.d()
        var pageing : Paging = Paging(1, 50)
        val disposable : Disposable =
                when (userId) {
                    0L -> {
                        twitterRepositoryImpl.getUserFollowByScreenName(twitter, screenName, -1).subscribe(
                                { list : List<User> ->
                                    userList = list
                                    view.showUserList(list)
                                }
                                , { e ->
                            view.showError(e)
                        }
                        )
                    }
                    else -> {
                        twitterRepositoryImpl.getUserFollowByUserId(twitter, userId, -1).subscribe(
                                { list : List<User> ->
                                    userList = list
                                    view.showUserList(list)
                                }
                                , { e ->
                            view.showError(e)
                        }
                        )
                    }
                }
        mCompositeDisposable.add(disposable)
    }

    override fun loadFollowerList() {
        LogUtil.d()
        var pageing : Paging = Paging(1, 50)
        val disposable : Disposable =
                when (userId) {
                    0L -> {
                        twitterRepositoryImpl.getUserFollowerByScreenName(twitter, screenName, -1).subscribe(
                                { list : List<User> ->
                                    userList = list
                                    view.showUserList(list)
                                }
                                , { e ->
                            view.showError(e)
                        }
                        )
                    }
                    else -> {
                        twitterRepositoryImpl.getUserFollowerByUserId(twitter, userId, -1).subscribe(
                                { list : List<User> ->
                                    userList = list
                                    view.showUserList(list)
                                }
                                , { e ->
                            view.showError(e)
                        }
                        )
                    }

                }
        mCompositeDisposable.add(disposable)
    }

    override fun loadMoreList(currentPage : Int) {
        LogUtil.d()
        when (pagerPosition) {
            2 -> loadMoreFollowList(currentPage)
            3 -> loadMoreFollowerList(currentPage)
        }
    }

    private fun loadMoreFollowList(currentPage : Int) {
        LogUtil.d()
    }

    private fun loadMoreFollowerList(currentPage : Int) {
        LogUtil.d()
    }

    override fun clearDisposable() {
        LogUtil.d()
        mCompositeDisposable.clear()
    }
}