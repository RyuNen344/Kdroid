package com.ryunen344.twikot.settings.preferences

import android.content.Context
import android.content.res.TypedArray
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.Checkable
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.DialogPreference
import androidx.preference.PreferenceViewHolder
import com.ryunen344.twikot.R
import com.ryunen344.twikot.util.LogUtil

class WallpaperPreference : DialogPreference {
    internal var mSwitchView : View? = null
    internal var mChecked : Boolean = false
    internal var mCheckedSet : Boolean = false

    internal var mSeekBarValue : Int = 0
    internal var mSeekBar : SeekBar? = null
    internal var mMin : Int = 0
    private var mMax : Int = 0

    private var mEntries : Array<CharSequence>? = null
    private var mEntryValues : Array<CharSequence>? = null
    private var mValue : String? = null
    private var mSummary : String? = null
    private var mValueSet : Boolean = false


    private val mSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar : SeekBar, progress : Int, fromUser : Boolean) {
//            if (fromUser && (mUpdatesContinuously || !mTrackingTouch)) {
//                syncValueInternal(seekBar)
//            } else {
//                // We always want to update the text while the seekbar is being dragged
//                updateLabelValue(progress + mMin)
//            }
            LogUtil.d()
        }

        override fun onStartTrackingTouch(seekBar : SeekBar) {
//            mTrackingTouch = true
            LogUtil.d()
        }

        override fun onStopTrackingTouch(seekBar : SeekBar) {
//            mTrackingTouch = false
//            if (seekBar.progress + mMin != mSeekBarValue) {
//                syncValueInternal(seekBar)
//            }
            LogUtil.d()
        }
    }

    /**
     * Listener reacting to the user pressing DPAD left/right keys if `adjustable` attribute is set to true; it transfers the key presses to the [SeekBar]
     * to be handled accordingly.
     */
    private val mSeekBarKeyListener = View.OnKeyListener { v, keyCode, event ->
        if (event.action != KeyEvent.ACTION_DOWN) {
            return@OnKeyListener false
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            // Right or left keys are pressed when in non-adjustable mode; Skip the keys.
            return@OnKeyListener false
        }

        // We don't want to propagate the click keys down to the SeekBar view since it will
        // create the ripple effect for the thumb.
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
            return@OnKeyListener false
        }

        if (mSeekBar == null) {
            LogUtil.e("SeekBar view is null and hence cannot be adjusted.")
            return@OnKeyListener false
        }
        mSeekBar!!.onKeyDown(keyCode, event)
    }

    private val mSwitchChangedListener = object : CompoundButton.OnCheckedChangeListener {

        override fun onCheckedChanged(buttonView : CompoundButton, isChecked : Boolean) {
            if (!callChangeListener(isChecked)) {
                // Listener didn't like it, change it back.
                // CompoundButton will make sure we don't recurse.
                buttonView.isChecked = !isChecked
                return
            }

            setChecked(isChecked)
        }
    }

    constructor(context : Context, attrs : AttributeSet?, defStyleAttr : Int, defStyleRes : Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        LogUtil.d()

        var a : TypedArray = context.obtainStyledAttributes(attrs, R.styleable.WallpaperPreference, defStyleAttr, defStyleRes)

        mMin = a.getInt(R.styleable.WallpaperPreference_min, 0)
        mMax = a.getInt(R.styleable.WallpaperPreference_android_max, 0)


        mEntries = TypedArrayUtils.getTextArray(a, R.styleable.WallpaperPreference_android_entries, R.styleable.WallpaperPreference_android_entries)
        mEntryValues = TypedArrayUtils.getTextArray(a, R.styleable.WallpaperPreference_entryValues, R.styleable.WallpaperPreference_android_entryValues)
        a.recycle()

        a = context.obtainStyledAttributes(attrs, R.styleable.Preference, defStyleAttr, defStyleRes)
        a.recycle()

    }

    constructor(context : Context, attrs : AttributeSet?, defStyleAttr : Int) : this(context, attrs, defStyleAttr, 0) {
        LogUtil.d()
    }

    constructor(context : Context, attrs : AttributeSet?) :
            this(context, attrs, TypedArrayUtils.getAttr(context, R.attr.dialogPreferenceStyle,
                    android.R.attr.dialogPreferenceStyle)) {
        LogUtil.d()
        dialogLayoutResource = R.layout.fragment_preference_wallpaper
    }

    constructor(context : Context) : this(context, null) {
        LogUtil.d()
    }

    override fun onBindViewHolder(holder : PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        holder?.let {
            it.itemView.setOnKeyListener(mSeekBarKeyListener)
            mSeekBar = it.findViewById(R.id.wallpaper_alpha_seekbar) as SeekBar?
            mSwitchView = it.findViewById(R.id.wallpaper_crop_switch)
            mSwitchView?.let { v ->
                syncSwitchView(v)
            }
        }
        mSeekBar ?: return
        mSeekBar?.setOnSeekBarChangeListener(mSeekBarChangeListener)
        mSeekBar?.max = mMax - mMin


        mSeekBar?.progress = mSeekBarValue - mMin

    }

    override fun onGetDefaultValue(a : TypedArray?, index : Int) : Any {
        return a?.getString(index)!!
    }

    override fun onSaveInstanceState() : Parcelable {
        val superState = super.onSaveInstanceState()
        if (isPersistent) {
            // No need to save instance state since it's persistent
            return superState
        }

        // Save the instance state
        val myState = SavedState(superState)
        myState.mSeekBarValue = mSeekBarValue
        myState.mMin = mMin
        myState.mMax = mMax
        return myState
    }

    override fun onRestoreInstanceState(state : Parcelable?) {
        if (state == null || state.javaClass != SavedState::class.java) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state)
            return
        }

        // Restore the instance state
        val myState = state as SavedState
        super.onRestoreInstanceState(myState.superState)
        mSeekBarValue = myState.mSeekBarValue!!
        mMin = myState.mMin!!
        mMax = myState.mMax!!
        notifyChanged()
    }

    inner class SavedState : BaseSavedState {

        internal var mUri : String? = null
        internal var mSeekBarValue : Int? = null
        internal var mMin : Int? = null
        internal var mMax : Int? = null
        internal var mChecked : Boolean = false

        internal constructor(source : Parcel) : super(source) {
            mUri = source.readString()
            mSeekBarValue = source.readInt()
            mMin = source.readInt()
            mMax = source.readInt()
            mChecked = source.readInt() == 1
        }

        internal constructor(superState : Parcelable) : super(superState)

        override fun writeToParcel(dest : Parcel, flags : Int) {
            super.writeToParcel(dest, flags)
            dest.writeString(mUri)
            dest.writeInt(mSeekBarValue!!)
            dest.writeInt(mMin!!)
            dest.writeInt(mMax!!)
            dest.writeInt(if (mChecked) 1 else 0)
        }
    }

    fun setChecked(checked : Boolean) {
        // Always persist/notify the first time; don't assume the field's default of false.
        val changed = mChecked != checked
        if (changed || !mCheckedSet) {
            mChecked = checked
            mCheckedSet = true
            persistBoolean(checked)
            if (changed) {
                notifyDependencyChange(shouldDisableDependents())
                notifyChanged()
            }
        }
    }

    private fun syncSwitchView(view : View) {
        if (view is SwitchCompat) {
            view.setOnCheckedChangeListener(null)
        }
        if (view is Checkable) {
            (view as Checkable).isChecked = mChecked
        }
        if (view is SwitchCompat) {
            view.setOnCheckedChangeListener(mSwitchChangedListener)
        }
    }


}