package com.example.collegebuddy.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.collegebuddy.databinding.FragmentSubjectNotesBinding
import android.app.Activity
import android.content.ActivityNotFoundException
import android.net.Uri
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collegebuddy.R
import com.example.collegebuddy.adapters.PdfAdapter
import com.example.collegebuddy.adapters.PdfClick
import com.example.collegebuddy.adapters.PdfLongClick
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
    private var actionMode: ActionMode? = null

    private lateinit var viewModelPdfAdapter: PdfAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubjectNotesBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.deselectAll()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeValues()
    }

    private val actionModelCallBack: ActionMode.Callback = object : ActionMode.Callback {
        var dialogCalled: Boolean = false
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            requireActivity().menuInflater.inflate(R.menu.action_bar_delete, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            if (item!!.itemId == R.id.action_delete) {
                confirmDeletion()
                dialogCalled = true
                actionMode!!.finish()
                return true
            }
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            if (!dialogCalled)
                viewModel.deselectAll()
            actionMode = null
        }
    }

    private fun confirmDeletion() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Delete")
            .setMessage("Are you sure you would like to delete the selected Pdf(s)?")
            .setPositiveButton(getString(R.string.alert_dialog_yes)) { _, _ ->
                viewModel.deletePdf()
            }
            .setNegativeButton(getString(R.string.alert_dialog_cancel)) { _, _ ->
                viewModel.deselectAll()
                actionMode = null
            }.show()
    }

    private fun setupViews() {
        binding.fabAddPdf.setOnClickListener {
            selectPdfFromStorage()
        }

        viewModelPdfAdapter = PdfAdapter(PdfClick {
            if (actionMode != null) {
                viewModel.updateSelected(it.pdfAddress)
            } else {
                openFile(it.pdfAddress)
            }
        }, PdfLongClick {
            if (actionMode == null) {
                actionMode = requireActivity().startActionMode(actionModelCallBack)
            }

            viewModel.updateSelected(it.pdfAddress)
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
                binding.addPdfHint.visibility =
                    if (!it.isNullOrEmpty()) View.GONE else View.VISIBLE
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
        actionMode?.finish()
        _binding = null
    }
}