package com.sunasterisk.thooi.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<out B : ViewBinding> : AppCompatActivity(),
    BindingComponent<B>,
    ObservableComponent {

    private lateinit var _binding: B
    protected val binding: B get() = _binding

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = onCreateBinding(layoutInflater, null)
        setContentView(binding.root)
        setupViews()
        initListeners()
    }

    open fun setupViews() = Unit

    open fun initListeners() = Unit
}
