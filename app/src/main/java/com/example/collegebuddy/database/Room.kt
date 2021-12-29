package com.example.collegebuddy.database

import androidx.room.*
import com.example.collegebuddy.domain.Pdf
import com.example.collegebuddy.domain.Subject
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectsDao {
    @Query("SELECT * FROM databasesubjects")
    fun getSubjects(): Flow<List<Subject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subject: Subject)

    @Query("DELETE FROM databasesubjects WHERE isSelected = 1")
    suspend fun deleteSubject()

    @Query("SELECT * from databasesubjects WHERE isSelected = 1")
    suspend fun getDeletedSubjects(): List<Subject>

    @Query("UPDATE databasesubjects SET isSelected =  NOT isSelected WHERE name = :name")
    suspend fun updateSelection(name: String)

    @Query("UPDATE databasesubjects SET isSelected = 0")
    suspend fun deselectAll()
}

@Database(entities = [Subject::class], version = 2)
abstract class SubjectsDatabase: RoomDatabase() {
    abstract fun subjectsDao(): SubjectsDao
}

@Dao
interface PdfDao {
    @Query("SELECT * FROM databasepdf WHERE subject = :subject")
    fun getPdf(subject: String): Flow<List<Pdf>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pdf: Pdf)

    @Query("DELETE FROM databasepdf WHERE isSelected = 1")
    suspend fun deletePdf()

    @Query("DELETE FROM databasepdf WHERE subject = :subject")
    suspend fun deleteSubjectPdf(subject: String)

    @Query("UPDATE databasepdf SET isSelected = NOT isSelected WHERE pdfAddress = :pdfAddress")
    suspend fun updateSelection(pdfAddress: String)

    @Query("UPDATE databasepdf SET isSelected = 0")
    suspend fun deselectAll()
}

@Database(entities = [Pdf::class], version = 2)
abstract class PdfDatabase: RoomDatabase() {
    abstract fun pdfDao(): PdfDao
}