package com.ryunen344.twikot.home

import android.app.Activity
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.WorkManager
import com.ryunen344.twikot.addTweetReply.AddTweetReplyActivity
import com.ryunen344.twikot.di.provider.AppProvider
import com.ryunen344.twikot.entity.AccountAndAccountDetail
import com.ryunen344.twikot.mediaViewer.MediaViewerActivity
import com.ryunen344.twikot.repository.AccountRepositoryImpl
import com.ryunen344.twikot.repository.TwitterRepositoryImpl
import com.ryunen344.twikot.settings.SettingsActivity
import com.ryunen344.twikot.util.LogUtil
import com.ryunen344.twikot.util.splitLastThreeWord
import com.ryunen344.twikot.workers.ProfileUpdateWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject
import twitter4j.Twitter
import twitter4j.auth.AccessToken
import java.io.File
import java.io.File.separator

class HomePresenter(private val userId : Long) : HomeContract.Presenter, KoinComponent {

    private val appProvider : AppProvider by inject()
    private var mTwitter : Twitter = appProvider.provideTwitter()
    private var mAccountAndDetail : AccountAndAccountDetail = AccountAndAccountDetail()
    private var mCompositeDisposable : CompositeDisposable = CompositeDisposable()
    private val accountRepositoryImpl : AccountRepositoryImpl by inject()
    private val twitterRepositoryImpl : TwitterRepositoryImpl by inject()

    override lateinit var view : HomeContract.View

    override fun start() {
        LogUtil.d(userId)
    }

    override fun initTwitter(absoluteDirPath : String?) {
        LogUtil.d()
        val disposable : Disposable = accountRepositoryImpl.findAccountById(userId)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            mTwitter.oAuthAccessToken = AccessToken(it.token, it.tokenSecret)
//                            mAccountAndDetail = it
                            initProfile(absoluteDirPath)
//                            view.showDrawerProfile(it.accountDetails[0].userName, it.account.screenName, it.accountDetails[0].localProfileImage, it.accountDetails[0].localProfileBannerImage)
                        },
                        { e ->
                            LogUtil.e(e)
                            view.showError(e)
                        })
        mCompositeDisposable.add(disposable)
    }

    override fun initProfile(absoluteDirPath : String?) {
        LogUtil.d()
        val disposable : Disposable = twitterRepositoryImpl.getUserByUserId(mTwitter, userId).subscribe(
                {
                    var insertUserName : String = it.name
                    var insertProfileImage : String = it.get400x400ProfileImageURLHttps()
                    var insertLocalProfileImage : String = absoluteDirPath + separator + it.get400x400ProfileImageURLHttps().split("/".toRegex()).last()
                    var insertProfileBannerImage : String = it.profileBanner1500x500URL
                    var insertLocalProfileBannerImage : String = absoluteDirPath + separator + splitLastThreeWord(it.profileBanner1500x500URL + ".png")

                    //compare local profile
//                    if (mAccountAndDetail.accountDetails.isEmpty()) {
//                        accountRepositoryImpl
//                                .insertAccountDetail(AccountDetail(userId, insertUserName, insertProfileImage, insertLocalProfileImage, insertProfileBannerImage, insertLocalProfileBannerImage))
//                                .subscribeOn(Schedulers.io())
//                                .subscribe {
//                                    view.showSuccessfullyUpdateProfile()
//                                }
//                    } else {
//                        if (insertUserName != mAccountAndDetail.accountDetails[0].userName ||
//                                insertProfileImage != mAccountAndDetail.accountDetails[0].profileImage ||
//                                insertProfileBannerImage != mAccountAndDetail.accountDetails[0].profileBannerImage) {
//
//                            accountRepositoryImpl
//                                    .insertAccountDetail(AccountDetail(userId, insertUserName, insertProfileImage, insertLocalProfileImage, insertProfileBannerImage, insertLocalProfileBannerImage))
//                                    .subscribeOn(Schedulers.io())
//                                    .subscribe {
//                                        view.showSuccessfullyUpdateProfile()
//
//                                    }
//                        }
//                    }
                }
                , { e ->
            LogUtil.e(e)
            view.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
    }

    override fun checkImageStatus(internalFileDir : File?) {
        LogUtil.d()

        //制約を作成
        val workManager : WorkManager = WorkManager.getInstance()
        val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiresStorageNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val disposable : Disposable = accountRepositoryImpl.findAccountById(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //                    if (it.accountDetails.isNotEmpty()) {
//                        LogUtil.d(File(it.accountDetails[0].localProfileImage).exists())
//                        if (!File(it.accountDetails[0].localProfileImage).exists()) {
//                            LogUtil.d("work request")
//                            //work manager request save image
//                            workManager.beginUniqueWork(
//                                    ProfileUpdateWorker.WORK_ID_PROFILE_IMAGE,
//                                    ExistingWorkPolicy.REPLACE,
//                                    OneTimeWorkRequestBuilder<ProfileUpdateWorker>()
//                                            .setConstraints(constraints)
//                                            .setInputData(createInputDataForUrl(it.accountDetails[0].localProfileImage!!, it.accountDetails[0].profileImage!!))
//                                            .build()
//                            ).enqueue()
//                        }
//                        LogUtil.d(File(it.accountDetails[0].localProfileBannerImage).exists())
//                        if (!File(it.accountDetails[0].localProfileBannerImage).exists()) {
//                            LogUtil.d("work request")
//                            //work manager request save image
//                            workManager.beginUniqueWork(
//                                    ProfileUpdateWorker.WORK_ID_PROFILE_BANNER_IMAGE,
//                                    ExistingWorkPolicy.REPLACE,
//                                    OneTimeWorkRequestBuilder<ProfileUpdateWorker>()
//                                            .setConstraints(constraints)
//                                            .setInputData(createInputDataForUrl(it.accountDetails[0].localProfileBannerImage!!, it.accountDetails[0].profileBannerImage!!))
//                                            .build()
//                            ).enqueue()
//                        }
//
//                    }
                },
                        { e ->
                            LogUtil.e(e)
                            view.showError(e)
                        })
        mCompositeDisposable.add(disposable)
    }

    private fun createInputDataForUrl(localImageUrl : String, onlineImageUrl : String) : Data {
        var builder : Data.Builder = Data.Builder()
        builder.putString(ProfileUpdateWorker.KEY_LOCAL_IMAGE_URL, localImageUrl)
        builder.putString(ProfileUpdateWorker.KEY_ONLINE_IMAGE_URL, onlineImageUrl)
        return builder.build()
    }

    override fun addNewTweet() {
        view.showAddNewTweet()
    }

    override fun result(requestCode : Int, resultCode : Int) {
        LogUtil.d()
        when (requestCode) {
            AddTweetReplyActivity.REQUEST_ADD_TWEET -> {
                when (resultCode) {
                    Activity.RESULT_OK -> view.showSuccessfullyTweet()
                    Activity.RESULT_CANCELED -> view.showFailTweet()
                }
            }
            SettingsActivity.REQUEST_SETTING -> {
                LogUtil.d()
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

