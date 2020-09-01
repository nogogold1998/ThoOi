package com.sunasterisk.thooi.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentNotificationBinding

class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentNotificationBinding.inflate(inflater, container, attachToRoot)
}
