package com.sunasterisk.thooi.ui.notification.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.data.model.Notification
import com.sunasterisk.thooi.data.source.entity.NotificationType
import com.sunasterisk.thooi.databinding.FragmentNotificationsBinding
import org.koin.android.ext.android.inject

class NotificationsFragment : BaseFragment<FragmentNotificationsBinding>() {

    private val viewModel by inject<NotificationViewModel>()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentNotificationsBinding.inflate(inflater, container, attachToRoot).also {
        it.viewModel = viewModel
        it.lifecycleOwner = viewLifecycleOwner
    }

    override fun setupView() {
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() = with(binding.recyclerNotifications) {
        adapter = NotificationAdapter().also {
            it.onItemClick = { notification, type -> goToNotificationDetail(notification, type) }
        }
    }

    private fun goToNotificationDetail(notification: Notification, type: NotificationType) {
    }
}
