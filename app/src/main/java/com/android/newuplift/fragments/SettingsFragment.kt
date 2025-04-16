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
import com.android.newuplift.utility.ThemeUtils
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
       // return inflater.inflate(R.layout.test_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<ImageButton>(R.id.backButton)
        val profileButton = view.findViewById<View>(R.id.profileSection)


        val developerButton = view.findViewById<View>(R.id.aboutSection)
        val logoutButton = view.findViewById<View>(R.id.logoutSection)

        val themeSwitch = view.findViewById<SwitchMaterial>(R.id.appearanceSwitch)
        themeSwitch.isChecked = ThemeUtils.isDarkMode(requireContext())

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            ThemeUtils.setDarkMode(requireContext(), isChecked)
        }

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_homeFragment)
        }

        profileButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_profileFragment)

        }

        developerButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_developerFragment)
        }

        logoutButton.setOnClickListener {
            // i want to use the custom dialog here how?
//            showLogoutDialog()
            showLogoutBottomSheet()
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
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        })

        bottomSheetDialog.show(parentFragmentManager, "LogoutBottomSheet")
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}

