package com.example.collegebuddy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.collegebuddy.databinding.FragmentSubjectNotesBinding
import com.example.collegebuddy.viewModels.NotesViewModel

class SubjectNotesFragment : Fragment() {
    private val args: SubjectNotesFragmentArgs by navArgs()

    private var _binding: FragmentSubjectNotesBinding?= null
    private val binding get() = _binding!!

    val viewModel: NotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubjectNotesBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}