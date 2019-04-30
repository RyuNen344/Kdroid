package com.ryunen344.kdroid.mediaViewer

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.R.layout.fragment_media_viewer
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.ensureNotNull
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_media_viewer.*
import kotlinx.android.synthetic.main.fragment_media_viewer.view.*

class MediaViewerFragment : Fragment(), MediaViewerContract.View {

    lateinit var mPresenter: MediaViewerContract.Presenter
    lateinit var mImageViewer: ImageView
    private var mPicasso: Picasso = Picasso.get()

    private lateinit var mScaleGestureDetector: ScaleGestureDetector
    private lateinit var mPanGestureDetector: GestureDetectorCompat

    private var mScaleFactor = 1.0f
    private var mTranslationX = 0f
    private var mTranslationY = 0f
    private var mImageWidth = 0f
    private var mImageHeight = 0f
    private var mDefaultImageWidth = 0f
    private var mDefaultImageHeight = 0f
    private var mViewPortWidth = 0f
    private var mViewPortHeight = 0f

    companion object {
        fun newInstance() = MediaViewerFragment()
    }

    override fun setPresenter(presenter: MediaViewerContract.Presenter) {
        debugLog("start")
        ensureNotNull(presenter) { p ->
            mPresenter = p
        }
        debugLog("end")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        debugLog("start")
        super.onActivityCreated(savedInstanceState)
        //init picasso instance
        mPicasso.setIndicatorsEnabled(true)
        mPicasso.isLoggingEnabled = true

        //init save button
        activity?.saveImageButton?.setOnClickListener {
            debugLog("start saveImageButton onClick")
            mPresenter.saveImage()
            debugLog("end saveImageButton onClick")

        }

        //init close button
        activity?.finishViewerButton?.setOnClickListener {
            activity?.finish()
        }

        debugLog("end")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        debugLog("start")
        var root: View = inflater.inflate(fragment_media_viewer, container, false)
        with(root) {
            mImageViewer = imageViewer
        }

        mImageViewer.setOnTouchListener { v, event ->
            onTouchEvent(event)
        }

        mScaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
        mPanGestureDetector = GestureDetectorCompat(context, PanListener())

        val viewTreeObserver = mImageViewer.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    mImageViewer.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    val imageAspectRatio = mImageViewer.drawable.intrinsicHeight.toFloat() / mImageViewer.drawable.intrinsicWidth.toFloat()
                    val viewAspectRatio = mImageViewer.height.toFloat() / mImageViewer.width.toFloat()

                    mImageWidth = if (imageAspectRatio < viewAspectRatio) {
                        // landscape image
                        mImageViewer.width.toFloat()
                    } else {
                        // Portrait image
                        mImageViewer.height.toFloat() / imageAspectRatio
                    }

                    mImageHeight = if (imageAspectRatio < viewAspectRatio) {
                        // landscape image
                        mImageViewer.width.toFloat() * imageAspectRatio
                    } else {
                        // Portrait image
                        mImageViewer.height.toFloat()
                    }

                    mDefaultImageWidth = mImageWidth
                    mDefaultImageHeight = mImageHeight

                    mViewPortWidth = mImageViewer.width.toFloat()
                    mViewPortHeight = mImageViewer.height.toFloat()
                }
            })
        }

        debugLog("end")
        return root
    }

    override fun onResume() {
        super.onResume()
        mPresenter.start()
    }

    override fun showImage(mediaUrl: String) {
        debugLog("start")

        //load image
        mPicasso
                .load(mediaUrl)
                .placeholder(R.drawable.ic_loading_image_24dp)
                .error(R.drawable.ic_loading_image_24dp)
                .into(mImageViewer)

        debugLog("end")

    }


    override fun showError(e: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun onTouchEvent(event: MotionEvent): Boolean {
        mScaleGestureDetector.onTouchEvent(event)
        mPanGestureDetector.onTouchEvent(event)

        return true
    }

    private inner class PanListener : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            debugLog("start")
            val translationX = mTranslationX - distanceX
            val translationY = mTranslationY - distanceY

            adjustTranslation(translationX, translationY)
            debugLog("end")

            return true
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            debugLog("start")
            mScaleFactor *= mScaleGestureDetector.scaleFactor
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 5.0f))
            mImageViewer.scaleX = mScaleFactor
            mImageViewer.scaleY = mScaleFactor
            mImageWidth = mDefaultImageWidth * mScaleFactor
            mImageHeight = mDefaultImageHeight * mScaleFactor

            adjustTranslation(mTranslationX, mTranslationY)

            debugLog("end")

            return true
        }
    }

    private fun adjustTranslation(translationX: Float, translationY: Float) {
        debugLog("start")
        val translationXMargin = Math.abs((mImageWidth - mViewPortWidth) / 2)
        val translationYMargin = Math.abs((mImageHeight - mViewPortHeight) / 2)

        if (translationX < 0) {
            mTranslationX = Math.max(translationX, -translationXMargin)
        } else {
            mTranslationX = Math.min(translationX, translationXMargin)
        }

        if (mTranslationY < 0) {
            mTranslationY = Math.max(translationY, -translationYMargin)
        } else {
            mTranslationY = Math.min(translationY, translationYMargin)
        }

        mImageViewer.translationX = mTranslationX
        mImageViewer.translationY = mTranslationY
        debugLog("end")
    }

}