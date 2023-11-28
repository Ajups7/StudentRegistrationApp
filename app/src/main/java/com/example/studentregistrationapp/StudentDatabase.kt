package com.example.studentregistrationapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Student::class],
    version = 1
)

abstract class StudentDatabase: RoomDatabase() {
    abstract val dao: StudentDao
}