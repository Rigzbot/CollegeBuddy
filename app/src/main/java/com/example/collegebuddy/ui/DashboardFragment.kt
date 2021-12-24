package com.example.collegebuddy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.collegebuddy.databinding.FragmentDashboardBinding
import com.example.collegebuddy.util.BottomSheetFragment
import com.example.collegebuddy.viewModels.DashboardViewModel
import com.example.collegebuddy.viewModels.NotificationsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        setupViews()
        return binding.root
    }

    private fun setupViews() {
        binding.bottomSheetButton.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment.newInstance()
            bottomSheetFragment.show(
                requireActivity().supportFragmentManager,
                "Note Bottom Sheet Fragment"
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}