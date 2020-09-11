package com.sunasterisk.thooi.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.base.BaseActivity
import com.sunasterisk.thooi.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val navController by lazy { findNavController(R.id.navHostFragment) }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ) = ActivityMainBinding.inflate(inflater)

    override fun setupViews() {
        setupBottomNavView()
    }

    private fun setupBottomNavView() = binding.bottomNavView.let {
        NavigationUI.setupWithNavController(it, navController)
        it.setOnNavigationItemSelectedListener { item ->
            if (navController.currentDestination?.id != item.itemId) {
                NavigationUI.onNavDestinationSelected(item, navController)
            } else {
                true
            }
        }
    }
}
