package com.ryunen344.twikot.repository

interface WallpaperRepository {


    fun setUri(uriStr : String?)
    fun getUri() : String?

    fun setSeekBarValue(value : Int?)
    fun getSeekBarValue() : Int

    fun setCropState(state : Boolean)
    fun getCropState() : Boolean
}