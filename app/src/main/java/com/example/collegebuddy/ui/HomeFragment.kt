package com.example.collegebuddy.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.collegebuddy.R
import com.example.collegebuddy.databinding.FragmentHomeBinding
import com.example.collegebuddy.util.BottomSheetFragment
import com.example.collegebuddy.util.SavedPreference
import com.example.collegebuddy.viewModels.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        val imageAddress = SavedPreference.getImage(requireContext())
        if(!imageAddress.isNullOrEmpty())
            showButtons()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeValues()
        setupViews()
    }

    private fun observeValues() {
        viewModel.ttAddress.observe(viewLifecycleOwner, {
            binding.ttImage.setImageURI(it)
        })
    }

    private fun setupViews() {
        binding.bottomSheetButton.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment.newInstance()
            bottomSheetFragment.show(
                requireActivity().supportFragmentManager,
                "Note Bottom Sheet Fragment"
            )
        }

        binding.fabAddTt.setOnClickListener {
            selectImageFromStorage()
        }

        binding.deleteTt.setOnClickListener {
            confirmDeletion()
        }
    }

    private fun showButtons() {
        binding.deleteTt.visibility = View.GONE
        binding.fabAddTt.visibility = View.VISIBLE
        binding.addTtHint.visibility = View.VISIBLE
    }

    private fun hideButtons() {
        binding.deleteTt.visibility = View.VISIBLE
        binding.fabAddTt.visibility = View.GONE
        binding.addTtHint.visibility = View.GONE
    }

    private fun confirmDeletion() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Delete")
            .setMessage("Are you sure, you would like to delete the time table?")
            .setPositiveButton(getString(R.string.alert_dialog_yes)) { _, _ ->
                SavedPreference.setImage(requireContext(), "")
                viewModel.updateTTAddress("".toUri())
                showButtons()
            }
            .setNegativeButton(getString(R.string.alert_dialog_cancel), null).show()
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Uri? = result.data?.data
            SavedPreference.setImage(requireContext(), data.toString())
            viewModel.updateTTAddress(data!!)
            hideButtons()
        }
    }

    private fun selectImageFromStorage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        resultLauncher.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}