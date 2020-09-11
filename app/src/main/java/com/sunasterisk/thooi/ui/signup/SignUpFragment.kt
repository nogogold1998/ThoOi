package com.sunasterisk.thooi.ui.signup

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.transition.Fade
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.Timestamp
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentSignUpBinding
import com.sunasterisk.thooi.ui.signup.AddressBottomSheet.Companion.RESULT_PLACES
import com.sunasterisk.thooi.util.beginTransition
import com.sunasterisk.thooi.util.check
import com.sunasterisk.thooi.util.getOneShotResult
import com.sunasterisk.thooi.util.toLocalDate
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.android.ext.android.inject
import java.util.*

class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

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

    private val resultLauncher =
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

    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            @SuppressLint("MissingPermission")
            if (it) {
                AddressBottomSheet().show(
                    parentFragmentManager,
                    AddressBottomSheet::class.simpleName
                )
            } else {
                viewModel.nameRule.value = R.string.msg_missing_permission
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
        if (arguments?.getBoolean(ACTION_GOOGLE_SIGN_IN) == true) {
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
                navController.navigate(R.id.sign_up_to_home)
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
                inputLayoutPassword.isVisible =
                    arguments?.getBoolean(ACTION_GOOGLE_SIGN_IN) != true
            }
        }

        binding.layoutAppbar.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding.editTextAddress.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                AddressBottomSheet().show(
                    parentFragmentManager,
                    AddressBottomSheet::class.simpleName
                )
            }
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
    }

    private fun addChipView(chipText: String) {
        chipGroupCategory?.run {
            val chip = layoutInflater.inflate(R.layout.item_chip_choice, this, false) as Chip
            chip.run {
                text = chipText
            }
            addView(chip)
        }
    }

    companion object {
        const val ACTION_GOOGLE_SIGN_IN = "google_sign_in"
    }
}
