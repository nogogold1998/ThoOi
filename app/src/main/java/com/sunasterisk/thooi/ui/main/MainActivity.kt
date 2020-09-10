package com.sunasterisk.thooi.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
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
        binding.bottomNavView.setupWithNavController(navController)
    }
}
