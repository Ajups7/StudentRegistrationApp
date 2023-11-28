package com.example.studentregistrationapp

sealed interface StudentEvent {
    object SaveStudentProfile: StudentEvent
    data class SetFirstName(val firstName: String): StudentEvent
    data class SetLastName(val lastName: String): StudentEvent
    data class SetLocation(val location: String): StudentEvent
    data class SetCourse(val course: String): StudentEvent
    data class SetFaculty(val faculty: String): StudentEvent
    data class SortStudents(val sortType: SortType): StudentEvent
    data class BlacklistStudents(val student: Student): StudentEvent
    object ShowDialog: StudentEvent
    object HideDialog: StudentEvent
    object CaptureStudents: StudentEvent
    data class SetImageUri(val imageUri: String): StudentEvent
    object SaveStudentImage: StudentEvent
    data class SetStudentToUpdate(val student: Student): StudentEvent

}