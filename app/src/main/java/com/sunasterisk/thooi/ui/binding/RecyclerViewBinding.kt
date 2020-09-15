package com.sunasterisk.thooi.ui.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sunasterisk.thooi.data.model.Conversation
import com.sunasterisk.thooi.data.model.Message
import com.sunasterisk.thooi.data.model.Notification
import com.sunasterisk.thooi.ui.conversation.MessageAdapter
import com.sunasterisk.thooi.ui.notification.messages.ConversationAdapter
import com.sunasterisk.thooi.ui.notification.notifications.NotificationAdapter
import com.sunasterisk.thooi.ui.post.detail.PostDetailsAdapter
import com.sunasterisk.thooi.ui.post.detail.model.PostDetailsAdapterItem

/**
 * Created by Cong Vu Chi on 06/09/20 23:38.
 */
@BindingAdapter("listPostDetailsAdapterItem")
fun RecyclerView.bindSummaryUser(items: List<PostDetailsAdapterItem<*>>?) {
    (adapter as? PostDetailsAdapter)?.submitList(items)
}

@BindingAdapter("listNotifications")
fun RecyclerView.bindNotification(items: List<Notification>?) {
    (adapter as? NotificationAdapter)?.submitList(items)
}

@BindingAdapter("listConversations")
fun RecyclerView.bindConversation(items: List<Conversation>?) {
    (adapter as? ConversationAdapter)?.submitList(items)
}

@BindingAdapter("listMessages")
fun RecyclerView.bindMessages(items: List<Message>?) {
    (adapter as? MessageAdapter)?.submitList(items)
}
