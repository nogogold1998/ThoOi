package com.sunasterisk.thooi.util

import com.sunasterisk.thooi.data.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T : Any, R : Any> Flow<Result<T>>.mapResult(function: (T) -> R) = map { result ->
    when (result) {
        is Result.Success -> Result.success(
            function(result.data)
        )
        is Result.Failed -> result
        is Result.Loading -> result
    }
}

fun <T, R> Flow<Result<List<T>>>.mapResultList(function: (T) -> R) = map { result ->
    when (result) {
        is Result.Success -> Result.success(
            result.data.map { function(it) }
        )
        is Result.Failed -> result
        is Result.Loading -> result
    }
}
