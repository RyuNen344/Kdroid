package com.ryunen344.kdroid.domain.repository

import io.reactivex.Completable
import io.reactivex.CompletableOnSubscribe
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import twitter4j.Paging
import twitter4j.Status
import twitter4j.Twitter
import twitter4j.User

class TwitterRepositoryImpl : TwitterRepository {

    override fun updateStatus(twitter : Twitter, status : String) : Completable {
        return Completable.create(CompletableOnSubscribe { emitter ->
            try {
                twitter.updateStatus(status)
                emitter.onComplete()
            } catch (t : Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getTimeLine(twitter : Twitter, paging : Paging) : Single<MutableList<Status>> {
        return Single.create(SingleOnSubscribe<MutableList<Status>>
        { emitter ->
            try {
                emitter.onSuccess(twitter.getHomeTimeline(paging))
            } catch (t : Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getMention(twitter : Twitter, paging : Paging) : Single<MutableList<Status>> {
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

    override fun createFavorite(twitter : Twitter, tweetId : Long) : Single<Status> {
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

    override fun destroyFavorite(twitter : Twitter, tweetId : Long) : Single<Status> {
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

    override fun createRetweet(twitter : Twitter, tweetId : Long) : Single<Status> {
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

    override fun destroyRetweet(twitter : Twitter, tweetId : Long) : Single<Status> {
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

    override fun getTweetByTweetId(twitter : Twitter, tweetId : Long) : Single<Status> {
        return Single.create(SingleOnSubscribe<Status>
        { emitter ->
            try {
                emitter.onSuccess(twitter.showStatus(tweetId))
            } catch (t : Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getLeastTimeLine(twitter : Twitter) : Single<MutableList<Status>> {
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

    override fun getLeastMention(twitter : Twitter, tweetId : Long) : Single<MutableList<Status>> {
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

    override fun getUserByUserId(twitter : Twitter, userId : Long) : Single<User> {
        return Single.create(SingleOnSubscribe<User>
        { emitter ->
            try {
                emitter.onSuccess(twitter.showUser(userId))
            } catch (t : Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getUserTimelineByUserId(twitter : Twitter, paging : Paging, userId : Long) : Single<MutableList<Status>> {
        return Single.create(SingleOnSubscribe<MutableList<Status>>
        { emitter ->
            try {
                emitter.onSuccess(twitter.getUserTimeline(userId, paging))
            } catch (t : Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getUserFavoriteByUserId(twitter : Twitter, paging : Paging, userId : Long) : Single<MutableList<Status>> {
        return Single.create(SingleOnSubscribe<MutableList<Status>>
        { emitter ->
            try {
                emitter.onSuccess(twitter.getFavorites(userId, paging))
            } catch (t : Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getUserFollowByUserId(twitter : Twitter, userId : Long, cursor : Long) : Single<MutableList<User>> {
        return Single.create(SingleOnSubscribe<MutableList<User>>
        { emitter ->
            try {
                emitter.onSuccess(twitter.getFriendsList(userId, cursor))
            } catch (t : Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getUserFollowerByUserId(twitter : Twitter, userId : Long, cursor : Long) : Single<MutableList<User>> {
        return Single.create(SingleOnSubscribe<MutableList<User>>
        { emitter ->
            try {
                emitter.onSuccess(twitter.getFollowersList(userId, cursor))
            } catch (t : Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getUserByScreenName(twitter : Twitter, screenName : String) : Single<User> {
        return Single.create(SingleOnSubscribe<User>
        { emitter ->
            try {
                emitter.onSuccess(twitter.showUser(screenName))
            } catch (t : Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getUserTimelineByScreenName(twitter : Twitter, paging : Paging, screenName : String) : Single<MutableList<Status>> {
        return Single.create(SingleOnSubscribe<MutableList<Status>>
        { emitter ->
            try {
                emitter.onSuccess(twitter.getUserTimeline(screenName, paging))
            } catch (t : Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getUserFavoriteByScreenName(twitter : Twitter, paging : Paging, screenName : String) : Single<MutableList<Status>> {
        return Single.create(SingleOnSubscribe<MutableList<Status>>
        { emitter ->
            try {
                emitter.onSuccess(twitter.getFavorites(screenName, paging))
            } catch (t : Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getUserFollowByScreenName(twitter : Twitter, screenName : String, cursor : Long) : Single<MutableList<User>> {
        return Single.create(SingleOnSubscribe<MutableList<User>>
        { emitter ->
            try {
                emitter.onSuccess(twitter.getFriendsList(screenName, cursor))
            } catch (t : Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getUserFollowerByScreenName(twitter : Twitter, screenName : String, cursor : Long) : Single<MutableList<User>> {
        return Single.create(SingleOnSubscribe<MutableList<User>>
        { emitter ->
            try {
                emitter.onSuccess(twitter.getFollowersList(screenName, cursor))
            } catch (t : Throwable) {
                emitter.onError(t)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}