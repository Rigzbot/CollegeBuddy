package com.example.collegebuddy.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collegebuddy.database.SubjectsDatabase
import com.example.collegebuddy.domain.Subject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    dbSubjects: SubjectsDatabase
) : ViewModel() {

    private val subjectsDao = dbSubjects.subjectsDao()

    fun getSubjects(): Flow<List<Subject>> = subjectsDao.getSubjects()

    fun deleteSubjects(subjects: List<String>) {
        viewModelScope.launch {
            for (subject in subjects) {
                subjectsDao.deleteSubject(subject)
            }
        }
    }

    fun insertSubject(subject: String) {
        viewModelScope.launch {
            subjectsDao.insert(Subject(subject))
        }
    }
}