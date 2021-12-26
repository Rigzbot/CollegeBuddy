package com.example.collegebuddy.di

import android.app.Application
import androidx.room.Room
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
}