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
import android.content.ActivityNotFoundException
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collegebuddy.adapters.PdfAdapter
import com.example.collegebuddy.adapters.PdfClick
import com.example.collegebuddy.viewModels.SubjectNotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
            openFile(it.pdfAddress)
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

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Uri? = result.data?.data
            viewModel.savePdf(requireContext().contentResolver, data!!, args.subjectName)
        }
    }

    private fun selectPdfFromStorage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        resultLauncher.launch(intent)
    }

    private fun goToPdfViewer(pdfAddress: String) {
        val action = SubjectNotesFragmentDirections.actionSubjectNotesFragmentToPdfViewFragment(pdfAddress)
        binding.root.findNavController().navigate(action)
    }

    private fun openFile(path: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(path.toUri(), "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            goToPdfViewer(path)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}