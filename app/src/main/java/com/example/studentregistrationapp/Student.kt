package com.example.studentregistrationapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Student(
    val firstName: String,
    val lastName: String,
    val location: String,
    val course: String,
    val faculty: String,
    val studentID: Long = System.currentTimeMillis(),
    val isBlacklisted: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageurl: String = ""
)
