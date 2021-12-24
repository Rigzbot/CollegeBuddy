package com.example.collegebuddy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.collegebuddy.databinding.FragmentNotesBinding
import com.example.collegebuddy.util.BottomSheetFragment
import com.example.collegebuddy.viewModels.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater)
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