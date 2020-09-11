package com.sunasterisk.thooi.ui.notification.notifications

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.sunasterisk.thooi.base.BaseViewHolder
import com.sunasterisk.thooi.data.model.Notification
import com.sunasterisk.thooi.data.source.entity.NotificationType
import com.sunasterisk.thooi.databinding.ItemNotificationBinding
import com.sunasterisk.thooi.util.inflater

class NotificationAdapter :
    ListAdapter<Notification, NotificationAdapter.NotificationViewHolder>(Notification.diffUtil) {

    var onItemClick: (Notification, NotificationType) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NotificationViewHolder(parent, onItemClick)

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NotificationViewHolder(
        container: ViewGroup,
        val onItemClick: (Notification, NotificationType) -> Unit
    ) :
        BaseViewHolder<Notification, ItemNotificationBinding>(
            ItemNotificationBinding.inflate(container.inflater, container, false)
        ) {

        override fun onBind(item: Notification, binding: ItemNotificationBinding) =
            with(binding) {
                root.setOnClickListener { onItemClick(item, item.type) }
                notification = item
                executePendingBindings()
            }
    }
}
