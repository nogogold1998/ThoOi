package com.sunasterisk.thooi.util

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.sunasterisk.thooi.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlin.reflect.KClass
import kotlin.reflect.jvm.javaType

const val PARAM_ID = "id"
const val MSG_UNKNOWN_ERROR = "Unknown error"
const val PARAMS_NUM = 2

fun <T : Any> DocumentSnapshot.toObjectWithId(
    firebaseClass: Class<*>,
    dataClass: KClass<T>,
): T? {
    val kFunctions = dataClass.constructors
    if (kFunctions.size >= PARAMS_NUM) {
        kFunctions.first().let {
            it.parameters.run {
                if (size == PARAMS_NUM) {
                    val obj = toObject(firebaseClass)
                    if (first().type.javaType == String::class.java
                        && last().type.javaType == firebaseClass
                        && first().name == PARAM_ID
                        && obj != null
                    ) return it.call(id, obj)
                }
            }
        }
    }
    return null
}

@ExperimentalCoroutinesApi
fun <T : Any> DocumentReference.getSnapshotFlow(converter: (value: DocumentSnapshot) -> T?) =
    callbackFlow {
        val listener = addSnapshotListener { value, error ->
            value?.run {
                converter(value)?.let {
                    offer(Result.success(it))
                } ?: Result.failed(Throwable(MSG_UNKNOWN_ERROR))
            }
            error?.run { offer(Result.failed(this)) }
        }
        awaitClose {
            listener.remove()
            cancel()
        }
    }.onStart {
        emit(Result.loading())
    }.flowOn(Dispatchers.IO)

suspend fun <T : Any> getOneShotResult(function: suspend () -> T): Result<T> = try {
    Result.success(function())
} catch (e: Exception) {
    Result.failed(e)
}

fun <T : Any> Result<T>.check(
    success: ((success: T) -> Unit)? = null,
    failed: ((exception: Throwable) -> Unit)? = null,
    loading: ((isLoading: Boolean) -> Unit)? = null,
) {
    when (this) {
        is Result.Success -> {
            success?.invoke(data)
            loading?.invoke(false)
        }
        is Result.Failed -> {
            failed?.invoke(cause)
            loading?.invoke(false)
        }
        is Result.Loading -> loading?.invoke(true)
    }
}

fun <T : Any, R : Any> Flow<Result<List<T>>>.mapResult(function: (T) -> R) = map { value ->
    when (value) {
        is Result.Success -> Result.success(
            value.data.map { function(it) }
        )
        is Result.Failed -> value
        is Result.Loading -> value
    }
}
