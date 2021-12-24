package com.example.collegebuddy.util

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.collegebuddy.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetFragment: BottomSheetDialogFragment() {

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
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }
}