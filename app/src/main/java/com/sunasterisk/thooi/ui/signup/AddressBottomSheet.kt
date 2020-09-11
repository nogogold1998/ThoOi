package com.sunasterisk.thooi.ui.signup

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.databinding.BottomSheetListBinding
import com.sunasterisk.thooi.util.check
import com.sunasterisk.thooi.util.getOneShotResult
import com.sunasterisk.thooi.util.toast
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.android.ext.android.inject

class AddressBottomSheet : BottomSheetDialogFragment() {

    private val viewModel by inject<SignUpViewModel>()
    private val adapter by lazy { AddressAdapter() }
    private lateinit var binding: BottomSheetListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetListBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.adapter = adapter
        }
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.run {
            lifecycleScope.launch {
                checkLocationSetting().check({
                    if (it) findAddress()
                }, {
                    context?.toast(R.string.msg_missing_location_setting)
                    dismiss()
                })
            }
            places.observe(viewLifecycleOwner) {
                adapter.submitList(it)
                binding.progressBar.isVisible = false
            }
            adapter.setOnclickListener {
                setFragmentResult(RESULT_PLACES, bundleOf(RESULT_PLACES to it))
                dismiss()
            }
        }
    }

    private suspend fun checkLocationSetting() = getOneShotResult {
        val request = LocationSettingsRequest.Builder()
            .addLocationRequest(LocationRequest())
            .build()
        LocationServices
            .getSettingsClient(requireActivity())
            .checkLocationSettings(request)
            .await()
            .locationSettingsStates
            .isLocationPresent
    }

    companion object {
        const val RESULT_PLACES = "result_places"
    }
}
