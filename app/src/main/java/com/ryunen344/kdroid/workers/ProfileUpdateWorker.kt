package com.ryunen344.kdroid.workers

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.ryunen344.kdroid.data.api.TwitterSource
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.debugLog
import io.reactivex.Single
import java.io.File.separator
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class ProfileUpdateWorker(appContext : Context, workerParams : WorkerParameters) : RxWorker(appContext, workerParams) {

    private var mTwitterSource : TwitterSource = AppProvider().provideRetrofit().create(TwitterSource::class.java)


    companion object {
        const val WORK_ID_PROFILE_IMAGE : String = "work_id_profile_image"
        const val WORK_ID_PROFILE_BANNER_IMAGE : String = "work_id_profile_banner_image"
        const val KEY_IMAGE_URL : String = "key_image_url"
    }

    override fun createWork() : Single<Result> {
        debugLog("start")
        var succeed : Boolean = false
        var dataUrl : String

        inputData.getString(KEY_IMAGE_URL).let {
            dataUrl = it!!
        }

        return mTwitterSource.getImageFromUrl(dataUrl)
                .doOnSuccess {
                    debugLog("writing image file to internal storage")
                    var inputStream : InputStream? = null
                    var fileOutPutStream : FileOutputStream? = null

                    try {
                        inputStream = it.byteStream()
                        fileOutPutStream = FileOutputStream(applicationContext.filesDir.absolutePath + separator + dataUrl.split("/".toRegex()).last())

                        while ((inputStream.read() != -1)) {
                            fileOutPutStream.write(inputStream.read())
                        }

                    } catch (e : IOException) {
                        Result.failure()
                        debugLog(e)
                    } finally {
                        inputStream.let {
                            inputStream?.close()
                        }

                        fileOutPutStream.let {
                            fileOutPutStream?.close()
                        }
                    }
                    succeed = true
                }
                .map {
                    debugLog(it)
                    Result.success()
                }
                .onErrorReturn {
                    debugLog(it)
                    Result.failure()
                }


//        mTwitterSource.getImageFromUrl(dataUrl)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        {
//                            //save file
//                            debugLog("writing image file to internal storage")
//                            var inputStream: InputStream? = null
//                            var fileOutPutStream: FileOutputStream? = null
//
//                            try {
//                                inputStream = it.byteStream()
//                                fileOutPutStream = FileOutputStream(applicationContext.filesDir.absolutePath + separator + dataUrl.split("/".toRegex()).last())
//
//                                while((inputStream.read() != -1)){
//                                    fileOutPutStream.write(inputStream.read())
//                                }
//
//                            }catch (e: IOException){
//                                succeed = false
//                                debugLog(e)
//                            }finally {
//                                inputStream.let {
//                                    inputStream?.close()
//                                }
//
//                                fileOutPutStream.let {
//                                    fileOutPutStream?.close()
//                                }
//                            }
//
//                            succeed = true
//                        },
//                        {
//                            debugLog(it)
//                        }
//                )
//
//        debugLog("end")
//        return if(succeed) Single.just(Result.success())  else Single.just(Result.failure())
    }
}