package com.example.collegebuddy.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collegebuddy.database.PdfDatabase
import com.example.collegebuddy.database.SubjectsDatabase
import com.example.collegebuddy.domain.Subject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    dbSubjects: SubjectsDatabase,
    dbPdf: PdfDatabase
) : ViewModel() {

    private val subjectsDao = dbSubjects.subjectsDao()
    private val pdfDao = dbPdf.pdfDao()

    fun getSubjects(): Flow<List<Subject>> = subjectsDao.getSubjects()

    fun deleteSubjects() {
        viewModelScope.launch {
            val list = subjectsDao.getDeletedSubjects()
            subjectsDao.deleteSubject()
            list.forEach {
                Log.d("Test", it.name)
                pdfDao.deleteSubjectPdf(it.name)
            }
        }
    }

    fun updateSelected(subject: String) {
        viewModelScope.launch {
            subjectsDao.updateSelection(subject)
        }
    }

    fun insertSubject(subject: String) {
        viewModelScope.launch {
            subjectsDao.insert(Subject(subject))
        }
    }

    fun deselectAll() {
        viewModelScope.launch {
            subjectsDao.deselectAll()
        }
    }
}