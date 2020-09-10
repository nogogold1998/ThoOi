package com.sunasterisk.thooi.base

/**
 * Created by Cong Vu Chi on 10/09/20 08:46.
 */
abstract class BaseAdapterItem<T : Any> {
    abstract val data: T
    abstract fun isTheSame(other: BaseAdapterItem<*>): Boolean
    abstract fun isContentTheSame(other: BaseAdapterItem<*>): Boolean
}
