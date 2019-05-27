package com.ryunen344.kdroid.home

import android.app.Activity
import android.os.Bundle
import androidx.work.*
import com.ryunen344.kdroid.addTweetReply.AddTweetReplyActivity
import com.ryunen344.kdroid.data.AccountAndAccountDetail
import com.ryunen344.kdroid.data.AccountDetail
import com.ryunen344.kdroid.data.dao.AccountDao
import com.ryunen344.kdroid.data.db.AccountDatabase
import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.mediaViewer.MediaViewerActivity
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.errorLog
import com.ryunen344.kdroid.util.splitLastThreeWord
import com.ryunen344.kdroid.workers.ProfileUpdateWorker
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import twitter4j.Twitter
import twitter4j.auth.AccessToken
import java.io.File

class HomePresenter(val homeView : HomeContract.View, val appProvider : AppProvider, val apiProvider : ApiProvider, val bundle : Bundle?) : HomeContract.Presenter {

    var mTwitter : Twitter = appProvider.provideTwitter()
    private val mAccountDatabase : AccountDatabase? = AccountDatabase.getInstance()
    private var mUserId : Long = 0L
    private var mAccountAndDetail : AccountAndAccountDetail = AccountAndAccountDetail()
    var mCompositeDisposable : CompositeDisposable = CompositeDisposable()

    init {
        bundle?.let {
            mUserId = it.getLong(HomeActivity.INTENT_KEY_USER_ID, 0)
        }
        homeView.setPresenter(this)
    }

    override fun start() {
        debugLog("start")
        if (mUserId == 0L) {
            homeView.showError(Throwable("user id not found"))
        } else {
            initTwitter()
        }
        debugLog("end")
    }

    override fun initTwitter() {
        debugLog("start")
        mAccountDatabase?.let { accountDatabase ->
            val accountDao : AccountDao = accountDatabase.accountDao()

            accountDao.loadAccountById(mUserId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            {
                                mTwitter.oAuthAccessToken = AccessToken(it.account.token, it.account.tokenSecret)
                                mAccountAndDetail = it
                                initProfile()
                            },
                            { e ->
                                errorLog(e.localizedMessage, e)
                                homeView.showError(e)
                            })
        }
        debugLog("end")
    }

    override fun initProfile() {
        debugLog("start")
        val disposable : Disposable = apiProvider.getUserByUserId(mTwitter, mUserId).subscribe(
                {
                    var insertUserName : String = it.name
                    var insertProfileImage : String = it.get400x400ProfileImageURLHttps()
                    var insertProfileBannerImage : String = it.profileBanner1500x500URL

                    //compare local profile
                    if (mAccountAndDetail.accountDetails.isNotEmpty()) {
                        if (insertUserName != mAccountAndDetail.accountDetails[0].userName ||
                                insertProfileImage != mAccountAndDetail.accountDetails[0].profileImage ||
                                insertProfileBannerImage != mAccountAndDetail.accountDetails[0].profileBannerImage) {
                            mAccountDatabase?.let { accountDatabase ->
                                val accountDao : AccountDao = accountDatabase.accountDao()

                                accountDao
                                        .insertAccountDetail(AccountDetail(mUserId, insertUserName, insertProfileImage, insertProfileBannerImage))
                                        .subscribeOn(Schedulers.io())
                                        .subscribe {
                                            homeView.showSuccessfullyUpdateProfile()
                                        }
                            }
                        }

                    } else {
                        mAccountDatabase?.let { accountDatabase ->
                            val accountDao : AccountDao = accountDatabase.accountDao()

                            accountDao
                                    .insertAccountDetail(AccountDetail(mUserId, insertUserName, insertProfileImage, insertProfileBannerImage))
                                    .subscribeOn(Schedulers.io())
                                    .subscribe {
                                        homeView.showSuccessfullyUpdateProfile()
                                    }
                        }
                    }
                }
                , { e ->
            errorLog(e.localizedMessage, e)
            homeView.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
        debugLog("end")
    }

    override fun checkImageStatus(internalFileDir : File?) {
        debugLog("start")

        internalFileDir.let { fileDir ->
            mAccountDatabase?.let { accountDatabase ->
                val accountDao : AccountDao = accountDatabase.accountDao()

                //制約を作成
                val workManager : WorkManager = WorkManager.getInstance()
                val constraints = Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .setRequiresStorageNotLow(true)
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()

                accountDao.loadAccountById(mUserId)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                {
                                    if (it.accountDetails.isNotEmpty()) {
                                        if (!File(fileDir, splitLastThreeWord(it.accountDetails[0].profileImage!!)).exists()) {
                                            debugLog("work request")
                                            //work manager request save image
                                            workManager.beginUniqueWork(
                                                    ProfileUpdateWorker.WORK_ID_PROFILE_IMAGE,
                                                    ExistingWorkPolicy.REPLACE,
                                                    OneTimeWorkRequestBuilder<ProfileUpdateWorker>()
                                                            .setConstraints(constraints)
                                                            .setInputData(createInputDataForUrl(it.accountDetails[0].profileImage!!))
                                                            .build()
                                            ).enqueue()
                                        }

                                        if (!File(fileDir, splitLastThreeWord(it.accountDetails[0].profileBannerImage!!)).exists()) {
                                            debugLog("work request")
                                            //work manager request save image
                                            workManager.beginUniqueWork(
                                                    ProfileUpdateWorker.WORK_ID_PROFILE_BANNER_IMAGE,
                                                    ExistingWorkPolicy.REPLACE,
                                                    OneTimeWorkRequestBuilder<ProfileUpdateWorker>()
                                                            .setConstraints(constraints)
                                                            .setInputData(createInputDataForUrl(it.accountDetails[0].profileBannerImage!!))
                                                            .build()
                                            ).enqueue()
                                        }

                                        homeView.showDrawerProfile(it.accountDetails[0].userName, it.account.screenName, it.accountDetails[0].profileImage, it.accountDetails[0].profileBannerImage)
                                    }
                                },
                                { e ->
                                    errorLog(e.localizedMessage, e)
                                    homeView.showError(e)
                                })
            }
        }
        debugLog("end")
    }

    private fun createInputDataForUrl(imageUrl : String) : Data {
        var builder : Data.Builder = Data.Builder()
        builder.putString(ProfileUpdateWorker.KEY_IMAGE_URL, imageUrl)
        return builder.build()
    }

    override fun addNewTweet() {
        homeView.showAddNewTweet()
    }

    override fun result(requestCode : Int, resultCode : Int) {
        debugLog("start")
        when (requestCode) {
            AddTweetReplyActivity.REQUEST_ADD_TWEET -> {
                when (resultCode) {
                    Activity.RESULT_OK -> homeView.showSuccessfullyTweet()
                    Activity.RESULT_CANCELED -> homeView.showFailTweet()
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

