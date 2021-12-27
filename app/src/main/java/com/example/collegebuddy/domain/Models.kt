package com.example.collegebuddy.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "databasesubjects")
data class Subject(
    @PrimaryKey
    val name: String
)

@Entity(tableName = "databasepdf")
data class Pdf(
    @PrimaryKey
    val pdfAddress: String,
    val name: String,
    val size: String,
    val subject: String
)