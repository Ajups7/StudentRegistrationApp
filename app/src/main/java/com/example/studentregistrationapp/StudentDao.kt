package com.example.studentregistrationapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {

    @Upsert
    suspend fun upsertStudent(student: Student)

//    @Query("UPDATE student SET isBlacklisted = 1")
//    suspend fun blacklistStudent(student: Student)


    @Query("SELECT * FROM student ORDER BY lastName ASC")
    fun getStudentOrderedByLastName(): Flow<List<Student>>

    @Query("SELECT * FROM student ORDER BY course ASC")
    fun getStudentOrderedBycourse(): Flow<List<Student>>

    @Query("SELECT *FROM student ORDER BY studentID ASC")
    fun getStudentOrderedBystudentID(): Flow<List<Student>>
}