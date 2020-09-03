package com.sunasterisk.thooi.data

sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Failed(val cause: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()

    companion object {
        fun <T : Any> success(data: T) = Success(data)
        fun failed(cause: Throwable) = Failed(cause)
        fun loading() = Loading
    }
}
