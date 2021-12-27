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

    @Query("DELETE FROM databasesubjects where name = :name")
    suspend fun deleteSubject(name: String)
}

@Database(entities = [Subject::class], version = 1)
abstract class SubjectsDatabase: RoomDatabase() {
    abstract fun subjectsDao(): SubjectsDao
}

@Dao
interface PdfDao {
    @Query("SELECT * FROM databasepdf where subject = :subject")
    fun getPdf(subject: String): Flow<List<Pdf>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pdf: Pdf)

    @Query("DELETE FROM databasepdf where pdfAddress = :pdfAddress")
    suspend fun deletePdf(pdfAddress: String)
}

@Database(entities = [Pdf::class], version = 1)
abstract class PdfDatabase: RoomDatabase() {
    abstract fun pdfDao(): PdfDao
}