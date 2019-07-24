package com.ryunen344.kdroid.util

import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class ParameterConverter : Converter.Factory(){
    override fun stringConverter(type : Type, annotations : Array<Annotation>, retrofit : Retrofit) : Converter<*, String>? =
            Converter<Any, String> { it.toString() }
}