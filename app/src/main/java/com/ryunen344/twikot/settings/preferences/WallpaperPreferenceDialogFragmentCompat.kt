package com.ryunen344.twikot.settings.preferences

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Switch
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceDialogFragmentCompat
import com.ryunen344.twikot.util.LogUtil
import kotlinx.android.synthetic.main.fragment_preference_wallpaper.view.*
import org.koin.android.scope.currentScope


class WallpaperPreferenceDialogFragmentCompat : PreferenceDialogFragmentCompat(), WallpaperPreferenceContract.View {

    override val presenter : WallpaperPreferenceContract.Presenter by currentScope.inject()

    lateinit var button : Button
    lateinit var switch : Switch
    lateinit var seekBar : SeekBar

    companion object {

        fun newInstance(key : String) : WallpaperPreferenceDialogFragmentCompat {
            LogUtil.d(key)
            val fragment : WallpaperPreferenceDialogFragmentCompat = WallpaperPreferenceDialogFragmentCompat()
            val bundle : Bundle = Bundle(1)
            bundle.putString("key", key)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState : Bundle?) : Dialog {
        LogUtil.d()
        val context = activity

        val builder = AlertDialog.Builder(context!!)
                .setTitle(preference.title)
                .setIcon(preference.icon)

        val contentView = onCreateDialogView(context)
        if (contentView != null) {
            onBindDialogView(contentView)
            builder.setView(contentView)
        } else {
            builder.setMessage(preference.dialogMessage)
        }

        onPrepareDialogBuilder(builder)

        // Create the dialog
        val dialog = builder.create()
        if (needInputMethod()) {
            LogUtil.d()
            requestInputMethod(dialog)
        }

        return dialog
    }

    /** @hide
     */
    override fun needInputMethod() : Boolean {
        // We want the input method to show, if possible, when dialog is displayed
        return true
    }

    private fun requestInputMethod(dialog : Dialog) {
        val window = dialog.window
        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)
        presenter.view = this
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        LogUtil.d()
        //val root = inflater.inflate(R.layout.fragment_preference_wallpaper, container, false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onBindDialogView(view : View?) {
        LogUtil.d()
        super.onBindDialogView(view)

        view?.let {
            button = it.wallpaper_button
            seekBar = it.alpha_seekbar
            switch = it.crop_switch
        }

        button.setOnClickListener {
            onClickLog(it)
            presenter.doSomething()
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
        }

    }

    override fun onDialogClosed(positiveResult : Boolean) {
        LogUtil.d()
    }

    private fun onClickLog(any : Any) {
        LogUtil.d(any)
    }

    override fun doSomething() {
        LogUtil.d()
    }

    override fun showError(e : Throwable) {
        LogUtil.e(e)
    }

}