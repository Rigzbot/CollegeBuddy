package com.example.collegebuddy.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "databasesubjects")
data class Subject(
    @PrimaryKey
    val name: String,
    val isSelected: Boolean = false
)

@Entity(tableName = "databasepdf")
data class Pdf(
    @PrimaryKey
    val pdfAddress: String,
    val name: String,
    val size: String,
    val subject: String,
    val isSelected: Boolean = false
)

data class User(
    val enrolmentNumber: String,
    val email: String,
    val name: String
)

data class Attendance(
    val absent: String,
    val attended: String
)