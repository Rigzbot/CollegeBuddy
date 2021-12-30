package com.example.collegebuddy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.collegebuddy.adapters.SubjectsAdapter
import com.example.collegebuddy.databinding.FragmentNotesBinding
import com.example.collegebuddy.util.BottomSheetFragment
import com.example.collegebuddy.viewModels.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.widget.EditText
import com.example.collegebuddy.R
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collegebuddy.adapters.SubjectClick
import com.example.collegebuddy.adapters.SubjectLongClick
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotesViewModel by viewModels()

    private lateinit var viewModelSubjectAdapter: SubjectsAdapter
    private var actionMode: ActionMode? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater)
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
            .setMessage("Deleting this folder(s) will also delete all the Pdf's inside it. Are you sure you would like to delete it?")
            .setPositiveButton(getString(R.string.alert_dialog_yes)) { _, _ ->
                viewModel.deleteSubjects()
            }
            .setNegativeButton(getString(R.string.alert_dialog_cancel)) { _, _ ->
                viewModel.deselectAll()
                actionMode = null
            }.show()
    }

    private fun observeValues() {
        lifecycle.coroutineScope.launch {
            viewModel.getSubjects().collect {
                viewModelSubjectAdapter.submitList(it)
                binding.addSubjectHint.visibility =
                    if (!it.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        }
    }

    private fun setupViews() {
        viewModelSubjectAdapter = SubjectsAdapter(SubjectClick {
            if (actionMode != null) {
                viewModel.updateSelected(it.name)
            } else {
                val action =
                    NotesFragmentDirections.actionNavigationNotesToSubjectNotesFragment(it.name)
                binding.root.findNavController().navigate(action)
            }
        }, SubjectLongClick {
            if (actionMode == null) {
                actionMode = requireActivity().startActionMode(actionModelCallBack)
            }
            viewModel.updateSelected(it.name)
        })

        binding.bottomSheetButton.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment.newInstance()
            bottomSheetFragment.show(
                requireActivity().supportFragmentManager,
                "Note Bottom Sheet Fragment"
            )
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelSubjectAdapter
        }

        binding.fabBtnCreateNote.setOnClickListener {
            createAlertDialog()
        }
    }

    private fun createAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Subject Name")

        val customLayout = layoutInflater.inflate(R.layout.custom_alert_layout, null)
        builder.setView(customLayout)

        builder
            .setPositiveButton("OK") { _, _ ->
                val editText = customLayout.findViewById<EditText>(R.id.editText)
                val subject = editText.text.toString()

                if (TextUtils.isEmpty(subject)) {
                    Toast.makeText(context, "Please enter subject name", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.insertSubject(subject)
                }
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        actionMode?.finish()
        _binding = null
    }
}