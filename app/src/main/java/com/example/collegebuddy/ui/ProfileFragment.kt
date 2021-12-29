package com.example.collegebuddy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.collegebuddy.databinding.FragmentProfileBinding
import com.example.collegebuddy.viewModels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding ?= null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}