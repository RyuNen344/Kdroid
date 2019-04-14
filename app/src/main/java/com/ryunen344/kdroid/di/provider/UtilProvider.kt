package com.ryunen344.kdroid.di.provider

import android.icu.text.SimpleDateFormat

/**
 * this provider provides instance what is needed to create instance
 */
class UtilProvider {

    lateinit var sdf: SimpleDateFormat

    /**
     * this method has to call after config twitter instance
     */
    fun provideSdf(): SimpleDateFormat {
        return SimpleDateFormat("MM/dd HH:mm:ss")
    }
}