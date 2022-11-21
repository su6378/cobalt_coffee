package com.ssafy.cobaltcoffee.util

interface DatabaseCallback<T> {
    fun onError(t: Throwable)

    fun onSuccess()

    fun onFailure()
}