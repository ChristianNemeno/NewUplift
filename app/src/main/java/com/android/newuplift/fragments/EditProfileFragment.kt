package com.android.newuplift.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.newuplift.R
import com.android.newuplift.database.DatabaseHelper
import com.android.newuplift.database.QuoteDao
import com.android.newuplift.utility.AuthManager
import com.android.newuplift.utility.UserAccount
import com.google.android.material.textfield.TextInputEditText

class EditProfileFragment: Fragment() {

    private lateinit var viewModel: UserViewModel
    private lateinit var quoteDao: QuoteDao

    private lateinit var profileImageView: ImageView
    private lateinit var editProfileImageButton: Button
    private val pickImageRequestCode = 101

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
        val dbHelper = DatabaseHelper(requireContext())
        quoteDao = QuoteDao(dbHelper.writableDatabase)
        viewModel = ViewModelProvider(this, UserViewModelFactory(quoteDao))[UserViewModel::class.java]

        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = AuthManager.currentUserId
        if (userId == -1) {
            Toast.makeText(context, "Please log in first", Toast.LENGTH_SHORT).show()
            return
        }

        val name = view.findViewById<TextInputEditText>(R.id.nameEditText)
        val username = view.findViewById<TextInputEditText>(R.id.usernameEditText)
        val address = view.findViewById<TextInputEditText>(R.id.addressEditText)
        val number = view.findViewById<TextInputEditText>(R.id.phonenumberEditText)
        val email = view.findViewById<TextInputEditText>(R.id.emailEditText)
        val doneButton = view.findViewById<Button>(R.id.doneEditButton)
        val backButton = view.findViewById<ImageButton>(R.id.backButton)

        profileImageView = view.findViewById(R.id.profileImageView)
        editProfileImageButton = view.findViewById(R.id.editProfilePictureButton)

        // Load profile image if saved
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

        editProfileImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, pickImageRequestCode)
        }

        viewModel.userDetails.observe(viewLifecycleOwner) { user ->
            name.setText(user.name)
            username.setText(user.username)
            address.setText(user.address)
            number.setText(user.number)
            email.setText(user.email)
        }

        viewModel.loadUserDetails(userId)

        doneButton.setOnClickListener {
            val updated = UserAccount(
                name = name.text.toString().trim(),
                username = username.text.toString().trim(),
                password = "",
                email = email.text.toString().trim(),
                address = address.text.toString().trim(),
                number = number.text.toString().trim()
            )

            viewModel.updateUserDetails(userId, updated)
            Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImageRequestCode && resultCode == AppCompatActivity.RESULT_OK) {
            val imageUri = data?.data
            imageUri?.let { uri ->
                // Take persistable permission
                requireContext().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                // Save URI and set image
                profileImageView.setImageURI(uri)
                val prefs = requireContext().getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
                val userId = AuthManager.currentUserId
                prefs.edit().putString("profileImageUri_user_$userId", uri.toString()).apply()
            }
        }
    }
}
