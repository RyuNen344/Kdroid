package com.ryunen344.kdroid.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ryunen344.kdroid.data.api.TwitterSource
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.splitLastThreeWord
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File.separator
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class ProfileUpdateWorker(appContext : Context, workerParams : WorkerParameters) : Worker(appContext, workerParams) {

    private var mTwitterSource : TwitterSource = Retrofit.Builder()
            .baseUrl("https://s3.amazon.com/profile-picture/path/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(TwitterSource::class.java)


    companion object {
        const val WORK_ID_PROFILE_IMAGE : String = "work_id_profile_image"
        const val WORK_ID_PROFILE_BANNER_IMAGE : String = "work_id_profile_banner_image"
        const val KEY_IMAGE_URL : String = "key_image_url"
    }

    override fun doWork() : Result {
        debugLog("start")
        var succeed : Boolean = false
        var dataUrl : String

        inputData.getString(KEY_IMAGE_URL).let {
            dataUrl = it!!
        }

        mTwitterSource.getImageFromUrl(dataUrl)
                .doOnSuccess {
                    debugLog("writing image file to internal storage")
                    var inputStream : InputStream? = null
                    var fileOutPutStream : FileOutputStream? = null

                    try {
                        inputStream = it.byteStream()
                        fileOutPutStream = FileOutputStream(applicationContext.filesDir.absolutePath + separator + splitLastThreeWord(dataUrl))

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
        return Result.success()
    }
}