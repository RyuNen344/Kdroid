package com.ryunen344.kdroid.mediaViewer

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment.getExternalStorageDirectory
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.errorLog
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.annotation.Target as Target1


class MediaViewerPresenter(var mediaViewerView: MediaViewerContract.View, var appProvider: AppProvider, val mediaUrl: String) : MediaViewerContract.Presenter {

    init {
        mediaViewerView.setPresenter(this)
    }

    private var mPicasso: Picasso = Picasso.get()

    override fun start() {
        debugLog("start")
        mediaViewerView.showImage(mediaUrl)
        debugLog("end")
    }

    override fun saveImage() {
        debugLog("start")
        mPicasso
                .load(mediaUrl)
                .placeholder(R.drawable.ic_loading_image_24dp)
                .error(R.drawable.ic_loading_image_24dp)
                .into(getTarget("test"))
        debugLog("end")
    }

    override fun reloadImage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun rotateRight() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun rotateLeft() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //target to save
    private fun getTarget(url: String): Target {
        return object : Target {

            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                Thread(Runnable {
                    val file = File(getExternalStorageDirectory().path + "/" + url)
                    try {
                        file.createNewFile()
                        val ostream = FileOutputStream(file)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream)
                        ostream.flush()
                        ostream.close()
                    } catch (e: IOException) {
                        errorLog("fail to save", e)
                    }
                }).start()

            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable) {
                debugLog("")
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                debugLog("")
            }
        }
    }

}