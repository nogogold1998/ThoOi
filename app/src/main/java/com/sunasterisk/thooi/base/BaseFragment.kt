package com.sunasterisk.thooi.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * base fragment class for using dataBinding or viewBinding
 */
abstract class BaseFragment<out B : ViewBinding> : Fragment(),
    BindingComponent<B>,
    ObservableComponent {

    private var _binding: B? = null

    /**
     * Available after [Fragment.onCreateView] returns
     * and before [Fragment.onDestroyView]'s super is called
     */
    protected val binding: B
        get() = _binding ?: throw IllegalStateException(
            "binding is only valid between onCreateView and onDestroyView"
        )

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = onCreateBinding(inflater, container)
        return binding.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        initListener()
        onObserveLiveData()
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    open fun setupView() = Unit

    open fun initListener() = Unit
}
