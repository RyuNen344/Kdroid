package com.ryunen344.kdroid.workers

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ryunen344.kdroid.data.api.TwitterSource
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.errorLog
import com.ryunen344.kdroid.util.splitLastThreeWord
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class ProfileUpdateWorker(appContext : Context, workerParams : WorkerParameters) : Worker(appContext, workerParams) {

    private var mTwitterSource : TwitterSource = Retrofit.Builder()
            .baseUrl("https://google.co.jp")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(TwitterSource::class.java)


    companion object {
        const val WORK_ID_PROFILE_IMAGE : String = "work_id_profile_image"
        const val WORK_ID_PROFILE_BANNER_IMAGE : String = "work_id_profile_banner_image"
        const val KEY_IMAGE_URL : String = "key_image_url"
        const val KEY_IS_BANNER : String = "key_is_banner"
    }

    override fun doWork() : Result {
        debugLog("start")
        var dataUrl : String
        var isBanner : Boolean

        inputData.getString(KEY_IMAGE_URL).let {
            dataUrl = it!!
        }
        debugLog(dataUrl)

        inputData.getBoolean(KEY_IS_BANNER, false).let {
            isBanner = it
        }
        debugLog(isBanner)

        mTwitterSource.getImageFromUrl(dataUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    //                    debugLog("writing image file to internal storage")
//                    var inputStream : InputStream? = null
//                    var fileOutPutStream : FileOutputStream? = null

//                    File(applicationContext.filesDir, splitLastThreeWord(dataUrl).split("/".toRegex()).last()).outputStream().use { fos ->
//                        fos.write(it.bytes())
//                    }
                    var fileName = if (isBanner) splitLastThreeWord(dataUrl + ".png") else dataUrl.split("/".toRegex()).last()
                    applicationContext.openFileOutput(fileName, MODE_PRIVATE).use { fos ->
                        fos.write(it.bytes())
                    }
//
//                    try {
//                        var f = File(applicationContext.filesDir.absolutePath + separator + splitLastThreeWord(dataUrl))
//                        inputStream = it.byteStream()
//                        fileOutPutStream = applicationContext.openFileOutput(applicationContext.filesDir.absolutePath + separator + splitLastThreeWord(dataUrl), MODE_PRIVATE)
//                        fileOutPutStream.write(it.bytes())
//
//                    } catch (e : IOException) {
//                        Result.failure()
//                        debugLog(e)
//                    } finally {
//                        inputStream.let {
//                            inputStream?.close()
//                        }
//
//                        fileOutPutStream.let {
//                            fileOutPutStream?.close()
//                        }
//                    }
//                    succeed = true
                },
                        {
                            errorLog("fail to save", it)
                        }
                )

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