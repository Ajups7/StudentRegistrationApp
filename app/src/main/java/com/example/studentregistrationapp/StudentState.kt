package com.example.studentregistrationapp

data class StudentState(
    val students: List<Student> = emptyList(),
    val firstName: String = "",
    val lastName: String = "",
    val location: String = "",
    val course: String = "",
    val faculty: String = "",
    val studentID: Long = 0L,
    val isAddingStudent: Boolean = false,
    val sortType: SortType = SortType.LAST_NAME

)
