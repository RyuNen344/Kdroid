package com.ryunen344.twikot

import java.io.IOException

enum class Status {
    NOPE,
    SUCCESS,
    ERROR,
    LOADING
}

data class FailureStatus(val message : String? = null, val throwable : Throwable? = null) {
    constructor(message : String?) : this(message = message, throwable = null)
    constructor(t : Throwable?) : this(message = null, throwable = t)

    fun createMessage(throwableFunc : (throwable : Throwable?) -> String) : String =
            if (!message.isNullOrEmpty()) {
                message
            } else {
                throwableFunc(throwable)
            }
}

sealed class IOState(val status : Status) {
    object NOPE : IOState(status = Status.NOPE)
    object LOADED : IOState(Status.SUCCESS)
    object LOADING : IOState(Status.LOADING)
    data class ERROR(val error : FailureStatus) : IOState(Status.ERROR) {
        constructor(throwable : Throwable?) : this(FailureStatus(throwable))
        constructor(message : String?) : this(FailureStatus(message))
    }
}

fun errorMessage(throwable : Throwable?) : String = when (throwable) {
    is RuntimeException -> "This is RuntimeException"
    is IOException -> "IO Error"
    else -> throwable.toString()
}