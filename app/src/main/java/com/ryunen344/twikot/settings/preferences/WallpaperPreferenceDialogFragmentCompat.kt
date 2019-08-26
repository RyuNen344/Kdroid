package com.ryunen344.twikot.settings.preferences

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import androidx.core.net.toUri
import androidx.preference.PreferenceDialogFragmentCompat
import com.ryunen344.twikot.util.LogUtil
import kotlinx.android.synthetic.main.fragment_preference_wallpaper.view.*
import org.koin.android.scope.currentScope


class WallpaperPreferenceDialogFragmentCompat : PreferenceDialogFragmentCompat(), WallpaperPreferenceContract.View {


    override val presenter : WallpaperPreferenceContract.Presenter by currentScope.inject()

    lateinit var button : Button
    lateinit var switch : SwitchCompat
    lateinit var seekBar : SeekBar
    lateinit var image : ImageView

    var uriString : String? = null
    var seekBarValue : Int = 0
    var isPreferenceChanged : Boolean = false
    var cropSwitchState : Boolean = false

    companion object {

        fun newInstance(key : String) : WallpaperPreferenceDialogFragmentCompat {
            LogUtil.d(key)
            val fragment : WallpaperPreferenceDialogFragmentCompat = WallpaperPreferenceDialogFragmentCompat()
            val bundle : Bundle = Bundle(1)
            bundle.putString("key", key)
            fragment.arguments = bundle
            return fragment
        }

        const val REQUEST_IMAGE_GET = 1

        private const val SAVE_STATE_URI = "WallpaperPreferenceDialogFragmentCompat.uri"
        private const val SAVE_STATE_CHANGED = "WallpaperPreferenceDialogFragmentCompat.changed"
        private const val SAVE_STATE_SEEKBAR_VALUE = "WallpaperPreferenceDialogFragmentCompat.seekbarValue"
        private const val SAVE_STATE_CROP_SWITCH = "WallpaperPreferenceDialogFragmentCompat.cropSwitch"
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)
        presenter.view = this
        if (savedInstanceState == null) {
            LogUtil.d()
            presenter.loadSharedPreferences()

        } else {
            uriString = savedInstanceState.getString(SAVE_STATE_URI)
            seekBarValue = savedInstanceState.getInt(SAVE_STATE_SEEKBAR_VALUE)
            isPreferenceChanged = savedInstanceState.getBoolean(SAVE_STATE_CHANGED)
            cropSwitchState = savedInstanceState.getBoolean(SAVE_STATE_CROP_SWITCH)
        }

        presenter.view = this
    }

    override fun onSaveInstanceState(outState : Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVE_STATE_URI, uriString)
        outState.putInt(SAVE_STATE_SEEKBAR_VALUE, seekBarValue)
        outState.putBoolean(SAVE_STATE_CHANGED, isPreferenceChanged)
        outState.putBoolean(SAVE_STATE_CROP_SWITCH, cropSwitchState)
    }

    override fun onCreateDialog(savedInstanceState : Bundle?) : Dialog {
        LogUtil.d()
        val context = activity

        val builder = AlertDialog.Builder(context!!)
                .setTitle(preference.title)
                .setIcon(preference.icon)
                .setPositiveButton("positive", this)
                .setNegativeButton("negative", this)

        val contentView = onCreateDialogView(context)
        if (contentView != null) {
            onBindDialogView(contentView)
            builder.setView(contentView)
        } else {
            builder.setMessage(preference.dialogMessage)
        }

        onPrepareDialogBuilder(builder)

        // Create the dialog
        return builder.create()

    }

    override fun onBindDialogView(view : View?) {
        LogUtil.d()
        super.onBindDialogView(view)

        view?.let {
            button = it.wallpaper_button
            seekBar = it.wallpaper_alpha_seekbar
            switch = it.wallpaper_crop_switch
            image = it.wallpaper_view
        }

        button.setOnClickListener {
            presenter.selectWallpaperImage()
        }

        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0 : SeekBar?, p1 : Int, p2 : Boolean) {
                LogUtil.d()
            }

            override fun onStartTrackingTouch(p0 : SeekBar?) {
                LogUtil.d()
            }

            override fun onStopTrackingTouch(p0 : SeekBar?) {
                LogUtil.d()
            }

        })

        switch.setOnClickListener {
            onClickLog(it)
            cropSwitchState = switch.isChecked
        }

    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        LogUtil.d()
        presenter.start()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStart() {
        LogUtil.d()
        super.onStart()
    }

    override fun onDialogClosed(positiveResult : Boolean) {
        LogUtil.d()
        if (positiveResult) {
            LogUtil.d()
            //presenterで値の永続化
            presenter.saveCurrentPreferences(uriString, seekBarValue, cropSwitchState)

        }
    }

    private fun onClickLog(any : Any) {
        LogUtil.d(any)
    }

    override fun openImagePicker() {
        LogUtil.d()
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(activity?.packageManager!!) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }
    }

    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?) {
        LogUtil.d()
        presenter.result(requestCode, resultCode, data)
    }

    override fun showWallpaperImage(imageUri : Uri) {
        LogUtil.d(imageUri)
        uriString = imageUri.toString()
        image.setImageURI(imageUri)
    }

    override fun setSharedPreferencesParam(uriStr : String?, seekBarValue : Int, cropState : Boolean) {
        LogUtil.d()
        uriString = uriStr
        this.seekBarValue = seekBarValue
        isPreferenceChanged = false
        cropSwitchState = cropState
    }

    override fun initView() {
        LogUtil.d()
        switch.isChecked = cropSwitchState
        seekBar.progress = seekBarValue
        image.setImageURI(uriString?.toUri())
    }

    override fun showError(e : Throwable) {
        LogUtil.e(e)
    }

    override fun onCancel(dialog : DialogInterface) {
        LogUtil.d(dialog)
        super.onCancel(dialog)
    }
}