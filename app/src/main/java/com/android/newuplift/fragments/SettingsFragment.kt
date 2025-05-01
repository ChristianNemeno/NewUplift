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
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial

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

        val backButton = view.findViewById<ImageButton>(R.id.backButton)
        val toolbar = view.findViewById<MaterialToolbar>(R.id.topAppBar)
        val profileButton = view.findViewById<View>(R.id.profileSection)


        val developerButton = view.findViewById<View>(R.id.aboutSection)
        val logoutButton = view.findViewById<View>(R.id.logoutSection)

        val themeSwitch = view.findViewById<SwitchMaterial>(R.id.appearanceSwitch)
        themeSwitch.isChecked = ThemeUtils.isDarkMode(requireContext())

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            ThemeUtils.setDarkMode(requireContext(), isChecked)
        }

        toolbar.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_homeFragment)
        }

        profileButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_profileFragment)

        }

        developerButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_developerFragment)
        }

        logoutButton.setOnClickListener {
            showLogoutBottomSheet()
        }


    }

    private fun showLogoutBottomSheet(){
        val bottomSheetDialog = com.android.newuplift.dialogfragment.BottomSheetDialog()
        bottomSheetDialog.setLogoutListener(object : BottomSheetDialog.LogoutListener {
            override fun onLogoutConfirmed() {
                AuthManager.logout(requireContext())
                val intent = Intent(requireContext(), LogoutActivity::class.java)
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

