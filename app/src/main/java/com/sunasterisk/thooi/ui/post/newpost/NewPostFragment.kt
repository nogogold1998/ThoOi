package com.sunasterisk.thooi.ui.post.newpost

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.firebase.Timestamp
import com.sunasterisk.thooi.NavGraphDirections
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentNewPostBinding
import com.sunasterisk.thooi.ui.placespicker.AddressBottomSheet
import com.sunasterisk.thooi.ui.placespicker.AddressBottomSheet.Companion.RESULT_PLACES
import com.sunasterisk.thooi.ui.post.newpost.CategoryBottomSheet.Companion.RESULT_CATEGORY
import com.sunasterisk.thooi.util.observeEvent
import com.sunasterisk.thooi.util.toLocalDate
import com.sunasterisk.thooi.util.toast
import org.koin.android.ext.android.inject
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class NewPostFragment : BaseFragment<FragmentNewPostBinding>() {

    private val viewModel by inject<NewPostViewModel>()
    private val adapter by lazy { ImageAdapter() }
    private lateinit var currentPhotoPath: String

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) viewModel.addImage(currentPhotoPath)
    }

    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.filterValues { b -> b == false }.isEmpty()) {
                takePhoto()
            } else {
                context?.toast(R.string.msg_permission_denied)
            }
        }

    private val datePicker by lazy {
        MaterialDatePicker.Builder.datePicker().setSelection(System.currentTimeMillis()).build()
    }

    private val timePicker by lazy { MaterialTimePicker.newInstance() }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentNewPostBinding.inflate(inflater).also {
        it.lifecycleOwner = viewLifecycleOwner
        it.viewModel = viewModel
        it.adapter = adapter
    }

    override fun setupView() = with(viewModel) {
        imageUri.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        error.observe(viewLifecycleOwner) {
            context?.toast(it.message.toString())
        }
        done.observeEvent(viewLifecycleOwner) {
            findNavController().navigate(NavGraphDirections.globalHome())
        }
    }

    override fun initListener() {
        binding.buttonCategory.setOnClickListener {
            CategoryBottomSheet().show(parentFragmentManager, CategoryBottomSheet::class.simpleName)
        }
        binding.buttonWorkTime.setOnClickListener {
            datePicker.show(parentFragmentManager, datePicker::class.simpleName)
        }
        binding.buttonAddress.setOnClickListener {
            AddressBottomSheet().show(parentFragmentManager, AddressBottomSheet::class.simpleName)
        }
        binding.buttonAddImage.setOnClickListener {
            if (checkSelfPermission(requireContext(), WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED
                || checkSelfPermission(requireContext(), CAMERA) == PERMISSION_GRANTED
            ) {
                takePhoto()
            } else {
                permissionResult.launch(
                    arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE)
                )
            }
        }
        adapter.setOnclickListener { position ->
            try {
                val file = File(currentPhotoPath)
                if (file.exists()) file.delete()
            } catch (e: IOException) {
                context?.toast(e.message.toString())
            }
            viewModel.removeImage(position)
            adapter.notifyItemRemoved(position)
        }
        timePicker.setListener {
            viewModel.onTimePick(LocalTime.of(it.hour, it.minute))
        }
        datePicker.addOnPositiveButtonClickListener {
            timePicker.show(parentFragmentManager, timePicker::class.simpleName)
            viewModel.onDatePick(
                Triple(
                    datePicker.headerText,
                    Timestamp(Date(it)).toLocalDate(),
                    LocalDateTime.now()
                )
            )
        }
        setFragmentResultListener(RESULT_PLACES) { _, bundle ->
            viewModel.places.value = bundle.getParcelable(RESULT_PLACES)
        }
        setFragmentResultListener(RESULT_CATEGORY) { _, bundle ->
            viewModel.category.value = bundle.getParcelable(RESULT_CATEGORY)
        }
    }

    private fun takePhoto() {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            null
        }
        photoFile?.also {
            val photoURI: Uri = FileProvider.getUriForFile(requireContext(), AUTHORITY, it)
            takePicture.launch(photoURI)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat(FORMAT_PATTERN, Locale.getDefault()).format(Date())
        val storageDir: File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply { currentPhotoPath = path }
    }

    companion object {
        const val FORMAT_PATTERN = "yyyyMMdd_HHmmss"
        const val AUTHORITY = "com.sunasterisk.thooi.fileprovider"
    }
}
