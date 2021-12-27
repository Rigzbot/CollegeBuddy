package com.example.collegebuddy.viewModels

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collegebuddy.database.PdfDatabase
import com.example.collegebuddy.domain.Pdf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import javax.inject.Inject

@HiltViewModel
class SubjectNotesViewModel @Inject constructor(
    dbPdf: PdfDatabase
) : ViewModel() {
    private val pdfDao = dbPdf.pdfDao()

    fun savePdf(resolver: ContentResolver, uri: Uri, subject: String) {
        val returnCursor = resolver.query(uri, null, null, null, null)!!
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val size = returnCursor.getString(sizeIndex)
        val name = returnCursor.getString(nameIndex)
        returnCursor.close()
        val sizeString = convertSize(size.toLong())

        viewModelScope.launch {
            pdfDao.insert(Pdf(uri.toString(), name, sizeString, subject))
        }
    }

    fun getPdf(subject: String): Flow<List<Pdf>> = pdfDao.getPdf(subject)

    fun deletePdf(pdfList: List<String>) {
        viewModelScope.launch {
            for(pdf in pdfList){
                pdfDao.deletePdf(pdf)
            }
        }
    }

    private fun convertSize(size: Long): String {
        var tempSize = size
        var suffix: String? = null

        if (tempSize >= 1024) {
            suffix = " KB"
            tempSize /= 1024
            if (tempSize >= 1024) {
                suffix = " MB"
                tempSize /= 1024
                if(tempSize >= 1024){
                    suffix = "GB"
                    tempSize /= 1024
                }
            }
        }
        val resultBuffer = StringBuilder(tempSize.toString())

        var commaOffset = resultBuffer.length - 3
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',')
            commaOffset -= 3
        }
        if (suffix != null) resultBuffer.append(suffix)
        Log.d("Test", resultBuffer.toString())
        return resultBuffer.toString()
    }
}