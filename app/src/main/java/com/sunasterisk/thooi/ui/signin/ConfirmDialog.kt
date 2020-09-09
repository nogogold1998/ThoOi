package com.sunasterisk.thooi.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.util.isEmail
import kotlinx.android.synthetic.main.dialog_confirm.*

class ConfirmDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_confirm, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonOk.setOnClickListener {
            textInputEmail.apply {
                val email = textInputEmail.text.toString()
                if (email.isNotEmpty() && email.isEmail) {
                    setFragmentResult(KEY_CONFIRM_RESULT, bundleOf(KEY_RESULT_VALUE to email))
                    this@ConfirmDialog.dismiss()
                } else {
                    error = getString(R.string.msg_email_invalid)
                    requestFocus()
                }
            }
        }
        buttonCancel.setOnClickListener { dismiss() }
    }

    override fun getTheme() = R.style.ThemeOverlay_MaterialComponents_Dialog_Alert

    companion object {
        const val KEY_CONFIRM_RESULT = "confirm_result"
        const val KEY_RESULT_VALUE = "result_value"
    }
}
