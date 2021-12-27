package com.example.collegebuddy.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.collegebuddy.databinding.FragmentSubjectNotesBinding
import android.app.Activity
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collegebuddy.adapters.PdfAdapter
import com.example.collegebuddy.adapters.PdfClick
import com.example.collegebuddy.viewModels.SubjectNotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val PDF_SELECTION_CODE = 99

@AndroidEntryPoint
class SubjectNotesFragment : Fragment() {
    private val args: SubjectNotesFragmentArgs by navArgs()

    private var _binding: FragmentSubjectNotesBinding? = null
    private val binding get() = _binding!!

    val viewModel: SubjectNotesViewModel by viewModels()

    private lateinit var viewModelPdfAdapter: PdfAdapter

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
        observeValues()
    }

    private fun setupViews() {
        binding.fabAddPdf.setOnClickListener {
            selectPdfFromStorage()
        }

        viewModelPdfAdapter = PdfAdapter(PdfClick {
            goToPdfViewer(it.pdfAddress)
        })

        binding.pdfRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelPdfAdapter
        }
    }

    private fun observeValues() {
        lifecycle.coroutineScope.launch {
            viewModel.getPdf(args.subjectName).collect {
                viewModelPdfAdapter.submitList(it)
            }
        }
    }

    private fun selectPdfFromStorage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        startActivityForResult(
            Intent.createChooser(intent, "Select PDF"), PDF_SELECTION_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PDF_SELECTION_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val pdfUri = data.data
            viewModel.savePdf(requireContext().contentResolver, pdfUri!!, args.subjectName)
        }
    }

    private fun goToPdfViewer(pdfAddress: String) {
        val action = SubjectNotesFragmentDirections.actionSubjectNotesFragmentToPdfViewFragment(pdfAddress)
        binding.root.findNavController().navigate(action)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}