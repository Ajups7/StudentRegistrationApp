package com.example.studentregistrationapp

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.studentregistrationapp.Student

@Database(
    entities = [Student::class],
    version = 1
)

abstract class StudentDatabase: RoomDatabase() {
    abstract val dao: StudentDao
}