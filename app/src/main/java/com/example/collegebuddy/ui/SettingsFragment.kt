package com.example.collegebuddy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.collegebuddy.R
import com.example.collegebuddy.databinding.FragmentProfileBinding
import com.example.collegebuddy.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}