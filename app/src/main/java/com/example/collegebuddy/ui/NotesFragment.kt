package com.example.collegebuddy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collegebuddy.adapters.SubjectClick
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotesViewModel by viewModels()

    private lateinit var viewModelSubjectAdapter: SubjectsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeValues()
    }

    private fun observeValues() {
        lifecycle.coroutineScope.launch {
            viewModel.getSubjects().collect {
                viewModelSubjectAdapter.submitList(it)
            }
        }
    }

    private fun setupViews() {
        viewModelSubjectAdapter = SubjectsAdapter(SubjectClick {
            val action =
                NotesFragmentDirections.actionNavigationNotesToSubjectNotesFragment(it.name)
            binding.root.findNavController().navigate(action)
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
        _binding = null
    }
}