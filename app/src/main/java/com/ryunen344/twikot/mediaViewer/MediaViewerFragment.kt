package com.ryunen344.twikot.mediaViewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.chrisbanes.photoview.PhotoView
import com.ryunen344.twikot.R
import com.ryunen344.twikot.R.layout.fragment_media_viewer
import com.ryunen344.twikot.di.provider.AppProvider
import com.ryunen344.twikot.util.LogUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_media_viewer.*
import kotlinx.android.synthetic.main.fragment_media_viewer.view.*
import org.koin.android.ext.android.inject
import org.koin.android.scope.currentScope


class MediaViewerFragment : Fragment(), MediaViewerContract.View {


    val appProvider : AppProvider by inject()
    lateinit var mImageViewer : PhotoView
    private var mPicasso : Picasso = appProvider.providePiccaso()

    override val presenter : MediaViewerContract.Presenter by currentScope.inject()

    companion object {
        fun newInstance() = MediaViewerFragment()
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)

        currentScope.getKoin().setProperty(MediaViewerActivity.INTENT_KEY_MEDIA_URL, arguments!!.getString(MediaViewerActivity.INTENT_KEY_MEDIA_URL, ""))
        presenter.view = this
    }


    override fun onActivityCreated(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onActivityCreated(savedInstanceState)

        //init save button
        activity?.saveImageButton?.setOnClickListener {
            LogUtil.d()
            presenter.saveImage(context!!)

        }

        //init close button
        activity?.finishViewerButton?.setOnClickListener {
            activity?.finish()
        }
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        LogUtil.d()
        var root : View = inflater.inflate(fragment_media_viewer, container, false)
        with(root) {
            mImageViewer = this.imageViewer
            //mImageViewer.scaleType = ImageView.ScaleType.CENTER_INSIDE
            LogUtil.d("max scale = " + mImageViewer.maximumScale)
            LogUtil.d("min scale = " + mImageViewer.minimumScale)
        }
        //mImageViewer.isZoomable = true
        return root
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun showImage(mediaUrl : String) {
        LogUtil.d()

        //load image
        mPicasso
                .load(mediaUrl)
                .placeholder(R.drawable.ic_loading_image_24dp)
                .error(R.drawable.ic_loading_image_24dp)
                .into(mImageViewer)

    }

    override fun showError(e : Throwable) {
        LogUtil.e(e)
    }

}