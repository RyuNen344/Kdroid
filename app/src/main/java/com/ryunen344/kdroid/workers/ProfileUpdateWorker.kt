package com.ryunen344.kdroid.workers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.domain.repository.TwitterMediaRepository
import com.ryunen344.kdroid.util.LogUtil
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject


class ProfileUpdateWorker(appContext : Context, workerParams : WorkerParameters) : Worker(appContext, workerParams), KoinComponent {

    private var mTwitterSource : TwitterMediaRepository
    private val appProvider : AppProvider by inject()

    companion object {
        const val WORK_ID_PROFILE_IMAGE : String = "work_id_profile_image"
        const val WORK_ID_PROFILE_BANNER_IMAGE : String = "work_id_profile_banner_image"
        const val KEY_LOCAL_IMAGE_URL : String = "key_local_image_url"
        const val KEY_ONLINE_IMAGE_URL : String = "key_online_image_url"
    }

    init {
        mTwitterSource = appProvider.provideRetrofit().create(TwitterMediaRepository::class.java)
    }

    @SuppressLint("CheckResult")
    override fun doWork() : Result {
        LogUtil.d()
        var localFileName : String
        var dataUrl : String
        var isSuccess : Boolean = false

        inputData.getString(KEY_LOCAL_IMAGE_URL).let {
            localFileName = it!!
        }
        LogUtil.d(localFileName)

        inputData.getString(KEY_ONLINE_IMAGE_URL).let {
            dataUrl = it!!
        }
        LogUtil.d(dataUrl)

        mTwitterSource.getImageFromUrl(dataUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        {
                            applicationContext.openFileOutput(localFileName.split("/".toRegex()).last(), MODE_PRIVATE).use { fos ->
                                fos.write(it.bytes())
                            }
                            isSuccess = true
                        },
                        {
                            LogUtil.e("fail to save", it)
                        }
                )

        return if (isSuccess) Result.success() else Result.failure()
    }
}