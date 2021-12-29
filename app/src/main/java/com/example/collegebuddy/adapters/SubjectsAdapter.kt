package com.example.collegebuddy.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.collegebuddy.adapters.SubjectsAdapter.SubjectViewHolder
import com.example.collegebuddy.adapters.SubjectsAdapter.SubjectViewHolder.SubjectComparator
import com.example.collegebuddy.databinding.NotesSubjectItemBinding
import com.example.collegebuddy.domain.Subject

class SubjectClick(val block: (Subject) -> Unit) {
    fun onClick(subject: Subject) = block(subject)
}

class SubjectLongClick(val block: (Subject) -> Unit) {
    fun onLongClick(subject: Subject): Boolean {
        block(subject)
        return false
    }
}

class SubjectsAdapter(
    private val callback: SubjectClick,
    private val longCallback: SubjectLongClick
) :
    ListAdapter<Subject, SubjectViewHolder>(SubjectComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val binding =
            NotesSubjectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val currentItem = getItem(position)

        if(currentItem.isSelected){
            holder.itemView.setBackgroundColor(Color.GREEN)
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }

        if (currentItem != null) {
            holder.bind(currentItem, callback, longCallback)
        }
    }

    class SubjectViewHolder(private val binding: NotesSubjectItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(subject: Subject, callback: SubjectClick, longCallback: SubjectLongClick) {
            binding.apply {
                this.subject = subject
                this.subjectCallback = callback
                this.subjectLongCallback = longCallback
            }
        }

        class SubjectComparator : DiffUtil.ItemCallback<Subject>() {
            override fun areItemsTheSame(oldItem: Subject, newItem: Subject) =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Subject, newItem: Subject) =
                oldItem == newItem
        }
    }
}