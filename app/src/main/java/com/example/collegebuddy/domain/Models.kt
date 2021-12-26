package com.example.collegebuddy.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "databasesubjects")
data class Subject(
    @PrimaryKey
    val name: String
)