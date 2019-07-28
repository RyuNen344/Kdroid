package com.ryunen344.kdroid.home

import android.app.Activity
import android.os.Bundle
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ryunen344.kdroid.addTweetReply.AddTweetReplyActivity
import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.domain.database.AccountDatabase
import com.ryunen344.kdroid.domain.entity.AccountAndAccountDetail
import com.ryunen344.kdroid.domain.entity.AccountDetail
import com.ryunen344.kdroid.domain.repository.AccountDao
import com.ryunen344.kdroid.mediaViewer.MediaViewerActivity
import com.ryunen344.kdroid.util.LogUtil
import com.ryunen344.kdroid.util.splitLastThreeWord
import com.ryunen344.kdroid.workers.ProfileUpdateWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import twitter4j.Twitter
import twitter4j.auth.AccessToken
import java.io.File
import java.io.File.separator

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
        LogUtil.d()
        if (mUserId == 0L) {
            homeView.showError(Throwable("user id not found"))
        }
    }

    override fun initTwitter(absoluteDirPath : String?) {
        LogUtil.d()
        mAccountDatabase?.let { accountDatabase ->
            val accountDao : AccountDao = accountDatabase.accountDao()

            accountDao.loadAccountById(mUserId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            {
                                mTwitter.oAuthAccessToken = AccessToken(it.account.token, it.account.tokenSecret)
                                mAccountAndDetail = it
                                initProfile(absoluteDirPath)
                                homeView.showDrawerProfile(it.accountDetails[0].userName, it.account.screenName, it.accountDetails[0].localProfileImage, it.accountDetails[0].localProfileBannerImage)
                            },
                            { e ->
                                LogUtil.e(e)
                                homeView.showError(e)
                            })
        }
    }

    override fun initProfile(absoluteDirPath : String?) {
        LogUtil.d()
        val disposable : Disposable = apiProvider.getUserByUserId(mTwitter, mUserId).subscribe(
                {
                    var insertUserName : String = it.name
                    var insertProfileImage : String = it.get400x400ProfileImageURLHttps()
                    var insertLocalProfileImage : String = absoluteDirPath + separator + it.get400x400ProfileImageURLHttps().split("/".toRegex()).last()
                    var insertProfileBannerImage : String = it.profileBanner1500x500URL
                    var insertLocalProfileBannerImage : String = absoluteDirPath + separator + splitLastThreeWord(it.profileBanner1500x500URL + ".png")

                    //compare local profile
                    if (mAccountAndDetail.accountDetails.isEmpty()) {
                        mAccountDatabase?.let { accountDatabase ->
                            val accountDao : AccountDao = accountDatabase.accountDao()

                            accountDao
                                    .insertAccountDetail(AccountDetail(mUserId, insertUserName, insertProfileImage, insertLocalProfileImage, insertProfileBannerImage, insertLocalProfileBannerImage))
                                    .subscribeOn(Schedulers.io())
                                    .subscribe {
                                        homeView.showSuccessfullyUpdateProfile()
                                    }
                        }
                    } else {
                        if (insertUserName != mAccountAndDetail.accountDetails[0].userName ||
                                insertProfileImage != mAccountAndDetail.accountDetails[0].profileImage ||
                                insertProfileBannerImage != mAccountAndDetail.accountDetails[0].profileBannerImage) {
                            mAccountDatabase?.let { accountDatabase ->
                                val accountDao : AccountDao = accountDatabase.accountDao()

                                accountDao
                                        .insertAccountDetail(AccountDetail(mUserId, insertUserName, insertProfileImage, insertLocalProfileImage, insertProfileBannerImage, insertLocalProfileBannerImage))
                                        .subscribeOn(Schedulers.io())
                                        .subscribe {
                                            homeView.showSuccessfullyUpdateProfile()
                                        }
                            }
                        }
                    }
                }
                , { e ->
            LogUtil.e(e)
            homeView.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
    }

    override fun checkImageStatus(internalFileDir : File?) {
        LogUtil.d()

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
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                if (it.accountDetails.isNotEmpty()) {
                                    LogUtil.d(File(it.accountDetails[0].localProfileImage).exists())
                                    if (!File(it.accountDetails[0].localProfileImage).exists()) {
                                        LogUtil.d("work request")
                                        //work manager request save image
                                        workManager.beginUniqueWork(
                                                ProfileUpdateWorker.WORK_ID_PROFILE_IMAGE,
                                                ExistingWorkPolicy.REPLACE,
                                                OneTimeWorkRequestBuilder<ProfileUpdateWorker>()
                                                        .setConstraints(constraints)
                                                        .setInputData(createInputDataForUrl(it.accountDetails[0].localProfileImage!!, it.accountDetails[0].profileImage!!))
                                                        .build()
                                        ).enqueue()
                                    }
                                    LogUtil.d(File(it.accountDetails[0].localProfileBannerImage).exists())
                                    if (!File(it.accountDetails[0].localProfileBannerImage).exists()) {
                                        LogUtil.d("work request")
                                        //work manager request save image
                                        workManager.beginUniqueWork(
                                                ProfileUpdateWorker.WORK_ID_PROFILE_BANNER_IMAGE,
                                                ExistingWorkPolicy.REPLACE,
                                                OneTimeWorkRequestBuilder<ProfileUpdateWorker>()
                                                        .setConstraints(constraints)
                                                        .setInputData(createInputDataForUrl(it.accountDetails[0].localProfileBannerImage!!, it.accountDetails[0].profileBannerImage!!))
                                                        .build()
                                        ).enqueue()
                                    }

                                }
                            },
                            { e ->
                                LogUtil.e(e)
                                homeView.showError(e)
                            })

        }
    }

    private fun createInputDataForUrl(localImageUrl : String, onlineImageUrl : String) : Data {
        var builder : Data.Builder = Data.Builder()
        builder.putString(ProfileUpdateWorker.KEY_LOCAL_IMAGE_URL, localImageUrl)
        builder.putString(ProfileUpdateWorker.KEY_ONLINE_IMAGE_URL, onlineImageUrl)
        return builder.build()
    }

    override fun addNewTweet() {
        homeView.showAddNewTweet()
    }

    override fun result(requestCode : Int, resultCode : Int) {
        LogUtil.d()
        when (requestCode) {
            AddTweetReplyActivity.REQUEST_ADD_TWEET -> {
                when (resultCode) {
                    Activity.RESULT_OK -> homeView.showSuccessfullyTweet()
                    Activity.RESULT_CANCELED -> homeView.showFailTweet()
                }
            }
            MediaViewerActivity.REQUEST_SHOW_MEDIA -> {
                LogUtil.d("media finish()")
            }
        }
    }

    override fun clearDisposable() {
        mCompositeDisposable.clear()
    }

}

