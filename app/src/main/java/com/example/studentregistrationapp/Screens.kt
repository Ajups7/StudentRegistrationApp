package com.example.studentregistrationapp

sealed class Screens(val route: String) {
    object AdminLoginScreen : Screens("admin_login_screen")
    object StudentRegistrationScreen : Screens("student_registration_screen")
    object TakePicture: Screens("take_picture_screen" )
    object SearchStudents: Screens("search_students_screen")
}
