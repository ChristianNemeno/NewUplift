package com.android.newuplift.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.android.newuplift.R
import com.android.newuplift.activities.LoginActivity
import com.android.newuplift.activities.LogoutActivity
import com.android.newuplift.dialogfragment.BottomSheetDialog
import com.android.newuplift.utility.AuthManager

class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favButton = view.findViewById<ImageButton>(R.id.settings_favorite_button)

        val logoutButton = view.findViewById<ImageView>(R.id.logout_button)



        logoutButton.setOnClickListener {


            // i want to use the custom dialog here how?
//            showLogoutDialog()
            showLogoutBottomSheet()

        }

        favButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_favoriteQuotesFragment)
        }
    }

//    private fun showLogoutDialog() {
//        context?.let { ctx ->
//            val dialog = Dialog(ctx)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.setContentView(R.layout.custom_logout_dialog)
//
//            dialog.window?.apply {
//                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                val width = (ctx.resources.displayMetrics.widthPixels * 0.9).toInt()
//                setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
//            }
//
//            val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
//            val btnLogout = dialog.findViewById<Button>(R.id.btnLogout)
//
//            btnCancel.background = ContextCompat.getDrawable(ctx, R.drawable.button_cancel_background)
//            btnLogout.background = ContextCompat.getDrawable(ctx, R.drawable.button_logout_background)
//
//            btnCancel.setOnClickListener {
//                dialog.dismiss()
//            }
//
//            btnLogout.setOnClickListener {
//                AuthManager.logout(requireContext())
//                println("Current user id $AuthManager.currentUserId")
//                dialog.dismiss()
//                startActivity(Intent(context, LogoutActivity::class.java))
//            }
//
//            dialog.show()
//        }
//    }

    private fun showLogoutBottomSheet(){
        val bottomSheetDialog = com.android.newuplift.dialogfragment.BottomSheetDialog()
        bottomSheetDialog.setLogoutListener(object : BottomSheetDialog.LogoutListener {
            override fun onLogoutConfirmed() {
                AuthManager.logout(requireContext())
                startActivity(Intent(context, LogoutActivity::class.java))
            }
        })

        bottomSheetDialog.show(parentFragmentManager, "LogoutBottomSheet")
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}

