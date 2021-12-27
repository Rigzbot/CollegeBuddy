package com.example.collegebuddy.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.collegebuddy.databinding.FragmentSubjectNotesBinding
import com.example.collegebuddy.viewModels.NotesViewModel
import android.app.Activity

import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.navigation.findNavController
import android.provider.OpenableColumns

import android.content.ContentResolver
import android.database.Cursor
import android.util.Log
import com.example.collegebuddy.viewModels.SubjectNotesViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val PDF_SELECTION_CODE = 99

@AndroidEntryPoint
class SubjectNotesFragment : Fragment() {
    private val args: SubjectNotesFragmentArgs by navArgs()

    private var _binding: FragmentSubjectNotesBinding? = null
    private val binding get() = _binding!!

    val viewModel: SubjectNotesViewModel by viewModels()

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
        binding.fabAddPdf.setOnClickListener {
            selectPdfFromStorage()
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
            val selectedPdfFromStorage = data.data
            goToPdfViewer(selectedPdfFromStorage)
        }
    }

    private fun goToPdfViewer(uri: Uri?) {
        val action = SubjectNotesFragmentDirections.actionSubjectNotesFragmentToPdfViewFragment(uri.toString())
        binding.root.findNavController().navigate(action)
    }

    //requireContext.queryResolver
//    private fun queryName(resolver: ContentResolver, uri: Uri): String? {
//        val returnCursor = resolver.query(uri, null, null, null, null)!!
//        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//        returnCursor.moveToFirst()
//        val name = returnCursor.getString(nameIndex)
//        returnCursor.close()
//        return name
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}