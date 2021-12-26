package com.example.collegebuddy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.collegebuddy.databinding.NotesSubjectItemBinding
import com.example.collegebuddy.domain.Subject

class SubjectClick(val block: (Subject) -> Unit) {
    fun onClick(subject: Subject) = block(subject)
}

class SubjectsAdapter(private val callback: SubjectClick) :
    ListAdapter<Subject, SubjectsAdapter.SubjectViewHolder>(SubjectViewHolder.SubjectComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val binding = NotesSubjectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val currentItem = getItem(position)
        if(currentItem != null) {
            holder.bind(currentItem, callback)
        }
    }

    class SubjectViewHolder(private val binding: NotesSubjectItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(subject: Subject, callback: SubjectClick) {
                binding.apply {
                    this.subject = subject
                    this.subjectCallback = callback
                }
            }

        class SubjectComparator: DiffUtil.ItemCallback<Subject>() {
            override fun areItemsTheSame(oldItem: Subject, newItem: Subject) =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Subject, newItem: Subject) =
                oldItem == newItem
        }
    }
}