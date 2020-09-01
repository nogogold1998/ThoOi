package com.sunasterisk.thooi.ui.notification

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    fm: FragmentManager, lifecycle: Lifecycle,
    private vararg val fragments: Fragment
) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}
