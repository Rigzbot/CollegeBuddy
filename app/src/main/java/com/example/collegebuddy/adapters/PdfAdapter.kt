package com.example.collegebuddy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.collegebuddy.adapters.PdfAdapter.PdfViewHolder
import com.example.collegebuddy.adapters.PdfAdapter.PdfViewHolder.PdfComparator
import com.example.collegebuddy.databinding.PdfItemBinding
import com.example.collegebuddy.domain.Pdf

class PdfClick(val block: (Pdf) -> Unit) {
    fun onCLick(pdf: Pdf) = block(pdf)
}

class PdfAdapter(private val callback: PdfClick) :
    ListAdapter<Pdf, PdfViewHolder>(PdfComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val binding = PdfItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PdfViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        val currentItem = getItem(position)
        if(currentItem != null) {
            holder.bind(currentItem, callback)
        }
    }

    class PdfViewHolder(private val binding: PdfItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pdf: Pdf, callback: PdfClick) {
            binding.apply {
                this.pdf = pdf
                this.pdfCallback = callback
            }
        }

        class PdfComparator : DiffUtil.ItemCallback<Pdf>() {
            override fun areItemsTheSame(oldItem: Pdf, newItem: Pdf) =
                oldItem.pdfAddress == newItem.pdfAddress

            override fun areContentsTheSame(oldItem: Pdf, newItem: Pdf) =
                oldItem == newItem
        }
    }
}