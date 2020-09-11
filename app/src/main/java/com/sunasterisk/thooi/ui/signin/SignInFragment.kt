package com.sunasterisk.thooi.ui.signin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentSignInBinding
import com.sunasterisk.thooi.ui.signin.ConfirmDialog.Companion.KEY_CONFIRM_RESULT
import com.sunasterisk.thooi.ui.signup.SignUpFragment.Companion.ACTION_GOOGLE_SIGN_IN
import org.koin.android.ext.android.inject

class SignInFragment : BaseFragment<FragmentSignInBinding>() {

    private val viewModel by inject<SignInViewModel>()
    private val navController by lazy { findNavController() }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ) = FragmentSignInBinding.inflate(inflater).also {
        it.lifecycleOwner = viewLifecycleOwner
        it.viewModel = viewModel
    }

    override fun setupView() {
        viewModel.signIn.observe(viewLifecycleOwner) {
            if (it.getContentIfNotHandled() != null) {
                navController.navigate(R.id.signIn_to_home)
            }
        }
    }

    override fun initListener() {
        binding.textForgotPassword.setOnClickListener {
            ConfirmDialog().show(parentFragmentManager, ConfirmDialog::class.simpleName)
        }
        binding.buttonSignInWithGoogle.setOnClickListener {
            navController.navigate(R.id.signIn_to_signUp, bundleOf(ACTION_GOOGLE_SIGN_IN to true))
        }
        binding.textSignUp.setOnClickListener {
            navController.navigate(R.id.signIn_to_signUp)
        }
        setFragmentResultListener(KEY_CONFIRM_RESULT) { _, bundle ->
            bundle.getString(ConfirmDialog.KEY_RESULT_VALUE)?.let {
                viewModel.forgotPasswordClick(it)
            }
        }
    }
}
