package com.example.collegebuddy.di

import android.app.Application
import androidx.room.Room
import com.example.collegebuddy.database.PdfDatabase
import com.example.collegebuddy.database.SubjectsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSubjectsDatabase(app: Application): SubjectsDatabase =
        Room.databaseBuilder(app, SubjectsDatabase::class.java, "subjects_database")
            .build()

    @Provides
    @Singleton
    fun providePdfDatabase(app: Application): PdfDatabase =
        Room.databaseBuilder(app, PdfDatabase::class.java, "pdf_database")
            .build()
}