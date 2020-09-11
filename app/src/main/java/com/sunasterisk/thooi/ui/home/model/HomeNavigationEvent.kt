package com.sunasterisk.thooi.ui.home.model

import com.sunasterisk.thooi.util.Event

sealed class HomeNavigationEvent<T>(content: T) : Event<T>(content) {

    class ToPostDetailEvent(postId: String) : HomeNavigationEvent<String>(postId)
}


