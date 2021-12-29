package com.example.collegebuddy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.collegebuddy.databinding.FragmentPdfViewBinding
import com.example.collegebuddy.viewModels.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint

class PdfViewFragment: Fragment() {
    private val args: PdfViewFragmentArgs by navArgs()

    private var _binding: FragmentPdfViewBinding? = null
    private val binding get() = _binding!!

    val viewModel: NotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPdfViewBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding.pdfView.fromUri(args.pdfUri.toUri())
            .defaultPage(0)
            .load()
    }
}