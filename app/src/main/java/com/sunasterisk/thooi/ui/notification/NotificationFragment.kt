package com.sunasterisk.thooi.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentNotificationBinding
import com.sunasterisk.thooi.ui.notification.messages.MessagesFragment
import com.sunasterisk.thooi.ui.notification.notifications.NotificationsFragment

class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentNotificationBinding.inflate(inflater, container, attachToRoot)

    override fun setupView() = with(binding) {
        toolbar.title = getString(R.string.app_name)
        
        viewPagerNotification.adapter = ViewPagerAdapter(
            requireActivity().supportFragmentManager,
            lifecycle,
            NotificationsFragment(),
            MessagesFragment()
        )
        buttonNotification.setOnClickListener {
//            toggleButtonGroup.check(R.id.buttonNotification)
            viewPagerNotification.currentItem = POSITION_TAB_NOTIFICATION
        }
        buttonMessage.setOnClickListener {
//            toggleButtonGroup.check(R.id.buttonMessage)
            viewPagerNotification.currentItem = POSITION_TAB_MESSAGE
        }
    }

    companion object {
        const val POSITION_TAB_NOTIFICATION = 0
        const val POSITION_TAB_MESSAGE = 1
    }
}
