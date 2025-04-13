package com.android.newuplift.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.newuplift.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog : BottomSheetDialogFragment() {

    interface LogoutListener {
        fun onLogoutConfirmed()
    }

    private var logoutListener: LogoutListener? = null

    fun setLogoutListener(listener: LogoutListener) {
        this.logoutListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.custom_logout_dialog, container, false)

        view.background = ContextCompat.getDrawable(requireContext(), R.drawable.bottom_sheet_background)

        val confirmButton = view.findViewById<Button>(R.id.btnLogout)
        val cancelButton = view.findViewById<Button>(R.id.btnCancel)

        confirmButton.setOnClickListener {
            logoutListener?.onLogoutConfirmed()
            dismiss()
        }

        cancelButton.setOnClickListener {
            dismiss()
        }

        return view
    }
}
