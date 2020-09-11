package com.sunasterisk.thooi.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.sunasterisk.thooi.NavGraphDirections
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.base.BaseActivity
import com.sunasterisk.thooi.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val mainVM: MainVM by inject()

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

    override fun onObserveLiveData() {
        mainVM.loginStatus.observe(this) {
            if (it == null) return@observe
            if (it.currentUser == null) navController.navigate(NavGraphDirections.globalSignIn())
        }
    }

    override fun initListeners() {
        with(binding.root) {
            val fullScreenDestinations = setOf(R.id.signInFragment, R.id.signupFragment)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                setTransition(R.id.transition_merge_fab_hide_bottom_nav)
                if (destination.id in fullScreenDestinations) {
                    transitionToEnd()
                } else {
                    transitionToStart()
                }
            }
        }
    }
}
