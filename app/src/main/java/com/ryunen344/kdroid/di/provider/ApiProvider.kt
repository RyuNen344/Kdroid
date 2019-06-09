package com.ryunen344.kdroid.di.provider

import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import twitter4j.Paging
import twitter4j.Status
import twitter4j.Twitter
import twitter4j.User

class ApiProvider {

    fun getTimeLine(twitter : Twitter, paging : Paging) : Single<MutableList<Status>> {
        return Single.create(SingleOnSubscribe<MutableList<Status>>
        { emitter ->
            try {
                emitter.onSuccess(twitter.getHomeTimeline(paging))
            } catch (t: Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getMention(twitter : Twitter, paging : Paging) : Single<MutableList<Status>> {
        return Single.create(SingleOnSubscribe<MutableList<Status>>
        { emitter ->
            try {
                emitter.onSuccess(twitter.getMentionsTimeline(paging))
            } catch (t : Throwable) {
                emitter.onError(t)
            }

        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun createFavorite(twitter : Twitter, tweetId : Long) : Single<Status> {
        return Single.create(SingleOnSubscribe<Status> { emitter ->
            try {
                emitter.onSuccess(twitter.createFavorite(tweetId))
            } catch (t : Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun destroyFavorite(twitter : Twitter, tweetId : Long) : Single<Status> {
        return Single.create(SingleOnSubscribe<Status> { emitter ->
            try {
                emitter.onSuccess(twitter.destroyFavorite(tweetId))
            } catch (t : Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun createRetweet(twitter : Twitter, tweetId : Long) : Single<Status> {
        return Single.create(SingleOnSubscribe<Status> { emitter ->
            try {
                emitter.onSuccess(twitter.retweetStatus(tweetId))
            } catch (t : Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun destroyRetweet(twitter : Twitter, tweetId : Long) : Single<Status> {
        return Single.create(SingleOnSubscribe<Status> { emitter ->
            try {
                emitter.onSuccess(twitter.unRetweetStatus(tweetId))
            } catch (t : Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLeastTimeLine(twitter : Twitter) : Single<MutableList<Status>> {
        return Single.create(SingleOnSubscribe<MutableList<Status>>
        { emitter ->
            try {
                emitter.onSuccess(twitter.homeTimeline)
            } catch (t : Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLeastMention() {

    }

    fun getUserByUserId(twitter: Twitter, userId: Long): Single<User> {
        return Single.create(SingleOnSubscribe<User>
        { emitter ->
            try {
                emitter.onSuccess(twitter.showUser(userId))
            } catch (t: Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getUserTimelineByUserId(twitter : Twitter, paging : Paging, userId : Long) : Single<MutableList<Status>> {
        return Single.create(SingleOnSubscribe<MutableList<Status>>
        { emitter ->
            try {
                emitter.onSuccess(twitter.getUserTimeline(userId, paging))
            } catch (t: Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getUserFavoriteByUserId(twitter : Twitter, paging : Paging, userId : Long) : Single<MutableList<Status>> {
        return Single.create(SingleOnSubscribe<MutableList<Status>>
        { emitter ->
            try {
                emitter.onSuccess(twitter.getFavorites(userId, paging))
            } catch (t: Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getUserFollowByUserId(twitter : Twitter, userId : Long, cursor : Long) : Single<MutableList<User>> {
        return Single.create(SingleOnSubscribe<MutableList<User>>
        { emitter ->
            try {
                emitter.onSuccess(twitter.getFriendsList(userId, cursor))
            } catch (t: Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getUserFollowerByUserId(twitter : Twitter, userId : Long, cursor : Long) : Single<MutableList<User>> {
        return Single.create(SingleOnSubscribe<MutableList<User>>
        { emitter ->
            try {
                emitter.onSuccess(twitter.getFollowersList(userId, cursor))
            } catch (t: Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


}