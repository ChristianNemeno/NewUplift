package com.android.newuplift.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.newuplift.R
import com.android.newuplift.database.DatabaseHelper
import com.android.newuplift.database.QuoteDao
import com.android.newuplift.utility.AuthManager
import com.google.android.material.textfield.TextInputEditText

class ProfileFragment : Fragment() {
    private lateinit var viewModel: UserViewModel
    private lateinit var quoteDao: QuoteDao

    private val PERMISSION_REQUEST_CODE = 102

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize DAO
        val dbHelper = DatabaseHelper(requireContext())
        quoteDao = QuoteDao(dbHelper.writableDatabase)

        // Initialize ViewModel
        viewModel = ViewModelProvider(
            this,
            UserViewModelFactory(quoteDao)
        )[UserViewModel::class.java]


        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = AuthManager.currentUserId
        if (userId == -1) {
            Toast.makeText(requireContext(), "Please log in", Toast.LENGTH_SHORT).show()

            return
        }

        val profileImageView = view.findViewById<ImageView>(R.id.profileImageView)
        val prefs = requireContext().getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
        val uriString = prefs.getString("profileImageUri_user_$userId", null)
        uriString?.let {
            val uri = Uri.parse(it)
            try {
                // Check if permission is still valid
                requireContext().contentResolver.persistedUriPermissions.find { it.uri == uri }?.let {
                    profileImageView.setImageURI(uri)
                }
            } catch (e: SecurityException) {
                // Handle expired permission (clear saved URI)
                prefs.edit().remove("profileImageUri_user_$userId").apply()
            }
        }

        // Observe user details
        viewModel.userDetails.observe(viewLifecycleOwner) { user ->
            user?.let {
                view.findViewById<TextInputEditText>(R.id.nameEditText).setText(it.name)
                view.findViewById<TextInputEditText>(R.id.usernameEditText).setText(it.username)
                view.findViewById<TextInputEditText>(R.id.addressEditText).setText(it.address)
                view.findViewById<TextInputEditText>(R.id.phonenumberEditText).setText(it.number)
                view.findViewById<TextInputEditText>(R.id.emailEditText).setText(it.email)
            }
        }

        // Load data
        viewModel.loadUserDetails(userId)

        val editButton = view.findViewById<Button>(R.id.editProfileButton)
        val backButton = view.findViewById<ImageButton>(R.id.backButton)

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }

        editButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
    }




    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}