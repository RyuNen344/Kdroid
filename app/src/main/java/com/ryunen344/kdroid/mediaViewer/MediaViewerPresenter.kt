package com.ryunen344.kdroid.mediaViewer

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import androidx.core.net.toUri
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.LogUtil
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File


class MediaViewerPresenter(var mediaViewerView: MediaViewerContract.View, var appProvider: AppProvider, val mediaUrl: String) : MediaViewerContract.Presenter {

    init {
        mediaViewerView.setPresenter(this)
    }

    private var mPicasso: Picasso = appProvider.providePiccaso()

    override fun start() {
        LogUtil.d()
        mediaViewerView.showImage(mediaUrl)
    }

    override fun saveImage(context: Context) {
        LogUtil.d()
        mPicasso
                .load(mediaUrl)
                .placeholder(R.drawable.ic_loading_image_24dp)
                .error(R.drawable.ic_loading_image_24dp)
                .into(getTarget(context, mediaUrl.split("/".toRegex()).last()))
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
    private fun getTarget(context: Context, url: String): Target {
        return object : Target {

            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                val file = File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), url)
                LogUtil.d("filePath = " + file.absolutePath)

                val contentValues: ContentValues = ContentValues()
                contentValues.put(MediaStore.Images.ImageColumns.MIME_TYPE, context.contentResolver.getType(url.toUri()))
                contentValues.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, url)
                contentValues.put(MediaStore.Images.ImageColumns.DATA, file.absolutePath)


                if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, context.contentResolver.openOutputStream(file.toUri()))

                    context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    LogUtil.d("compress run")
                } else {
                    LogUtil.d("compress not run")
                }


//                if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    Thread(Runnable {
//
//
//                        try {
//                            file.createNewFile()
//                            val ostream = FileOutputStream(file)
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream)
//                            ostream.flush()
//                            ostream.close()
//                        } catch (e: IOException) {
//                            errorLog("fail to save", e)
//                        }
//
//                    }).start()
//                }else{
//
//                    debugLog("permission denied")
//                }


            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable) {
                LogUtil.d()
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                LogUtil.d()
            }
        }
    }


}