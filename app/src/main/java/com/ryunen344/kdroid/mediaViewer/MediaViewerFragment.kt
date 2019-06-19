package com.ryunen344.kdroid.mediaViewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.chrisbanes.photoview.PhotoView
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.R.layout.fragment_media_viewer
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.ensureNotNull
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_media_viewer.*
import kotlinx.android.synthetic.main.fragment_media_viewer.view.*
import org.koin.android.ext.android.inject


class MediaViewerFragment : Fragment(), MediaViewerContract.View {

    val appProvider : AppProvider by inject()
    lateinit var mPresenter : MediaViewerContract.Presenter
    lateinit var mImageViewer : PhotoView
    private var mPicasso : Picasso = appProvider.providePiccaso()

    companion object {
        fun newInstance() = MediaViewerFragment()
    }

    override fun setPresenter(presenter : MediaViewerContract.Presenter) {
        debugLog("start")
        ensureNotNull(presenter) { p ->
            mPresenter = p
        }
        debugLog("end")
    }

    override fun onActivityCreated(savedInstanceState : Bundle?) {
        debugLog("start")
        super.onActivityCreated(savedInstanceState)

        //init save button
        activity?.saveImageButton?.setOnClickListener {
            debugLog("start saveImageButton onClick")
            mPresenter.saveImage(context!!)
            debugLog("end saveImageButton onClick")

        }

        //init close button
        activity?.finishViewerButton?.setOnClickListener {
            activity?.finish()
        }

        debugLog("end")
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        debugLog("start")
        var root : View = inflater.inflate(fragment_media_viewer, container, false)
        with(root) {
            mImageViewer = this.imageViewer
            //mImageViewer.scaleType = ImageView.ScaleType.CENTER_INSIDE
            debugLog("max scale = " + mImageViewer.maximumScale)
            debugLog("min scale = " + mImageViewer.minimumScale)
        }
        //mImageViewer.isZoomable = true

        debugLog("end")
        return root
    }

    override fun onResume() {
        super.onResume()
        mPresenter.start()
    }

    override fun showImage(mediaUrl : String) {
        debugLog("start")

        //load image
        mPicasso
                .load(mediaUrl)
                .placeholder(R.drawable.ic_loading_image_24dp)
                .error(R.drawable.ic_loading_image_24dp)
                .into(mImageViewer)

        debugLog("end")

    }

    override fun showError(e : Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}