package com.ryunen344.twikot.workers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ryunen344.twikot.domain.repository.TwitterMediaRepositoryImpl
import com.ryunen344.twikot.util.LogUtil
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject


class ProfileUpdateWorker(appContext : Context, workerParams : WorkerParameters) : Worker(appContext, workerParams), KoinComponent {

    private val twitterMediaRepositoryImpl : TwitterMediaRepositoryImpl by inject()

    companion object {
        const val WORK_ID_PROFILE_IMAGE : String = "work_id_profile_image"
        const val WORK_ID_PROFILE_BANNER_IMAGE : String = "work_id_profile_banner_image"
        const val KEY_LOCAL_IMAGE_URL : String = "key_local_image_url"
        const val KEY_ONLINE_IMAGE_URL : String = "key_online_image_url"
    }

    @SuppressLint("CheckResult")
    override fun doWork() : Result {
        LogUtil.d()
        lateinit var localFileName : String
        lateinit var dataUrl : String
        var isSuccess : Boolean = false

        inputData.getString(KEY_LOCAL_IMAGE_URL)?.let {
            localFileName = it
        }
        LogUtil.d(localFileName)

        inputData.getString(KEY_ONLINE_IMAGE_URL)?.let {
            dataUrl = it
        }
        LogUtil.d(dataUrl)

        val disposable : Disposable = twitterMediaRepositoryImpl.getImageFromUrl(dataUrl)
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