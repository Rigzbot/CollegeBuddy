package com.example.collegebuddy.ui

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.collegebuddy.databinding.FragmentAttendanceBinding
import com.example.collegebuddy.util.BottomSheetFragment
import com.example.collegebuddy.util.SavedPreference
import com.example.collegebuddy.viewModels.AttendanceViewModel
import org.eazegraph.lib.models.PieModel

class AttendanceFragment : Fragment() {

    private var _binding: FragmentAttendanceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AttendanceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttendanceBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.enrolmentNumber = SavedPreference.getEnrolment(requireContext())
        viewModel.getAttendanceValues()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeValues()
    }

    private fun observeValues() {
        viewModel.isUpdated.observe(viewLifecycleOwner, {
            if (it) {
                Toast.makeText(context, "Attendance Successfully Updated!", Toast.LENGTH_SHORT)
                    .show()
                binding.totalClassesEt.text?.clear()
                binding.attendedClassesEt.text?.clear()
                viewModel.changeIsUpdated()
            }
        })

        viewModel.classesAttended.observe(viewLifecycleOwner, {
            val attendance = if (!it.isNullOrEmpty()) {
                it.toFloat()
            } else {
                0F
            }
            binding.pieChart.addPieSlice(
                PieModel(
                    "Present", attendance,
                    Color.parseColor("#29B6F6")
                )
            )
            binding.pieChart.startAnimation()
        })

        viewModel.classesAbsent.observe(viewLifecycleOwner, {
            val absent = if (!it.isNullOrEmpty()) {
                it.toFloat()
            } else {
                0F
            }
            binding.pieChart.addPieSlice(
                PieModel(
                    "Absent", absent,
                    Color.parseColor("#EF5350")
                )
            )
        })
    }

    private fun setupViews() {
        binding.bottomSheetButton.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment.newInstance()

            bottomSheetFragment.show(
                requireActivity().supportFragmentManager,
                "Id card Bottom Sheet Fragment"
            )
        }

        binding.submitButton.setOnClickListener {
            updateAttendance()
        }

        binding.pieChart.startAnimation()
    }

    private fun updateAttendance() {
        val attended = binding.attendedClassesEt.text.toString()
        val total = binding.totalClassesEt.text.toString()

        if (total.toInt() < attended.toInt()) {
            Toast.makeText(
                context,
                "Attended classes cannot be greater than total classes. Please enter again",
                Toast.LENGTH_LONG
            ).show()
            binding.attendedClassesEt.text?.clear()
            binding.totalClassesEt.text?.clear()
            return
        }

        if (TextUtils.isEmpty(attended) || TextUtils.isEmpty(total)) {
            Toast.makeText(context, "Please enter both values", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.updateAttendance(
            SavedPreference.getEnrolment(requireContext())!!,
            attended,
            (total.toInt() - attended.toInt()).toString()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}