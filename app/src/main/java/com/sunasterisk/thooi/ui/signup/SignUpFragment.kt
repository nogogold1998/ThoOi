package com.sunasterisk.thooi.ui.signup

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.Fade
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.Timestamp
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentSignUpBinding
import com.sunasterisk.thooi.ui.placespicker.AddressBottomSheet
import com.sunasterisk.thooi.ui.placespicker.AddressBottomSheet.Companion.RESULT_PLACES
import com.sunasterisk.thooi.util.beginTransition
import com.sunasterisk.thooi.util.check
import com.sunasterisk.thooi.util.getOneShotResult
import com.sunasterisk.thooi.util.toLocalDate
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.android.ext.android.inject
import java.util.*

@InternalCoroutinesApi
class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

    private val navArgs: SignUpFragmentArgs by navArgs()

    private val viewModel by inject<SignUpViewModel>()
    private val navController by lazy { findNavController() }

    private val googleSignInClient by lazy {
        GoogleSignIn.getClient(
            requireContext(), GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )
    }

    private val resultLauncher: ActivityResultLauncher<Intent> by lazy {
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                lifecycleScope.launch {
                    getOneShotResult { GoogleSignIn.getSignedInAccountFromIntent(it.data).await() }
                        .check({ account ->
                            viewModel.signInWithGoogleClick(account)
                        }, {
                            activity?.onBackPressed()
                        })
                }
            } else {
                activity?.onBackPressed()
            }
        }
    }

    private val picker by lazy {
        MaterialDatePicker.Builder.datePicker().setSelection(System.currentTimeMillis()).build()
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ) = FragmentSignUpBinding.inflate(inflater).also {
        it.lifecycleOwner = viewLifecycleOwner
        it.viewModel = viewModel
    }

    override fun setupView() {
        if (navArgs.isGoogleSignUp) {
            resultLauncher.launch(googleSignInClient.signInIntent)
        }
    }

    override fun initListener() = with(viewModel) {
        signUp.observe(viewLifecycleOwner) {
            if (it.getContentIfNotHandled() != null) {
                activity?.onBackPressed()
            }
        }

        googleSignIn.observe(viewLifecycleOwner) {
            if (it.getContentIfNotHandled() != null) {
                navController.navigate(SignUpFragmentDirections.signUpToHome())
            }
        }

        fixer.observe(viewLifecycleOwner) {
            binding.run {
                layoutProfile.beginTransition(
                    Fade(Fade.MODE_IN),
                    R.id.textLabelCategory,
                    R.id.chipGroupCategory,
                    R.id.inputLayoutDescription,
                    R.id.inputLayoutPassword,
                    R.id.buttonSignUp
                )
                textLabelCategory.isVisible = it
                chipGroupCategory.isVisible = it
                inputLayoutDescription.isVisible = it
                inputLayoutPassword.isVisible = !navArgs.isGoogleSignUp
            }
        }

        binding.layoutAppbar.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding.editTextAddress.setOnClickListener {
            AddressBottomSheet().show(
                parentFragmentManager,
                AddressBottomSheet::class.simpleName
            )
        }

        binding.editTextBirthday.setOnClickListener {
            picker.show(parentFragmentManager, picker::class.simpleName)
        }

        picker.addOnPositiveButtonClickListener {
            birthday.value = picker.headerText to Timestamp(Date(it)).toLocalDate()
        }

        setFragmentResultListener(RESULT_PLACES) { _, bundle ->
            address.value = bundle.getParcelable(RESULT_PLACES)
        }

        category.observe(viewLifecycleOwner) { list ->
            binding.chipGroupCategory.removeAllViews()
            list.forEach { addChipView(it.title) }
        }

        binding.chipGroupCategory.setOnCheckedChangeListener { group, checkedId ->
            group.forEach { if (it is Chip && it.id == checkedId) viewModel.category }
        }
    }

    private fun addChipView(chipText: String) {
        binding.chipGroupCategory.run {
            val chip = layoutInflater.inflate(R.layout.item_chip_choice, this, false) as Chip
            chip.run {
                text = chipText
            }
            addView(chip)
        }
    }
}
