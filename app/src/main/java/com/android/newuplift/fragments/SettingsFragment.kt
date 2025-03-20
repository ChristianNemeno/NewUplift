package com.android.newuplift.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.android.newuplift.R

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
        favButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_favoriteQuotesFragment)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}