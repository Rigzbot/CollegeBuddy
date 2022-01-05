package com.example.collegebuddy.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.example.collegebuddy.R
import com.example.collegebuddy.databinding.FragmentBottomSheetBinding
import com.example.collegebuddy.viewModels.BottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment: BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BottomSheetViewModel by viewModels()

    companion object {
        fun newInstance(): BottomSheetFragment {
            val args = Bundle()
            val fragment = BottomSheetFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("InflateParams", "RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_bottom_sheet, null)
        dialog.setContentView(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        val idAddress = SavedPreference.getIdCard(requireContext())
        viewModel.updateIdAddress(idAddress!!.toUri())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeValues()
    }

    private fun setupViews() {
        binding.uploadIdCard.setOnClickListener {
            selectImageFromStorage()
        }

        binding.deleteIdCard.setOnClickListener {
            confirmDeletion()
        }
    }

    private fun observeValues() {
        viewModel.idAddress.observe(viewLifecycleOwner, {
            if(it == "".toUri())
                uploadVisible()
             else
                uploadHidden()
            binding.idCardImage.setImageURI(it)
        })
    }

    private fun confirmDeletion() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Delete")
            .setMessage("Are you sure, you would like to delete this Id Card?")
            .setPositiveButton(getString(R.string.alert_dialog_yes)) { _, _ ->
                SavedPreference.setIdCard(requireContext(), "")
                viewModel.updateIdAddress("".toUri())
            }
            .setNegativeButton(getString(R.string.alert_dialog_cancel), null).show()
    }

    private fun uploadHidden() {
        binding.uploadIdCard.visibility = View.GONE
        binding.deleteIdCard.visibility = View.VISIBLE
    }

    private fun uploadVisible() {
        binding.uploadIdCard.visibility = View.VISIBLE
        binding.deleteIdCard.visibility = View.GONE
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Uri? = result.data?.data
            SavedPreference.setIdCard(requireContext(), data.toString())
            viewModel.updateIdAddress(data!!)
        }
    }

    private fun selectImageFromStorage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        resultLauncher.launch(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}