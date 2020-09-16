package com.sunasterisk.thooi.ui.settings.editprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.transition.Fade
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.Timestamp
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentEditProfileBinding
import com.sunasterisk.thooi.ui.placespicker.AddressBottomSheet
import com.sunasterisk.thooi.ui.placespicker.AddressBottomSheet.Companion.RESULT_PLACES
import com.sunasterisk.thooi.util.beginTransition
import com.sunasterisk.thooi.util.observeEvent
import com.sunasterisk.thooi.util.toLocalDate
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject
import java.util.*

@InternalCoroutinesApi
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>() {

    private val viewModel by inject<EditProfileViewModel>()

    private val picker by lazy {
        MaterialDatePicker.Builder.datePicker().setSelection(System.currentTimeMillis()).build()
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ) = FragmentEditProfileBinding.inflate(inflater).also {
        it.lifecycleOwner = viewLifecycleOwner
        it.viewModel = viewModel
    }

    override fun setupView() {

    }

    override fun initListener() = with(viewModel) {

        fixer.observe(viewLifecycleOwner) {
            binding.run {
                layoutProfile.beginTransition(
                    Fade(Fade.MODE_IN),
                    R.id.inputLayoutDescription,
                    R.id.inputLayoutPassword,
                    R.id.buttonSave
                )
                inputLayoutDescription.isVisible = it
            }
        }

        viewModel.save.observeEvent(viewLifecycleOwner) {
            activity?.onBackPressed()
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
    }
}
