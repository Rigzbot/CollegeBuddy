package com.example.collegebuddy.database

import androidx.room.*
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