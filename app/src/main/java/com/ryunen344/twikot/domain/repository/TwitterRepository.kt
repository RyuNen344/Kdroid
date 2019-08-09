package com.ryunen344.twikot.domain.repository

import io.reactivex.Completable
import io.reactivex.Single
import twitter4j.Paging
import twitter4j.Status
import twitter4j.Twitter
import twitter4j.User

interface TwitterRepository {

    fun updateStatus(twitter : Twitter, status : String) : Completable
    fun getTimeLine(twitter : Twitter, paging : Paging) : Single<MutableList<Status>>
    fun getMention(twitter : Twitter, paging : Paging) : Single<MutableList<Status>>
    fun createFavorite(twitter : Twitter, tweetId : Long) : Single<Status>
    fun destroyFavorite(twitter : Twitter, tweetId : Long) : Single<Status>
    fun createRetweet(twitter : Twitter, tweetId : Long) : Single<Status>
    fun destroyRetweet(twitter : Twitter, tweetId : Long) : Single<Status>
    fun getTweetByTweetId(twitter : Twitter, tweetId : Long) : Single<Status>
    fun getLeastTimeLine(twitter : Twitter) : Single<MutableList<Status>>
    fun getLeastMention(twitter : Twitter, tweetId : Long) : Single<MutableList<Status>>
    fun getUserByUserId(twitter : Twitter, userId : Long) : Single<User>
    fun getUserTimelineByUserId(twitter : Twitter, paging : Paging, userId : Long) : Single<MutableList<Status>>
    fun getUserFavoriteByUserId(twitter : Twitter, paging : Paging, userId : Long) : Single<MutableList<Status>>
    fun getUserFollowByUserId(twitter : Twitter, userId : Long, cursor : Long) : Single<MutableList<User>>
    fun getUserFollowerByUserId(twitter : Twitter, userId : Long, cursor : Long) : Single<MutableList<User>>
    fun getUserByScreenName(twitter : Twitter, screenName : String) : Single<User>
    fun getUserTimelineByScreenName(twitter : Twitter, paging : Paging, screenName : String) : Single<MutableList<Status>>
    fun getUserFavoriteByScreenName(twitter : Twitter, paging : Paging, screenName : String) : Single<MutableList<Status>>
    fun getUserFollowByScreenName(twitter : Twitter, screenName : String, cursor : Long) : Single<MutableList<User>>
    fun getUserFollowerByScreenName(twitter : Twitter, screenName : String, cursor : Long) : Single<MutableList<User>>

}