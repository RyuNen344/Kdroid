package com.ryunen344.twikot.domain.repository

import android.content.Context
import android.content.SharedPreferences

class WallpaperRepositoryImpl(context : Context) : WallpaperRepository {

    companion object {
        private const val KEY_URI = "WallpaperPreferenceDialogFragmentCompat.uri"
        private const val KEY_SEEKBAR_VALUE = "WallpaperPreferenceDialogFragmentCompat.seekbarValue"
        private const val KEY_CROP_SWITCH = "WallpaperPreferenceDialogFragmentCompat.cropSwitch"
    }

    private val dataStore : SharedPreferences = context.getSharedPreferences("WallpaperDataStore", Context.MODE_PRIVATE)

    override fun setUri(uriStr : String?) {
        uriStr?.let {
            dataStore.edit().putString(KEY_URI, it).apply()
        }

    }

    override fun getUri() : String? {
        return dataStore.getString(KEY_URI, null)
    }

    override fun setSeekBarValue(value : Int?) {
        value?.let {
            dataStore.edit().putInt(KEY_SEEKBAR_VALUE, value).apply()
        }
    }

    override fun getSeekBarValue() : Int {
        return dataStore.getInt(KEY_SEEKBAR_VALUE, 0)
    }

    override fun setCropState(state : Boolean) {
        dataStore.edit().putBoolean(KEY_CROP_SWITCH, state).apply()
    }

    override fun getCropState() : Boolean {
        return dataStore.getBoolean(KEY_CROP_SWITCH, false)
    }
}